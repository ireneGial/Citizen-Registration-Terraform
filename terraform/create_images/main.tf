provider "aws" {
  region = var.region
}

#Security group allowing SSH - Due to failure in my network
resource "aws_security_group" "allow_ssh" {
  name        = "allow_ssh"
  description = "Allow SSH"

  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "allow_ssh"
  }
}

resource "aws_instance" "db" {
  ami           = "ami-0aff18ec83b712f05" # Ubuntu Server 24.04 LTS AMI
  instance_type = var.instance_type_db
  key_name      = var.key_name
  vpc_security_group_ids = [aws_security_group.allow_ssh.id]  # attach SG here
  tags = {
    Name = "mysql-db"
  }

  user_data = <<-EOF
              #!/bin/bash
              #install MySQL server
              sudo apt-get update
              sudo apt-get install -y mysql-server
              
              # Configure MySQL to allow remote connections
              sudo sed -i 's/^bind-address\s*=.*$/bind-address = 0.0.0.0/' /etc/mysql/mysql.conf.d/mysqld.cnf
              
              # Restart MySQL service
              sudo systemctl restart mysql
              
              # create non-root user and Books db that this user has full access to
              sudo mysql -u root -e "CREATE USER '${var.db_user}'@'%' IDENTIFIED BY '${var.db_password}';"
              sudo mysql -u root -e "CREATE DATABASE ${var.db_name};"
              sudo mysql -u root -e "GRANT ALL PRIVILEGES ON ${var.db_name}.* TO '${var.db_user}'@'%' WITH GRANT OPTION;"
              sudo mysql -u root -e "FLUSH PRIVILEGES;"
              touch /tmp/user_data_complete
              EOF
}

# Poll for instance status to ensure user data script completes
resource "null_resource" "wait_for_db_instance" {
  depends_on = [aws_instance.db]

  provisioner "remote-exec" {
    inline = [
      "while [ ! -f /tmp/user_data_complete ]; do sleep 10; done",
      "echo 'User-data script completed'"
    ]

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file("~/.ssh/cloud_test.pem")
      host        = aws_instance.db.public_ip
    }
  }
}


resource "aws_ami_from_instance" "db_ami" {
  name               = "db-ami"
  source_instance_id = aws_instance.db.id
 
  tags = {
    Name = "citizen-db-ami"
  }

  depends_on = [null_resource.wait_for_db_instance]
}

resource "aws_instance" "app" {
  count         = 3
  ami           = "ami-0aff18ec83b712f05"  # Ubuntu Server 24.04 LTS AMI
  instance_type = var.instance_type_app
  key_name      = var.key_name
  vpc_security_group_ids = [aws_security_group.allow_ssh.id]  # attach SG here
  tags = {
    Name = "spring-boot-setup-${count.index + 1}"
  }

  user_data = <<-EOF
              #!/bin/bash
              sudo apt-get update -y
              sudo apt-get install -y openjdk-21-jdk git maven
              git clone ${var.spring_boot_app_git_repo} /home/ubuntu/app
              cd /home/ubuntu/app
              git fetch
              git checkout -b ${var.git_repo_branch} origin/${var.git_repo_branch}
              mvn clean package
              touch /tmp/user_data_complete
              EOF
}

# Poll for instance status to ensure user data script completes
resource "null_resource" "wait_for_app_instance" {

  for_each = { for idx, inst in aws_instance.app : idx => inst }


  depends_on = [aws_instance.app]

  provisioner "remote-exec" {
    inline = [
      "while [ ! -f /tmp/user_data_complete ]; do sleep 10; done",
      "echo 'User-data script completed'"
    ]

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file("~/.ssh/cloud_test.pem")
      host        = each.value.public_ip
    }
  }
}

resource "aws_ami_from_instance" "app_ami" {

  for_each = { for idx, inst in aws_instance.app : idx => inst }

  name               = "app-ami-${each.key}"
  source_instance_id = each.value.id
  
  tags = {
    Name = "citizen-app-ami-${each.key}"
  }
  
  depends_on = [null_resource.wait_for_app_instance]
}