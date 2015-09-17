SOURCE_DIR="src/"
BUILD_DIR="../build/"

set -e

cd $SOURCE_DIR
echo "java compiler version: "
javac -version
echo "now compiling..."
mkdir -p $BUILD_DIR
javac -d $BUILD_DIR "Physics/*.java"
echo "done!"