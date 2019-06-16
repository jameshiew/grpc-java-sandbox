package sandbox.colors.interceptors;

import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AuthenticationServerInterceptor implements ServerInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(AuthenticationServerInterceptor.class);

  private static final String EXPECTED_AUTH_TOKEN = "1234";

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
    if (!EXPECTED_AUTH_TOKEN.equals(headers.get(sandbox.Metadata.Headers.AUTH_TOKEN))) {
      call.close(Status.UNAUTHENTICATED, new Metadata());
      return noopListener();
    }
    return next.startCall(call, headers);
  }

  private static <ReqT> ServerCall.Listener<ReqT> noopListener() {
    return new ForwardingServerCallListener<ReqT>() {
      @Override
      protected ServerCall.Listener<ReqT> delegate() {
        return new ServerCall.Listener<ReqT>() {};
      }
    };
  }
}
