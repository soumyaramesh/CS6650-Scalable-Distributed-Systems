BSDS Assignment 1 

Run Instructions -

When the code is run with no arguments, the default number of publisher threads are 20, subscribers are 10 and TTL is 90
seconds.

CAServer
CAPubClient <numberOfMessages> <publisherThreads> <host>
CASubClient <subscriberThreads> <host>

Remote interfaces are:
  BSDSPublishInterface
  BSDSSubscribeInterface

A skeleton RMI server supporting two remote interfaces:
  CAServer.java

Two test clients, one for each interface:
  CAPubClient
  CASubClient
  
To test:
  1 Run CAServer
  2 Run either/both clients, they should connect to ther server amd call each remote method successfully.
  
Also there are:
  BSDSContent
  BSDSMessage
  
