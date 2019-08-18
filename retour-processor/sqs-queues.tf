resource "aws_sqs_queue" "dlq_queue" {
  name = "dlq"
  # max, 15 min
  delay_seconds = 0
  # max, 256kb
  max_message_size = 262144
  # max, 14 days
  message_retention_seconds = 1209600
}

resource "aws_sqs_queue" "dlq_fifo_queue" {
  name = "dlq.fifo"
  # max, 15 min
  delay_seconds = 0
  # max, 256kb
  max_message_size = 262144
  # max, 14 days
  message_retention_seconds = 1209600
  fifo_queue = true
}

resource "aws_sqs_queue" "retour_queue" {
  name = "retour"
  delay_seconds = 0
  max_message_size = 2048
  message_retention_seconds = 1209600

  redrive_policy = "{\"deadLetterTargetArn\":\"${aws_sqs_queue.dlq_queue.arn}\",\"maxReceiveCount\":5}"
}

resource "aws_sqs_queue" "retour_ack_fifo_queue" {
  name = "retour_ack.fifo"
  delay_seconds = 0
  max_message_size = 2048
  fifo_queue = true
  content_based_deduplication = false
  message_retention_seconds = 1209600

  redrive_policy = "{\"deadLetterTargetArn\":\"${aws_sqs_queue.dlq_fifo_queue.arn}\",\"maxReceiveCount\":5}"
}

resource "aws_sqs_queue" "retour_err_queue" {
  name = "retour_err"
  delay_seconds = 0
  max_message_size = 2048
  fifo_queue = false
  message_retention_seconds = 1209600

  redrive_policy = "{\"deadLetterTargetArn\":\"${aws_sqs_queue.dlq_queue.arn}\",\"maxReceiveCount\":5}"
}

resource "aws_sqs_queue" "charge_seller_fifo_queue" {
  name = "charge_seller.fifo"
  delay_seconds = 0
  max_message_size = 2048
  fifo_queue = true
  content_based_deduplication = false
  message_retention_seconds = 1209600

  redrive_policy = "{\"deadLetterTargetArn\":\"${aws_sqs_queue.dlq_fifo_queue.arn}\",\"maxReceiveCount\":5}"
}

resource "aws_sqs_queue" "refund_buyer_fifo_queue" {
  name = "refund_buyer.fifo"
  delay_seconds = 0
  max_message_size = 2048
  fifo_queue = true
  content_based_deduplication = false
  message_retention_seconds = 1209600

  redrive_policy = "{\"deadLetterTargetArn\":\"${aws_sqs_queue.dlq_fifo_queue.arn}\",\"maxReceiveCount\":5}"
}
