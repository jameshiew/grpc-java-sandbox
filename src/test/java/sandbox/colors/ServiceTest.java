package sandbox.colors;

import io.grpc.BindableService;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import net.hiew.sandbox.colors.ColorsGrpc;
import net.hiew.sandbox.colors.ColorsOuterClass;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

public class ServiceTest {
  @Rule public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

  private final String serverName = InProcessServerBuilder.generateName();
  private final InProcessServerBuilder serverBuilder =
      InProcessServerBuilder.forName(serverName).directExecutor();
  private final InProcessChannelBuilder channelBuilder =
      InProcessChannelBuilder.forName(serverName).directExecutor();

  @Test
  public void testGetColor() throws IOException {
    final BindableService service = new Service();
    grpcCleanup.register(serverBuilder.addService(service).build().start());
    final var channel = grpcCleanup.register(channelBuilder.maxInboundMessageSize(1024).build());
    ColorsGrpc.ColorsBlockingStub stub = ColorsGrpc.newBlockingStub(channel);
    try {
      stub.getColor(ColorsOuterClass.GetColorRequest.newBuilder().build());
    } catch (StatusRuntimeException e) {

    }
  }
}
