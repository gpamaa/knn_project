package Client.Main;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import Client.Server.*;

/**
 * la classe controller corrispondente alla pagina fxml mainpage
 */
public class Controllermain {
    @FXML
    void connessione(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Server/server.fxml"));
        Stage stage;

        Scene scene;

        Parent root;
        try {
            root = loader.load();
            Controllerserver controllerserver = loader.getController();

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            scene = new Scene(root);

            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void visualizzaGuida(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("guida.fxml"));
            Stage stage;

            Scene scene;

            Parent root;
            root = loader.load();

            Controllermain controllerguida = loader.getController();

            root = FXMLLoader.load(getClass().getResource("guida.fxml"));

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            scene = new Scene(root);

            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
        }
    }

    @FXML
    void indietro(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainpage.fxml"));
        Stage stage;

        Scene scene;

        Parent root;
        try {
            root = loader.load();

            Controllermain controllerguida = loader.getController();
            root = FXMLLoader.load(getClass().getResource("mainpage.fxml"));

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            scene = new Scene(root);

            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
