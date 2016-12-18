import boto3
import json
from botocore.exceptions import ClientError
import logging
    
    
def lambda_handler(event, context):
    
    #boto3.set_stream_logger('boto3.resources', logging.INFO)
    message = event['Records'][0]['Sns']['Message']

    dynamo_wordcount = boto3.resource('dynamodb').Table("WordCount")

    word_count_entity = dict()

    words = message.split(" ")
    
    words_map = dict()
    
    for word in words:
        if word in words_map:
            words_map[word] += 1
        else:
            words_map[word] = 1

    for word in words_map:
        #lookup word in db
        word_res = dynamo_wordcount.get_item(
                Key={
                    'word': word
                }
            )

        if "Item" not in word_res:
            #create new word item
            word_count_entity["word"] = word
            word_count_entity["word_count"] = words_map[word]
            dynamo_wordcount.put_item(Item = word_count_entity)

        else:
            dynamo_wordcount.update_item(
                Key={
                    'word': word
                },
                UpdateExpression="set word_count = word_count + :r",
                ExpressionAttributeValues={
                    ':r': words_map[word]
                },
                ReturnValues="UPDATED_NEW"
            )
    return "Updated word count"