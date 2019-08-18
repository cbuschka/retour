resource "aws_dynamodb_table" "retour_table" {
  name = "Retour"
  billing_mode = "PROVISIONED"
  read_capacity = 5
  write_capacity = 5
  hash_key = "RetourNo"

  attribute {
    name = "RetourNo"
    type = "S"
  }
}