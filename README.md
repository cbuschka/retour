# Retour processing on aws

## Ingredients
* java 8
* python
* terraform
* aws lambda
* cloudwatch
* dynamodb
* sqs (standard and fifo)

## Prerequisites
* linux
* terraform
* make
* java 8
* aws auth config in ~/.aws/credentials
```
[default]
aws_access_key_id = ABCDFGHIJKLMNOPQRSTUVWXYZ
aws_secret_access_key = HGSGgui279dhwshksd6e27e2ui
```

## Deploy aws stack
```
make deploy
```

## Destroy aws stack
```
make destroy
```

## Setup python tools
```
cd tools && \
	virtualenv --python=python3.7 .py37 && \
	source .py37/bin/activate && \
	.py37/bin/pip install -r requirements.txt
```

## Send test message
```
.py37/bin/python3.7 send_sqs_message.py
```

## Loose Ends
* Sender/receiver info in metadata
* Queues per sender/receiver
* Refund/charge amounts? Messages payload?
* Sync calls?
* TTL retour records
* TTL messages
* Framework? (micronaut?, ...)
* Extract tf module for lambda with request/ack/error messages
* Naming lambda role
* Roles instead of custom policies
* Recreate tf state?
* Integration tests?
* Load tests?
* Environments?
* Order cancelled? Order already retoured?

## Author
Written 2019 by [Cornelius Buschka](https://github.com/cbuschka).

## License
[MIT](./license.txt)
