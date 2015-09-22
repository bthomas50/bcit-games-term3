set -e

./build.sh
java -Djava.library.path="lib/native/" -cp "build/:lib/*" sketchwars.SketchWars