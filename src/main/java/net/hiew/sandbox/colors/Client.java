package net.hiew.sandbox.colors;

import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class Client {
  private static final Logger logger = LoggerFactory.getLogger(Client.class);

  private Client() {
  }

  public static void main(String[] args) {
    ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 8080).build();
    ColorsGrpc.ColorsBlockingStub stub = ColorsGrpc.newBlockingStub(channel);
    final var response =
        stub.getColor(ColorsOuterClass.GetColorRequest.newBuilder().setName("test").build());
    logger.info("{}", response);
  }
}
