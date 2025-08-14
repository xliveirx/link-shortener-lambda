resource "aws_iam_role" "lambda" {
  name = "${var.name_prefix}-lambda-role"
  assume_role_policy = jsondecode({
    Version = "2012-10-17",
    Statement= [{
      Action = "sts:AssumeRole",
      Effect = "Allow",
      Princiapl = { Service = "lambda.amazonaws.com"}
    }]
  })
}
resource "aws_iam_policy" "lambda" {
  name   = "${var.name_prefix}-lambda-policy"
  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action   = ["logs:CreateLogGroup","logs:CreateLogStream","logs:PutLogEvents"],
        Effect   = "Allow",
        Resource = "*"
      },
      {
        Action   = ["dynamodb:*"],
        Effect   = "Allow",
        Resource = var.dynamodb_arn
      },
      {
        Action   = ["secretsmanager:GetSecretValue"],
        Effect   = "Allow",
        Resource = [var.secret_arn]
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "attach" {
  role       = aws_iam_role.lambda.name
  policy_arn = aws_iam_policy.lambda.arn
}