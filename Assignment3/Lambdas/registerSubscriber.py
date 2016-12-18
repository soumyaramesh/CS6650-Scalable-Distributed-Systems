from __future__ import print_function

import boto3
import json
from botocore.exceptions import ClientError
import uuid

print('Loading function regSubscriber')


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

        dynamo_subscriber = boto3.resource('dynamodb').Table("Subscriber")
        #dynamo_topic_topicarn = boto3.resource('dynamodb').Table("TopicToArn")

        topic_string = payload["topic"]
        new_sub_id = str(uuid.uuid4())
        subscriber = dict()
        subscriber["id"] = new_sub_id
        subscriber["topic"] = topic_string
        
        #iam = boto3.resource('iam')
        #policy = iam.Policy('arn:aws:iam::aws:policy/AmazonSQSFullAccess')
        
        try:
            sns = boto3.resource('sns')
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
                    #subscriber["id"] = "Already Exist"
                    #return respond(None, subscriber)
            if not policy_exists:
                #subscriber["id"] = "Does not Exist"
                #return respond(None, subscriber)
                lambda_client.add_permission(
                    FunctionName='WordCount',
                    StatementId=new_sub_id[:6]+"_sid",
                    Action='lambda:InvokeFunction',
                    Principal='sns.amazonaws.com',
                    SourceArn=topic.arn
                    # SourceAccount='string',
                    # EventSourceToken='string',
                    # Qualifier='string'
                )
                
            #Setup SQS for this subscriber
            #sqs = boto3.resource('sqs')
            sqs_client = boto3.client('sqs')
            queue_name = new_sub_id + '_' + topic_string + '_Queue'
            queue = sqs_client.create_queue(
                QueueName = queue_name
            )
            
            sqs_attributes = sqs_client.get_queue_attributes(
                QueueUrl=queue["QueueUrl"],
                AttributeNames=[
                    'QueueArn',
                ]
            )
            
            #policy_string = '{ "Version": "2012-10-17",  "Statement": [{ "Action": ["sqs:SendMessage"], "Effect": "Allow", "Principal": {"AWS":"*"}, "Resource": \"%s\" }] }' % sqs_attributes["Attributes"]["QueueArn"]
            #policy_str2 = '{ "Version": "2012-10-17", "Id": "SQSDefaultPolicy", "Statement": [{"Sid": "1","Effect": "Allow", "Principal": {"AWS":"*"}, "Action": "SQS:SendMessage", "Resource":sqs_attributes["Attributes"]["QueueArn"],"Condition": {"ArnEquals": {"aws:SourceArn:topic.arn}}}]}]
            
            sqs_client.set_queue_attributes(
                QueueUrl = queue["QueueUrl"],
                Attributes={
                    'Policy': create_policy(sqs_attributes["Attributes"]["QueueArn"],topic.arn)
                }
            )


            subscription = topic.subscribe(
                Protocol = 'sqs',
                Endpoint = sqs_attributes["Attributes"]["QueueArn"]
            )
            
            # Here we just need to return the ARN
            subscriber["queue_name"] = queue_name
            
            dynamo_subscriber.put_item(Item = subscriber)
        except ClientError as e:
            return respond(None, subscriber)
            print(e.response['Error']['Message'])
            return respond(ValueError(('Databse operation error "{}": '+e.response['Error']['Message']).format(operation)))
            
        return respond(None, subscriber)
    else:
        return respond(ValueError('Unsupported method "{}"'.format(operation)))
        
def create_policy(queue_arn, topic_arn):
    return json.dumps({
        "Version": "2012-10-17",
        "Id": "SQSDefaultPolicy", 
        "Statement": [{
            "Sid": "1",
            "Effect": "Allow",
            "Principal": {"AWS":"*"},
            "Action": "SQS:SendMessage",
            #"Action": "*",
            "Resource":queue_arn,
            "Condition": {
                "ArnEquals": {
                    "aws:SourceArn":topic_arn
                    }
                    }
                    }]
                    })
