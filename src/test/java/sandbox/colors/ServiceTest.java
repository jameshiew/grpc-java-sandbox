package sandbox.colors;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import net.hiew.sandbox.colors.ColorsGrpc;
import net.hiew.sandbox.colors.ColorsOuterClass;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@EnableRuleMigrationSupport
class ServiceTest {
  @Rule public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

  private final String serverName = InProcessServerBuilder.generateName();
  private final InProcessServerBuilder serverBuilder =
      InProcessServerBuilder.forName(serverName).directExecutor();
  private final InProcessChannelBuilder channelBuilder =
      InProcessChannelBuilder.forName(serverName).directExecutor();

  @Test
  void testGetColorInitiallyUnavailable() throws IOException {
    grpcCleanup.register(serverBuilder.addService(new Service()).build().start());
    ColorsGrpc.ColorsBlockingStub stub =
        ColorsGrpc.newBlockingStub(
            grpcCleanup.register(channelBuilder.maxInboundMessageSize(1024).build()));

    for (var i = 0; i < 2; i++) {
      final var status =
          assertThrows(
                  StatusRuntimeException.class,
                  () -> stub.getColor(ColorsOuterClass.GetColorRequest.newBuilder().build()))
              .getStatus();
      assertEquals(Status.UNAVAILABLE, status, "initially unavailable");
    }
  }

  @Test
  void testGetColorFinallySucceeds() throws IOException {
    grpcCleanup.register(serverBuilder.addService(new Service()).build().start());
    ColorsGrpc.ColorsBlockingStub stub =
        ColorsGrpc.newBlockingStub(
            grpcCleanup.register(channelBuilder.maxInboundMessageSize(1024).build()));
    for (var i = 0; i < 2; i++) {
      try {
        stub.getColor(ColorsOuterClass.GetColorRequest.newBuilder().build());
      } catch (StatusRuntimeException e) {

      }
    }

    final var response = stub.getColor(ColorsOuterClass.GetColorRequest.newBuilder().build());

    assertEquals(
        ColorsOuterClass.Color.newBuilder().setRed(100).setBlue(100).setGreen(100).build(),
        response,
        "finally succeeds");
  }
}
