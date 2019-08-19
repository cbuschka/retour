resource "aws_lambda_event_source_mapping" "retour_processor_function_from_retour_queue_trigger" {
  event_source_arn = "${aws_sqs_queue.retour_queue.arn}"
  enabled = true
  function_name = "${aws_lambda_function.retour_processor_function.arn}"
  batch_size = 1
}
