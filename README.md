# grpc-java-sandbox [![pipeline status](https://gitlab.com/jameshiew/grpc-java-sandbox/badges/master/pipeline.svg)](https://gitlab.com/jameshiew/grpc-java-sandbox/commits/master)

A starting point for experimenting with [grpc-java](https://github.com/grpc/grpc-java).

## Prerequisites

* Java 11+
* Gradle

# Running the server

```bash
gradle jar
java -jar build/libs/grpc-java-sandbox-1.0-SNAPSHOT.jar
```

# Running the client

Currently the easiest way is to run the `sandbox.client.Main` class' `main()` method from within IntelliJ.
