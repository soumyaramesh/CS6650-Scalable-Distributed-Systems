from Config import *
import urllib2
import json
from Utils import *
import time
import sys
import os


def register_subscriber(topic):
    url = REGISTER_SUBSCRIBER_URL+"?topic=" + topic
    response = send_get_request(url)
    while 'message' in response and response["message"] == "Internal server error":
        response = send_get_request(url)
        time.sleep(0.5)
    #print response
    #print "subscriber registered with name " + response["queue_name"]
    return response["queue_name"]


def get_latest_content(sqs_name):
    parameters = dict()
    parameters["sqs_name"] = sqs_name
    response = send_post_request(parameters, GET_LATEST_CONTENT_URL)
    if response is not None:
        return response["message"]
    else:
        print "Response is None"
        return None


# arg1: topic
def main(*args):
    print "In subscriber class"
    print args[0]
    if len(args) < 1:
        print "Not enough arguments"
        os._exit(-1)

    print args[0]
    sqs_name = register_subscriber(args[0])
    timeout = 1
    count = 0
    while timeout < 10:
        msg = get_latest_content(sqs_name)
        if msg is None:
            time.sleep(timeout)
            timeout = timeout * 2
        else:
            #count = count + 1
            print "received msg = " + msg
            timeout = 1
        time.sleep(0.2)
    print "timeout reached. Total messages = " + str(count)
    return


if __name__ == "__main__":
    main()
