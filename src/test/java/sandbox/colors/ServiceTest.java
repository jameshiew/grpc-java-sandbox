package sandbox.colors;

import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableRuleMigrationSupport
class ServiceTest {
  @Rule
  public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

  @Test
  void testOnePlusOne() {
    assertEquals(2, 1 + 1, "test");
  }
}
