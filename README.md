# grpc-java-sandbox

A starting point for experimenting with [grpc-java](https://github.com/grpc/grpc-java).

## Prerequisites

* Java
* Gradle

# Running the server

```bash
gradle jar
java -jar build/libs/grpc-java-sandbox-1.0-SNAPSHOT.jar
```

# Running the client

Currently the easiest way is to run the `sandbox.client.Main` class' `main()` method from within IntelliJ.
