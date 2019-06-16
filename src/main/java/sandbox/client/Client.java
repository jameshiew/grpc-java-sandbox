package sandbox.client;

import com.google.common.collect.ImmutableList;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.MetadataUtils;
import net.hiew.sandbox.colors.ColorsGrpc;
import net.hiew.sandbox.colors.ColorsOuterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sandbox.client.interceptors.LoggingClientInterceptor;

import java.util.HashMap;
import java.util.Map;

final class Client {
  private static final Logger logger = LoggerFactory.getLogger(Client.class);

  private Client() {}

  static void callGetColor() {
    logger.info("Building channel to gRPC service...");

    final Map<String, Object> name = new HashMap<>(1);
    name.put("service", "colors.Colors");

    final Map<String, Object> retryPolicy = new HashMap<>(5);
    retryPolicy.put("maxAttempts", 4.0);
    retryPolicy.put("initialBackoff", "1s");
    retryPolicy.put("maxBackoff", "10s");
    retryPolicy.put("backoffMultiplier", 2.0);
    retryPolicy.put("retryableStatusCodes", ImmutableList.of("UNAVAILABLE"));

    final Map<String, Object> methodConfig = new HashMap<>(2);
    methodConfig.put("name", ImmutableList.of(name));
    methodConfig.put("retryPolicy", retryPolicy);

    final Map<String, Object> serviceConfig = new HashMap<>(1);
    serviceConfig.put("methodConfig", ImmutableList.of(methodConfig));

    ManagedChannel channel =
        NettyChannelBuilder.forAddress("localhost", 8080)
            .usePlaintext()
            .enableRetry()
            .defaultServiceConfig(serviceConfig)
            .build();
    logger.info("Creating stub with service config {}", serviceConfig);
    ColorsGrpc.ColorsBlockingStub stub =
        ColorsGrpc.newBlockingStub(channel)
            .withInterceptors(getAuthClientInterceptor(), new LoggingClientInterceptor());
    logger.info("Making gRPC call...");
    try {
      final var response =
          stub.getColor(ColorsOuterClass.GetColorRequest.newBuilder().setName("test").build());
      logger.info("{}", response);
    } catch (StatusRuntimeException e) {
      logger.error("Received error response", e);
    }
    logger.info("Finished");
  }

  private static ClientInterceptor getAuthClientInterceptor() {
    final var metadata = new Metadata();
    metadata.put(sandbox.Metadata.Headers.AUTH_TOKEN, "1234");
    return MetadataUtils.newAttachHeadersInterceptor(metadata);
  }
}
