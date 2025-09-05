variable "region" {
  description = "The AWS region to deploy to"
  type        = string
  default     = "us-west-2"
}

variable "instance_type_app" {
  description = "EC2 instance type for the Spring Boot application"
  type        = string
  default     = "t3.micro"
}

variable "instance_type_db" {
  description = "EC2 instance type for the MySQL database"
  type        = string
  default     = "t3.micro"
}

variable "db_user" {
  description = "Username for the MySQL database"
  type        = string
  default   = "appuser"
}

variable "db_password" {
  description = "Password for the MySQL database"
  type        = string
  sensitive   = true
}

variable "spring_boot_app_git_repo" {
  description = "Git repository URL of the Spring Boot application"
  type        = string
  default     = "https://github.com/ireneGial/Citizen-Registration-Terraform.git"
}

variable "git_repo_branch" {
  description = "Branch to check in from the Git repository of the Spring Boot application"
  type        = string
  default     = "main"
}

variable "db_name" {
  description = "Name of the database"
  type        = string
  default     = "citizen"
}

variable "key_name" {
  description = "The name of the SSH key pair"
  type        = string
  default     = "cloud_test"
}