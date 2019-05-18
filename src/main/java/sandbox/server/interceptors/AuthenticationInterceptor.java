package sandbox.server.interceptors;

import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AuthenticationInterceptor implements ServerInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

  private static final String EXPECTED_AUTH_TOKEN = "1234";
  private static final Metadata.Key<String> AUTH_TOKEN_HEADER =
      Metadata.Key.of("AUTH_TOKEN", Metadata.ASCII_STRING_MARSHALLER);

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
    if (!EXPECTED_AUTH_TOKEN.equals(headers.get(AUTH_TOKEN_HEADER))) {
      return close(call, Status.UNAUTHENTICATED);
    }
    return next.startCall(call, headers);
  }

  private static <ReqT, RespT> ServerCall.Listener<ReqT> close(
      ServerCall<ReqT, RespT> call, Status status) {
    call.close(status, new Metadata());
    return new ForwardingServerCallListener<ReqT>() {
      @Override
      protected ServerCall.Listener<ReqT> delegate() {
        return new ServerCall.Listener<ReqT>() {
          @Override
          public void onMessage(ReqT message) {
            return;
          }
        };
      }
    };
  }
}
