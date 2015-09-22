SOURCE_DIR="test"
BUILD_DIR="build/test/"

set -e

echo "java compiler version: "
javac -version
echo "now compiling tests..."
mkdir -p $BUILD_DIR
javac -d $BUILD_DIR -cp "build/:lib/*" $(find $SOURCE_DIR/* | grep .java)
echo "done!"