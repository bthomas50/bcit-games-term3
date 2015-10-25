package sketchwars.physics;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  BoundingBoxTest.class,
  VectorsTest.class,
  QuadTreeTest.class,
  BitMaskTest.class,
  BitMaskFactoryTest.class,
  BasicPhysicsObjectTest.class,
  PixelColliderTest.class,
  CollisionsTest.class,
  CollisionBehaviourTest.class,
  PhysicsTest.class
})

public class PhysicsSuite {
  // the class remains empty,
  // used only as a holder for the above annotations
}