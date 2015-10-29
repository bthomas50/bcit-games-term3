SOURCE_DIR="src"
BUILD_DIR="build"
case "$(uname -s)" in

   Darwin)
     echo 'Mac OS X'
     export OS=MAC
     PATH_SEPARATOR=':'
     ;;

   Linux)
     echo 'Linux'
     export OS=LINUX
     PATH_SEPARATOR=':'
     ;;

   CYGWIN*|MINGW32*|MSYS*|MINGW64*)
     echo 'MS Windows'
     export OS=WINDOWS
     PATH_SEPARATOR=';'
     ;;

   *)
     echo 'other OS (or missing cases for above OSs)' 
     ;;
esac
echo "PATH_SEPARATOR = $PATH_SEPARATOR"
set -e

echo "java compiler version: "
javac -version
echo "now compiling..."
mkdir -p $BUILD_DIR
javac -d $BUILD_DIR -Xlint:unchecked -Xlint:deprecation -cp $(find lib/* | grep .jar | tr '\n' $PATH_SEPARATOR) $(find $SOURCE_DIR/* | grep .java)
echo "done!"