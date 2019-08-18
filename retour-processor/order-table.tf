resource "aws_dynamodb_table" "order_table" {
  name = "Order"
  billing_mode = "PROVISIONED"
  read_capacity = 5
  write_capacity = 5
  hash_key = "OrderNo"

  attribute {
    name = "OrderNo"
    type = "S"
  }
}
