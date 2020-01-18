resource "aws_api_gateway_rest_api" "retour" {
  name = "RetourProcessorAPI"
  description = "API Gateway connection for retour processing"
}

resource "aws_api_gateway_resource" "retour" {
  rest_api_id = "${aws_api_gateway_rest_api.retour.id}"
  parent_id = "${aws_api_gateway_rest_api.retour.root_resource_id}"
  path_part = "{proxy+}"
}
resource "aws_api_gateway_method" "retour" {
  rest_api_id = aws_api_gateway_rest_api.retour.id
  resource_id = aws_api_gateway_resource.retour.id
  http_method = "ANY"
  authorization = "NONE"
}
resource "aws_api_gateway_integration" "retour" {
  depends_on = [
    aws_api_gateway_rest_api.retour,
    aws_api_gateway_method.retour

  ]
  rest_api_id = "${aws_api_gateway_rest_api.retour.id}"
  resource_id = "${aws_api_gateway_method.retour.resource_id}"
  http_method = aws_api_gateway_method.retour.http_method
  content_handling = "CONVERT_TO_TEXT"
  integration_http_method = "POST"
  type = "AWS_PROXY"
  uri = "arn:aws:apigateway:eu-central-1:lambda:path/2015-03-31/functions/${aws_lambda_function.retour_processor_function.arn}/invocations"
}

resource "aws_api_gateway_deployment" "retour_dev" {
  depends_on = [
    aws_api_gateway_integration.retour,
  ]

  rest_api_id = aws_api_gateway_rest_api.retour.id
}

resource "aws_api_gateway_stage" "retour_dev" {
  deployment_id = "${aws_api_gateway_deployment.retour_dev.id}"
  rest_api_id = "${aws_api_gateway_rest_api.retour.id}"
  stage_name = "retour-dev"
}

resource "aws_lambda_permission" "apigw_may_invoke_retour_processor_function" {
  statement_id = "AllowAPIGatewayInvoke"
  action = "lambda:InvokeFunction"
  function_name = aws_lambda_function.retour_processor_function.function_name
  principal = "apigateway.amazonaws.com"

  # The "/*/*" portion grants access from any method on any resource
  # within the API Gateway REST API.
  source_arn = "${aws_api_gateway_rest_api.retour.execution_arn}/*/*"
}

output "base_url" {
  value = aws_api_gateway_deployment.retour_dev.invoke_url
}
