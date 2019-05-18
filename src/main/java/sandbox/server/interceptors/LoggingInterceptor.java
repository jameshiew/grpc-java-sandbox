package sandbox.server.interceptors;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LoggingInterceptor implements ServerInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
    logger.info("Received call to {}", call.getMethodDescriptor().getFullMethodName());
    return next.startCall(call, headers);
  }
}
