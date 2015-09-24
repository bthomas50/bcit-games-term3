SOURCE_DIR="src"
BUILD_DIR="build"
case "$OS" in
  MAC)
	PATH_SEPARATOR=':'
  ;;
  WINDOWS)
	PATH_SEPARATOR=';'
  ;;
  LINUX)
	PATH_SEPARATOR=':'
  ;;
esac
echo "PATH_SEPARATOR = $PATH_SEPARATOR"
set -e

echo "java compiler version: "
javac -version
echo "now compiling..."
mkdir -p $BUILD_DIR
javac -d $BUILD_DIR -Xlint:unchecked -cp $(find lib/* | grep .jar | tr '\n' $PATH_SEPARATOR) $(find $SOURCE_DIR/* | grep .java)
echo "done!"