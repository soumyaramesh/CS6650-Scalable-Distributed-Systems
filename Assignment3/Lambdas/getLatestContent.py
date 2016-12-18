from __future__ import print_function

import boto3
import time
import json
from botocore.exceptions import ClientError

print('Loading function GetLatestContent')


def respond(err, res=None):
    return {
        'statusCode': '400' if err else '200',
        'body': err.message if err else json.dumps(res),
        'headers': {
            'Content-Type': 'application/json',
        },
    }


def lambda_handler(event, context):
    '''Demonstrates a simple HTTP endpoint using API Gateway. You have full
    access to the request and response payload, including headers and
    status code.

    To scan a DynamoDB table, make a GET request with the TableName as a
    query string parameter. To put, update, or delete an item, make a POST,
    PUT, or DELETE request respectively, passing in the payload to the
    DynamoDB API as a JSON body.
    '''
    #print("Received event: " + json.dumps(event, indent=2))

    operations = {
        #'DELETE': lambda dynamo, x: dynamo.delete_item(**x),
        #'GET': lambda dynamo, x: dynamo.scan(**x),
        #'POST': lambda dynamo, x: dynamo.put_item(**x),
        #'PUT': lambda dynamo, x: dynamo.update_item(**x),
        'GET',
        'POST',
    }

    operation = event['httpMethod']
    if operation in operations:
        payload = event['queryStringParameters'] if operation == 'GET' else json.loads(event['body'])
        sqs_name = payload["sqs_name"]
        res = dict()
        res["message"] = ""
        
        try:
            sqs = boto3.resource('sqs')
            sqs_client = boto3.client('sqs')
            sqs_queue = sqs.get_queue_by_name(QueueName=sqs_name)

            response = sqs_client.receive_message(
                QueueUrl=sqs_queue.url,
                MaxNumberOfMessages=1
            )
            
            #return respond(None, {'a': response})
            if "Messages" not in response:
            #if response is None or response["Messages"] is None:
                return respond(None, None)
                    
            for message in response["Messages"]:
                res["message"] =  message["Body"]
                sqs_client.delete_message(
                    QueueUrl=sqs_queue.url,
                    ReceiptHandle=message["ReceiptHandle"]
                )

        except ClientError as e:
            print(e.response['Error']['Message'])
            return respond(ValueError(('Publish message could not read id from database error "{}": ' + e.response['Error']['Message']) .format(operation)))
        

        return respond(None, res)
    else:
        return respond(ValueError('Unsupported method "{}"'.format(operation)))