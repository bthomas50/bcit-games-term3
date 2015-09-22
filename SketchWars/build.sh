SOURCE_DIR="src"
BUILD_DIR="build"

set -e

echo "java compiler version: "
javac -version
echo "now compiling..."
mkdir -p $BUILD_DIR
javac -d $BUILD_DIR -Xlint:unchecked -cp "lib/*" $(find $SOURCE_DIR/* | grep .java)
echo "done!"