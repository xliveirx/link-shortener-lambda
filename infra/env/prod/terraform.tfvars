env = "prod"
region = "us-east-1"

app_prefix = "link-shortener-saas"

lambda_handler = "joao.StreamLambdaHandler::handleRequest"

env_vars = {
  "SECRET_NAME" = "dev-link-shortener-saas-jwt-secret"
  "SPRING_PROFILES_ACTIVE" = "prod"
  "ENV" = "prod"
  "JAVA_TOOL_OPTIONS" = "-Dnetworkaddress.cache.ttl=5 -Dnetworkaddress.cache.negative.ttl=0"
}