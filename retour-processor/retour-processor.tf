variable "artifact" {
  default = "./retour-processor/target/retour-processor-1.0.0-SNAPSHOT.jar"
}

resource "aws_lambda_function" "retour_processor_function" {
  runtime = "java8"
  filename = "${var.artifact}"
  source_code_hash = "${filebase64sha256(var.artifact)}"
  function_name = "retour_processor"
  handler = "com.github.cbuschka.retour.retour_processor.RetourLambdaHandler"
  timeout = 10
  reserved_concurrent_executions = 2
  memory_size = 256
  role = "${aws_iam_role.lambda_role.arn}"
  depends_on = [
    "aws_cloudwatch_log_group.lambda_retour_processor_log_group"
  ]
}
