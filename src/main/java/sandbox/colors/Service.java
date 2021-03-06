package sandbox.colors;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.hiew.sandbox.colors.ColorsGrpc;
import net.hiew.sandbox.colors.ColorsOuterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Service extends ColorsGrpc.ColorsImplBase {
  private static final Logger logger = LoggerFactory.getLogger(Service.class);

  private int numberOfCallsReceived;

  @Override
  public void getColor(
      ColorsOuterClass.GetColorRequest request,
      StreamObserver<ColorsOuterClass.Color> responseObserver) {
    if (numberOfCallsReceived < 2) {
      logger.debug("Responding with UNAVAILABLE");
      numberOfCallsReceived++;
      responseObserver.onError(Status.UNAVAILABLE.asException());
      return;
    }
    logger.debug("Responding successfully");
    responseObserver.onNext(
        ColorsOuterClass.Color.newBuilder().setRed(100).setBlue(100).setGreen(100).build());
    responseObserver.onCompleted();
  }
}
