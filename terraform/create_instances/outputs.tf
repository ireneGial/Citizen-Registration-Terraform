output "app_instance_ids" {
  description = "Instance IDs of the Spring Boot applications"
  value       = aws_instance.app[*].id
}

output "db_instance_id" {
  description = "Instance ID for the Books DB"
  value       = aws_instance.db.id
}

output "load_balancer_dns_name" {
  description = "DNS name of the load balancer"
  value       = aws_lb.app_lb.dns_name
}