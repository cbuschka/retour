init:
	if [ ! -d ".terraform/" ]; then terraform init; fi

build:
	mvn clean install -P-with-integration-tests

plan:	init build
	terraform plan -out=plan.out

deploy:	plan
	terraform apply plan.out

destroy:	init
	terraform destroy
