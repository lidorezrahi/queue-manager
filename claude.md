This is my architecture, UI layer -> ViewModel for BL -> Repository as a Pipline for data sources
and API / Data Sources... My application is offline first meaning some of the APIs that I want to
make should be saved in a     
queue and try to make the requests until they succeed. For that I need you to create with me
QueueManager that will handle this queue and requests until there is a network to succeed those
requests and than delete them from   
the queue