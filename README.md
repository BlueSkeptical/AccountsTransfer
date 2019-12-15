# AccountsTransfer
A basic accounts money transfer web service

## Running the tests

```
$ mvn clean test

```

## A web service usage example

Sending 100 coins from the account 1001 to the account 1002:

```

$ curl -d "from=10001&to=10002&amount=100" http://localhost:8080/transfers

```

## And a Java client API code example

Sending 100 coins from the account 1001 to the account 1002: 

```java

final TransferService ts = new HttpClientTransferService(new InetSocketAddress("localhost", 8080));
final Try<Integer> result = ts.transfer(new AccountNumber(1001), new AccountNumber(1002), new Value(100)).run();
// in case of success of transfer the result will contain the transaction's number

```