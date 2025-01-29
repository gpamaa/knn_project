package Client.Login;

import java.io.IOException;

import Client.Client;
import Client.Scelta.Controllerscelta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

/**
 * la classe controller corrispondente alla pagina fxml di login
 */

public class Controllerlogin {

    @FXML
    private Label loginErrorLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField tabellaField;

    private Client client;

    @FXML
    private TextField usernameField;

    @FXML
    void login(ActionEvent event) {
        try {
            client.writeInput(usernameField.getText());

            client.writeInput(passwordField.getText());

            if (client.talking5()) {
                client.writeInput(4);
                Parent root;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Scelta/scelta.fxml"));

                try {
                    root = loader.load();

                    Controllerscelta controllerscelta = loader.getController();
                    controllerscelta.setClient(client);
                    controllerscelta.impostaPagina();
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);

                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                }
            } else {
                usernameField.setText("");
                passwordField.setText("");
                loginErrorLabel.setVisible(true);
            }
        } catch (IOException | ClassNotFoundException e) {
        }
    }

    /**
     * imposta il client con una variabile di tipo Client passata in input
     *
     * @param c
     *          Client che verrà utilizzato per sovrascrivere l'attuale client
     */
    public void setClient(Client c) {
        client = c;
    }
}