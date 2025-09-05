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
  description = "EC2 instance type for the Book DB"
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

variable "db_name" {
  description = "Name of the MySQL database"
  type        = string
  default     = "citizen"
}

variable "vpc_id" {
  description = "VPC ID for the infrastructure"
  type        = string
  default     = "vpc-0ede2648c171af852"
}

variable "subnets" {
  description = "Subnets for the load balancer"
  type        = list(string)
  default     = ["subnet-06f63df1e0f9a650d", "subnet-0aa19e41cecd91909", "subnet-0d457d77d0f896869", "subnet-09ced39b59a347889"]
}

variable "key_name" {
  description = "The name of the SSH key pair"
  type        = string
  default     = "cloud_test"
}

variable "jar_name" {
  description = "The name of the app jar file"
  type        = string
  default     = "citizen-service-1.0-SNAPSHOT"
}