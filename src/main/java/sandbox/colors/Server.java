package sandbox.colors;

import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sandbox.colors.interceptors.AuthenticationInterceptor;
import sandbox.colors.interceptors.LoggingInterceptor;

import java.io.IOException;

final class Server {
  private static final Logger logger = LoggerFactory.getLogger(Server.class);

  private Server() {}

  static void start() {
    logger.info("Building colors...");
    final var server =
        ServerBuilder.forPort(8080)
            .intercept(new AuthenticationInterceptor())
            .intercept(new LoggingInterceptor())
            .addService(new Service())
            .build();

    logger.info("Starting colors...");
    try {
      server.start();
    } catch (IOException e) {
      logger.error("Couldn't start colors", e);
      System.exit(1);
    }
    logger.info("Server started on port {}", server.getPort());
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread() {
              @Override
              public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC colors since JVM is shutting down");
                server.shutdown();
                System.err.println("*** colors shut down");
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
