import boto3
from time import gmtime, strftime
import json

sqs = boto3.resource('sqs')

queue = sqs.get_queue_by_name(QueueName='retour')

retourNo = "retour"+strftime("%Y%m%d%H%M%S", gmtime())
retourJson = json.dumps({"retourNo":retourNo})
response = queue.send_message(MessageBody=retourJson)

print("Sent {} with messageId={}.".format(retourJson, response.get('MessageId')))
