import time

import Publisher
from threading import Thread
import timeit

def handle_script_Publisher(args):
    print("Calling functions from Publisher")
    Publisher.main(args)

if __name__ == '__main__':
    num_of_topics = 10
    num_of_messages = 1000
    threadList = []
    start = timeit.default_timer()
    for j in range(1,num_of_topics+1):
        for i in range(1,101):
            topic = "topic" + str(j)
            a = []
            a.append(topic)
            a.append(str(num_of_messages))
            t = Thread(target=handle_script_Publisher, args=(a,))
            t.start()
            threadList.append(t)
    for th in threadList:
        th.join()
    stop = timeit.default_timer()
    print "Publisher Main Thread Complete"
    print "Time taken by Publisher client is "
    print stop - start
