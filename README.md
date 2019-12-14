# AccountsTransfer
A basic accounts money transfer web service
## 

## Running the tests, including end-to-end tests

```

$ mvn test

```

## Web service usage example for a client

Sending 100 coins from the account 1001 to the account 1002:

```

$ curl -d "from=10001&to=10002&amount=100" http://localhost:8080/transfers

```

## Java client API usage example

Sending 100 coins from the account 1001 to the account 1002: 

```java

final TransferService ts = new HttpClientTransferService(new InetSocketAddress("localhost", 8080));
final Try<Integer> result = ts.transfer(new AccountNumber(1001), new AccountNumber(1002), new Value(10)).run();
// in case of success of transfer the result will contain the transaction's number

```