package sandbox.server.interceptors;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AuthenticationInterceptor implements ServerInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
    if (!headers.keys().contains("AUTH_TOKEN")) {
      throw new RuntimeException("No auth token supplied!");
    }
    return next.startCall(call, headers);
  }
}
