from __future__ import print_function

import boto3
import json
from botocore.exceptions import ClientError
import uuid

print('Loading function regPublisher')
wordcount_lambda_arn = 'arn:aws:lambda:us-west-2:121282886211:function:WordCount'


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

        dynamo_publisher = boto3.resource('dynamodb').Table("Publisher")
        publisher = dict()

        topic_string = payload["topic"]
        new_pub_id = str(uuid.uuid4())

        
        try:
            sns = boto3.resource('sns')
            sns_client = boto3.client('sns')
            topic = sns.create_topic(
                Name = topic_string
            )
            
            # client = boto3.client('lambda') 
            # policy = client.get_policy(FunctionName="WordCount")['Policy']
            # statements = json.loads(policy)['Statement'] 
            # sid_list = [item['Sid'] for item in statements][:-1]
            # for sid in sid_list:       
            #     print("Removing policy SID {}".format(sid))
            #     client.remove_permission(FunctionName="WordCount", StatementId=sid)
            # print(client.get_policy(FunctionName="WordCount"))
            
            # return respond(None, {'a': 'aaa'})    
            
            lambda_client = boto3.client('lambda')
            policy = lambda_client.get_policy(FunctionName='WordCount')['Policy']
            policy = json.loads(policy)
            policy_exists = False
            #return respond(None, policy['Statement'])
            for stmt in policy['Statement']:
                if 'Condition' in stmt and 'ArnLike' in stmt['Condition'] and "AWS:SourceArn" in stmt['Condition']['ArnLike'] and stmt['Condition']['ArnLike']['AWS:SourceArn'] == topic.arn:
                    policy_exists = True
                    #publisher["id"] = "Already Exist"
                    #return respond(None, publisher)
            if not policy_exists:
                #publisher["id"] = "Does not Exist"
                #return respond(None, publisher)
                lambda_client.add_permission(
                    FunctionName='WordCount',
                    StatementId=new_pub_id[:6]+"_sid",
                    Action='lambda:InvokeFunction',
                    Principal='sns.amazonaws.com',
                    SourceArn=topic.arn
                    # SourceAccount='string',
                    # EventSourceToken='string',
                    # Qualifier='string'
                )

            
            # subscription = topic.subscribe(
            #     Protocol = 'sqs',
            #     Endpoint = wordcount_queue_arn
            # )

            # response = topic.subscribe(
            #     Protocol='lambda',
            #     Endpoint=wordcount_lambda_arn
            # )
            
            response = sns_client.subscribe(
                TopicArn=topic.arn,
                Protocol='lambda',
                Endpoint=wordcount_lambda_arn
            )

            # Insert Item to Publisher
            publisher["id"] = new_pub_id
            publisher["topic"] = topic_string

            dynamo_publisher.put_item(Item = publisher)
        except ClientError as e:
            print(e.response['Error']['Message'])
            return respond(ValueError(('Databse operation error while registering publisher"{}": ' + e.response['Error']['Message']).format(operation)))
            
        return respond(None, publisher)
    else:
        return respond(ValueError('Unsupported method "{}"'.format(operation)))