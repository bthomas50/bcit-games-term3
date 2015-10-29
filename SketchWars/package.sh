./build.sh

jar cvfm SketchWars-SinglePlayer.jar ManifestSinglePlayer.txt -C build .
jar cvfm SketchWars-Server.jar ManifestServer.txt -C build .
jar cvfm SketchWars-Client.jar ManifestClient.txt -C build .