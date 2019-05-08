package net.hiew.sandbox.colors;

import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class Client {
  private static final Logger logger = LoggerFactory.getLogger(Client.class);

  private Client() {}

  public static void main(String[] args) {
    logger.info("Building channel to gRPC service...");
    ManagedChannel channel =
        NettyChannelBuilder.forAddress("localhost", 8080).usePlaintext().build();
    logger.info("Creating stub...");
    ColorsGrpc.ColorsBlockingStub stub = ColorsGrpc.newBlockingStub(channel);
    logger.info("Making gRPC call...");
    final var response =
        stub.getColor(ColorsOuterClass.GetColorRequest.newBuilder().setName("test").build());
    logger.info("{}", response);
  }
}
