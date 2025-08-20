variable "name_prefix" {
  type = string
}

variable "dynamodb_arn" {
  type = list(string)
}

variable "secrets_arn" {
  type = string
}