from threading import Thread
import Subscriber
import timeit


def handle_script_Subscriber(args):
    print("Calling functions from Subscriber")
    Subscriber.main(args)

if __name__ == '__main__':
    threadList = []
    num_of_sub = 10
    start = timeit.default_timer()
    for i in range(1,num_of_sub + 1):
        topic = "topic" + str(i)
        a = (topic)
        t = Thread(target=handle_script_Subscriber, args=(a,))
        t.start()
        threadList.append(t)
    for th in threadList:
        th.join()
    stop = timeit.default_timer()
    print "Subscriber Main Thread Complete"
    print "Time taken by subscriber client is "
    print stop - start