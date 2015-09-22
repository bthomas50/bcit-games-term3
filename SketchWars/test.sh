set -e

./build.sh
./build-tests.sh
java -cp "build/:build/test/:lib/*" org.junit.runner.JUnitCore sketchwars.physics.PhysicsSuite