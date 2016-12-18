from __future__ import print_function

import boto3
import json
from botocore.exceptions import ClientError

print('Loading function')


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
        'DELETE': lambda dynamo, x: dynamo.delete_item(**x),
        'GET': lambda dynamo, x: dynamo.scan(**x),
        'POST': lambda dynamo, x: dynamo.put_item(**x),
        'PUT': lambda dynamo, x: dynamo.update_item(**x),
    }

    operation = event['httpMethod']
    if operation in operations:
        payload = event['queryStringParameters'] if operation == 'GET' else json.loads(event['body'])
        id = payload["id"]
        message = payload["message"]
        dynamo_publisher = boto3.resource('dynamodb').Table("Publisher")
        try:
            # Get the next available ID
            res = dict()
            publisher_item = dynamo_publisher.get_item(
                Key={
                    'id': id
                }
            )
            
            if "Item" not in publisher_item:
                return respond(None, res)
            publisher_item = publisher_item["Item"]
            topic_string = publisher_item["topic"]


            sns = boto3.resource('sns')
            topic = sns.create_topic(
                Name = topic_string
            )

            response = topic.publish(
                Message=message
            )
            
            res["MessageId"] = response.get('MessageId')

        except ClientError as e:
            print(e.response['Error']['Message'])
            res["Error"] = e.response['Error']['Message']
            return respond(None, res)
            #return respond(ValueError(('Publish message could not read id from database error "{}"' + e.response['Error']['Message']).format(operation)))
        return respond(None, res)
    else:
        return respond(ValueError('Unsupported method "{}"'.format(operation)))