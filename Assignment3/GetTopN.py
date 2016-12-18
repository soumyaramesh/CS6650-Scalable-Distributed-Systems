from Config import *
import urllib2
import json
from Utils import *
import os


def get_top_n(n):
    url = GET_TOP_N_URL+"?n=" + n
    response = send_get_request(url)
    print "Got top %s:" % n
    return response["items"]

# arg1: n
def main(*args):
    print "In top N method"
    print args[0]
    if len(args) < 1:
        print "Not enough arguments"
        os._exit(-1)

    top_n_list = get_top_n(args[0])
    print top_n_list
    # for item in top_n_list:
    #     print "\t" + str(item)
    return


if __name__ == "__main__":
    main()
