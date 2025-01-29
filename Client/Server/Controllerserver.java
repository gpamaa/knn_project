package Client.Server;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import Client.Client;
import Client.Scelta.Controllerscelta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

/**
 * la classe controller corrispondente alla pagina fxml server
 */
public class Controllerserver {

    @FXML
    private Label erroreLabel;

    @FXML
    private TextField indirizzoField;

    @FXML
    private TextField portaField;

    @FXML
    void btConnettiClicked(ActionEvent event) {
        String[] string = new String[2];
        string[0] = indirizzoField.getText();
        string[1] = portaField.getText();
        if (string[0].equals("") && string[1].equals("")) {
            erroreLabel.setVisible(true);
            erroreLabel.setText("inserire porta e indirizzo ip");
        } else if (string[0].equals("")) {
            erroreLabel.setVisible(true);
            erroreLabel.setText("inserire indirizzo ip");
        } else if (string[1].equals("")) {
            erroreLabel.setVisible(true);
            erroreLabel.setText("inserire numero di porta");
        } else {
            Stage stage;

            Scene scene;

            Parent root;
            try {
                Client c = new Client(string[0], Integer.valueOf(string[1]));
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Scelta/scelta.fxml"));
                root = loader.load();

                Controllerscelta controllerscelta = loader.getController();
                controllerscelta.setClient(c);
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                scene = new Scene(root);

                stage.setScene(scene);

                stage.show();

            } catch (UnknownHostException e) {
                erroreLabel.setVisible(true);
                erroreLabel.setText("inserire indirizzo ip valido");
                indirizzoField.setText("");
            } catch (NumberFormatException e) {
                erroreLabel.setVisible(true);
                erroreLabel.setText("inserire numero di porta valido");
                portaField.setText("");
            } catch (ConnectException e) {
                erroreLabel.setVisible(true);
                erroreLabel.setText("inserire porta corretta");
                portaField.setText("");
            } catch (IOException e) {
            } catch (ClassNotFoundException e) {
            }
        }
    }
}
