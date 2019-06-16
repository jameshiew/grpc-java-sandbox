package sandbox.colors.interceptors;

import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import net.hiew.sandbox.colors.ColorsOuterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DisallowRedServerInterceptor implements ServerInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(DisallowRedServerInterceptor.class);

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
    return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(
        next.startCall(call, headers)) {
      private boolean callClosed;

      private void closeCall(Status status) {
        call.close(status, new Metadata());
        callClosed = true;
      }

      @Override
      public void onHalfClose() {
        if (callClosed) {
          return;
        }
        super.onHalfClose();
      }

      @Override
      public void onCancel() {
        if (callClosed) {
          return;
        }
        super.onCancel();
      }

      @Override
      public void onComplete() {
        if (callClosed) {
          return;
        }
        super.onComplete();
      }

      @Override
      public void onReady() {
        if (callClosed) {
          return;
        }
        super.onReady();
      }

      @Override
      public void onMessage(ReqT message) {
        if (callClosed) {
          return;
        }
        final var req = (ColorsOuterClass.GetColorRequest) message;
        if (req.getName().equals("red")) {
          logger.info("Closing call due to RED request");
          closeCall(Status.PERMISSION_DENIED);
          return;
        }
        super.onMessage(message);
      }
    };
  }
}
