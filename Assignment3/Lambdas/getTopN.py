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

    #return respond(None, event)
    operations = {
        #'DELETE': lambda dynamo, x: dynamo.delete_item(**x),
        'GET',#: lambda dynamo, x: dynamo.scan(**x),
        'POST'#: lambda dynamo, x: dynamo.put_item(**x),
        #'PUT': lambda dynamo, x: dynamo.update_item(**x),
    }

    operation = event['httpMethod']
    if operation in operations:
        try:
            payload = event['queryStringParameters'] if operation == 'GET' else json.loads(event['body'])
            n = payload["n"]
            
            dynamo_wordcount = boto3.resource('dynamodb').Table("WordCount")
            
            response = dynamo_wordcount.scan()
            items = response["Items"]
            sort_list = list()
            
            
            for i, item in enumerate(items):
                entry = dict()
                entry["word"] = item["word"]
                entry["count"] = int(item["word_count"])
                sort_list.append(entry)
            
            sort_list = sorted(sort_list, key=lambda item: item["count"], reverse=True)
            

            res = dict()
            res["items"] = sort_list[:int(n)]
            
            return respond(None, res)
            
        
        except ClientError as e:
            print(e.response['Error']['Message'])
            return respond(ValueError(('Publish message could not read id from database error "{}": ' + e.response['Error']['Message']) .format(operation)))        

        return respond(None, res)
    else:
        return respond(ValueError('Unsupported method "{}"'.format(operation)))
