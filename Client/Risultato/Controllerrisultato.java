package Client.Risultato;

import java.io.IOException;

import Client.Client;
import Client.Predizione.Controllerpredizione;
import Client.Scelta.Controllerscelta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;

/**
 * la classe controller corrispondente alla pagina fxml risultatopredizione
 */

public class Controllerrisultato {

    @FXML
    private Button esciButton;

    @FXML
    private Label risultatoLabel;
    private Client client;

    @FXML
    void cambiaModalita(ActionEvent event) {
        Parent root;
        try {
            client.writeInput(5);
            client.writeInput("y");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Scelta/scelta.fxml"));
            root = loader.load();
            Controllerscelta controllerscelta = loader.getController();
            controllerscelta.setClient(client);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * imposta il risultato nella schermata di risultatopredizione
     */
    public void impostaRisultato() {
        risultatoLabel.setText(String.valueOf(client.talking4()));
    }

    /**
     * imposta il client corrente sovrascrivendolo al client passato come argomento
     * 
     * @param client
     *               rappresenta il client collegato con il server
     */
    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    void cambiaQueryPoint(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Predizione/predict.fxml"));
            root = loader.load();
            Controllerpredizione controllerpredizione = loader.getController();
            controllerpredizione.setClient(client);
            controllerpredizione.impostaPagina();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void uscita(ActionEvent event) {
        try {
            client.writeInput(5);
            client.writeInput("n");
            Stage stage = (Stage) esciButton.getScene().getWindow();
            stage.close();
        } catch (IOException | ClassNotFoundException e) {
        }
    }
}
