This is a Spring Boot based Application that  organizes and stores each trade in a specific sequence.
Tech Stack: Spring Boot 3.4.3, Java 21, Kafka and H2 DB (In memory DB)
There are two modules:
1) tradeproducer- This houses the functionality for producing Trade and publish on Kafka Topic. (tradetopic) .Kafka broker hosted locally on Docker desktop.[Ref: https://developer.confluent.io/confluent-tutorials/kafka-on-docker/]
2) tradeconsumer - This module houses the functionailty for consuming Trade messge  from Kafka Topic and storing Trade in a Trade Store. In memory SQL DB  H2DB which ships with Springboot. Alternatively you can use any other SQl/No Sql (MongoDB)

Architecture and Workflow

![TradeConsumer](https://github.com/user-attachments/assets/0aa49943-6071-4966-91be-8d09a3d2283a)






Key Application Components:
1) TradeProducerController-REST Controller for publishing Trade message through TradeControlelrService on Kafka Topic-trade Topic
2) TradeProducerService - Service for publishing Trade message on Kafka

3) TradeConsumerService- Subscribes to Kafka Topic and consumes message.
4) TradeStoreService - Service for performing necessary validations on incoming Tade message and store in H2 DB. This also has a scheduler (Cron Job) set to run at midnight daily to scan all Trades in the DB and mark the Trades as Expired for those whose Maturity Date is less than current date.

Key Validations performed on the incoming Trade:
1.	If a trade with a lower version is received during transmission, the store will reject it and generate an exception. Trades with the same version will replace the current record.
2.	The store must reject any trade that has a maturity date earlier than today's date.
3.	When a trade's maturity date is surpassed, the store should automatically mark the trade as expired.

Sample Trade Structure:
{
    "tradeId": "T1",
    "version": 1,
    "counterPartyId": "CP-1",
    "bookId": "B1",
    "maturityDate": "2025-05-20",
    "createdDate": "2025-03-15",
    "expired": false
}


Reference Plant UML Sequence diagram located under tradeconsumer module. tradeconsumer--->src-->TradeConsumer.puml
