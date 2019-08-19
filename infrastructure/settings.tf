terraform {
  backend "s3" {
    bucket = "retour"
    key    = "terraform.tfstate"
    region = "eu-central-1"
  }
}
