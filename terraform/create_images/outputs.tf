output "db_ami_id" {
  value = aws_ami_from_instance.db_ami.id
}

output "app_ami_id" {
  value = aws_ami_from_instance.app_ami.id
}