package Physics;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  BoundingBoxTest.class,
  VectorsTest.class,
  QuadTreeTest.class
})

public class PhysicsSuite {
  // the class remains empty,
  // used only as a holder for the above annotations
}