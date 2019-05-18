package sandbox;

import io.grpc.stub.StreamObserver;
import net.hiew.sandbox.colors.ColorsGrpc;
import net.hiew.sandbox.colors.ColorsOuterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Service extends ColorsGrpc.ColorsImplBase {
  private static final Logger logger = LoggerFactory.getLogger(Service.class);

  @Override
  public void getColor(
      ColorsOuterClass.GetColorRequest request,
      StreamObserver<ColorsOuterClass.Color> responseObserver) {
    logger.debug("Received request");
    responseObserver.onNext(
        ColorsOuterClass.Color.newBuilder().setRed(100).setBlue(100).setGreen(100).build());
    responseObserver.onCompleted();
  }
}
