package sandbox.colors.interceptors;

import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Interceptor which does something on every message (including possibly throwing an exception) */
public final class EachMessageServerInterceptor implements ServerInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(EachMessageServerInterceptor.class);

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
    return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(next.startCall(call, headers)) {
      @Override
      public void onMessage(ReqT message) {
        logger.info("Message received: {}", message);
        super.onMessage(message);
      }
    };
  }
}
