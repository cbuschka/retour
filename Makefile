init:
	cd infrastructure && if [ ! -d ".terraform/" ]; then terraform init; fi

build:
	mvn clean install -P-with-integration-tests

plan:	init build
	cd infrastructure/ && terraform plan -out=plan.out

deploy:	plan
	cd infrastructure/ && terraform apply plan.out

destroy:	init
	cd infrastructure/ && terraform destroy
