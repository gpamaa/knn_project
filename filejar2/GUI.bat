@ECHO OFF
start java -jar src\GUI\server.jar
@ECHO OFF
start javaw  --module-path "lib\javafx-sdk-19\lib" --add-modules javafx.controls,javafx.fxml -jar src\GUI\Client.jar
