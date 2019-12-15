# AccountsTransfer
A basic accounts money transfer web service

## Running the tests

```

$ mvn clean test

```

## A web service usage example

Sending 100 coins from the account 10001 to the account 10002 using HTTP POST:

```

$ curl -d "from=10001&to=10002&amount=100" http://localhost:8080/transfers

```
When succeed, the transaction number with HTTP code 200 to be returned.
In case of a business logic problem, e.g. not enough money on the source account, HTTP code 409 and an error message to be returned.


## A Java client API code example

Sending 100 coins from the account 10001 to the account 10002: 

```java

final TransferService ts = new HttpClientTransferService(new InetSocketAddress("localhost", 8080));
final Try<Integer> result = ts.transfer(new AccountNumber(10001), new AccountNumber(10002), new Value(100)).run();
// In case of success the result will contain the transaction's number. 
// In case of a business logic failure the result will contain a TransferException object.

```