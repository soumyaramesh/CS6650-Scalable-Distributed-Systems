import time

from Config import *
import urllib2
import json
from Utils import *
import sys
import os

# Get
def register_publisher(topic):
    url = REGISTER_PUBLISHER_URL+"?topic=" + topic
    response = send_get_request(url)
    while 'id' not in response:
        response = send_get_request(url)
        time.sleep(1)
    print "publisher registered with id " + response["id"]
    return response["id"]

# Post
def publish_message(id, msg):
    parameters = dict()
    parameters["id"] = id
    parameters["message"] = msg
    send_post_request(parameters, PUBLISH_CONTENT_URL)
    return


# arg1: topic, arg2: number of messages to send
def main(args):
    print "In Publisher class"
    if len(args) < 2:
        print "Not enough arguments"
        os._exit(-1)
    num_of_messages = int(args[1])
    topic = args[0]
    print topic
    print num_of_messages
    id = register_publisher(topic)
    time.sleep(0.5)
    for i in range(0, num_of_messages):
        publish_message(id, "This is message " + str(i))
        time.sleep(0.3)
    return


if __name__ == "__main__":
    main(sys.argv[1:])
