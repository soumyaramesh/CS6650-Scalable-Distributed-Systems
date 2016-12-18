from threading import Thread
import random
import GetTopN
import timeit

def handle_script_TopN(args):
    print args
    print("Calling functions from TopN")
    GetTopN.main(args)

if __name__ == '__main__':
    threadList = []
    start = timeit.default_timer()

    for i in range(1,11):
        n = str(random.randint(1,10))
        a = (n)
        t = Thread(target=handle_script_TopN, args=(a,))
        t.start()
        threadList.append(t)
    for th in threadList:
        th.join()
    stop = timeit.default_timer()
    print "GetTopN Main Thread Complete"
    print "Time taken by GetTopN client is "
    print stop - start