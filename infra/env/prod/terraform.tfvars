env = "prod"
region = "us-east-1"

app_prefix = "link-shortener-saas"

lambda_handler = "joao.StreamLambdaHandler::handleRequest"

env_vars = {
  "VARIABLE" = "TESTE"
}