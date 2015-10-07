set -e

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

./build.sh

LIBRARY_PATH="lib/native/"
CLASS_PATH="build/"$PATH_SEPARATOR$(find lib/* | grep .jar | tr '\n' $PATH_SEPARATOR)
java -Djava.library.path=$LIBRARY_PATH -cp $CLASS_PATH main.ServerMain;