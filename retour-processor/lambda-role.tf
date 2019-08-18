resource "aws_iam_role" "lambda_role" {
  name = "lambda_role"
  assume_role_policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
      {
        "Action": "sts:AssumeRole",
        "Principal": {
          "Service": "lambda.amazonaws.com"
        },
        "Effect": "Allow",
        "Sid": ""
      }
    ]
}
EOF
}

resource "aws_iam_policy" "lambda_policy" {
  name = "lambda-policy"
  path = "/"

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
      {
        "Sid": "AllowRetourProcessorToLog",
        "Effect": "Allow",
        "Action": [
          "cloudwatch:PutMetricData",
          "logs:CreateLogStream", "logs:PutLogEvents"
        ],
        "Resource": "*"
      },
      {
        "Sid": "AllowRetourProcessorToReceiveMessagesFromRetour",
        "Effect": "Allow",
        "Action": [ "sqs:GetQueueUrl", "sqs:ReceiveMessage", "sqs:DeleteMessage", "sqs:GetQueueAttributes" ],
        "Resource": "${aws_sqs_queue.retour_queue.arn}"
      },
      {
        "Sid": "AllowRetourProcessorToSendErrorMessages",
        "Effect": "Allow",
        "Action": [ "sqs:GetQueueUrl", "sqs:SendMessage" ],
        "Resource": "${aws_sqs_queue.retour_err_queue.arn}"
      },
      {
        "Sid": "AllowRetourProcessorToSendAckMessages",
        "Effect": "Allow",
        "Action": [ "sqs:GetQueueUrl", "sqs:SendMessage" ],
        "Resource": "${aws_sqs_queue.retour_ack_fifo_queue.arn}"
      },
      {
        "Sid": "AllowRetourProcessorToAccessMessagesToChargeSeller",
        "Effect": "Allow",
        "Action": [ "sqs:GetQueueUrl", "sqs:SendMessage" ],
        "Resource": "${aws_sqs_queue.charge_seller_fifo_queue.arn}"
      },
      {
        "Sid": "AllowRetourProcessorToAccessMessagesToRefundBuyer",
        "Effect": "Allow",
        "Action": [ "sqs:GetQueueUrl", "sqs:SendMessage" ],
        "Resource": "${aws_sqs_queue.refund_buyer_fifo_queue.arn}"
      },
      {
        "Sid": "AllowRetourProcessorToAccessRetourTable",
        "Effect": "Allow",
        "Action": [ "dynamodb:GetItem", "dynamodb:PutItem", "dynamodb:UpdateItem" ],
        "Resource": "${aws_dynamodb_table.retour_table.arn}"
      }
    ]
  }
EOF
}

resource "aws_iam_role_policy_attachment" "lambda_policy_on_lambda_role" {
  role = "${aws_iam_role.lambda_role.name}"
  policy_arn = "${aws_iam_policy.lambda_policy.arn}"
}

resource "aws_cloudwatch_log_group" "lambda_retour_processor_log_group" {
  name = "/aws/lambda/retour_processor"
  retention_in_days = 3
}

data "aws_iam_policy_document" "cloudwatch_lambda_retour_processor_log_group_access_document" {
  statement {
    actions = [
      "logs:CreateLogGroup",
      "logs:CreateLogStream",
      "logs:PutLogEvents"
    ]
    resources = [
      "arn:aws:logs:::*",
    ]
  }
}
