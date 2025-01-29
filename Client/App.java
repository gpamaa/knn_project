package Client;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Client.Main.Controllermain;

/**
 * è la classe con cui il client inizia l esecuzione
 */
public class App extends Application {

    /**
     * 
     * viene utilizzato per mostrare la prima pagina dell
     * interfaccia grafica
     * 
     * @param primarystage
     *                     primarystage rappresenta la finestra che viene aperta con
     *                     l avvio del programma
     * 
     */
    public void start(Stage primarystage) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("Main/mainpage.fxml"));
            Scene scene = new Scene(root);
            primarystage.setTitle("KNN Software");
            primarystage.setScene(scene);
            primarystage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args
     *             contiene comandi da cmd
     */
    public static void main(String[] args) {
        launch(args);
    }
}
