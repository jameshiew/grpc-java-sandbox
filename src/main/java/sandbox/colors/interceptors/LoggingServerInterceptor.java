package sandbox.colors.interceptors;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LoggingServerInterceptor implements ServerInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(LoggingServerInterceptor.class);

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
    logger.info(
        "Received call to {} with headers {}",
        call.getMethodDescriptor().getFullMethodName(),
        headers);
    return next.startCall(call, headers);
  }
}
