import boto3
from time import gmtime, strftime
import json
import sys

sqs = boto3.resource('sqs')

queue = sqs.get_queue_by_name(QueueName='retour')

if len(sys.argv) == 2:
  suffix = sys.argv[1]
else:
  suffix = strftime("%Y%m%d%H%M%S", gmtime())
retourNo = "R"+suffix
orderNo = "O"+suffix
retourJson = json.dumps({"retourNo":retourNo,"orderNo":orderNo})
response = queue.send_message(MessageBody=retourJson)

print("Sent {} with messageId={}.".format(retourJson, response.get('MessageId')))
