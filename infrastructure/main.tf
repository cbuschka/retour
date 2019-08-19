provider "aws" {
  region = "${var.aws_region}"
}

module "retour_processor" {
  source = "./retour-processor/"
}
