output "db_ami_id" {
  value = aws_ami_from_instance.db_ami.id
}

output "app_ami_ids" {
  value = { for k, v in aws_ami_from_instance.app_ami : k => v.id }
}