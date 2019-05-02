package net.hiew.sandbox.grpc;

import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

final class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  private Main() {}

  public static void main(String... args) {
    final var server =
        ServerBuilder.forPort(8080).addService(new net.hiew.sandbox.colors.Service()).build();

    try {
      server.start();
    } catch (IOException e) {
      logger.error("Couldn't start server", e);
      System.exit(1);
    }

    try {
      server.awaitTermination();
    } catch (InterruptedException e) {
      logger.error("Interrupted while awaiting termination, will shut down now", e);
      System.exit(1);
    }
  }
}
