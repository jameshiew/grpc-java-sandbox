package sandbox;

import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sandbox.interceptors.LoggingInterceptor;

import java.io.IOException;

final class Server {
  private static final Logger logger = LoggerFactory.getLogger(Server.class);

  private Server() {}

  public static void main(String... args) {
    logger.info("Building server...");
    final var server =
        ServerBuilder.forPort(8080)
            .intercept(new LoggingInterceptor())
            .addService(new Service())
            .build();

    logger.info("Starting server...");
    try {
      server.start();
    } catch (IOException e) {
      logger.error("Couldn't start server", e);
      System.exit(1);
    }
    logger.info("Server started on port {}", server.getPort());
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread() {
              @Override
              public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                server.shutdown();
                System.err.println("*** server shut down");
              }
            });

    try {
      server.awaitTermination();
    } catch (InterruptedException e) {
      logger.error("Interrupted while awaiting termination, will shut down now", e);
      System.exit(1);
    }
    logger.info("Server terminated");
  }
}
