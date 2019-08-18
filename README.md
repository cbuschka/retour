# Retour processing on aws

## Prerequisites
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
	pip install -r requirements.txt
```

## Send test message
```
.py37/bin/python3.7 send_sqs_message.py
```
