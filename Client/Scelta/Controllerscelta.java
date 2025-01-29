package Client.Scelta;

import java.io.IOException;

import Client.Client;
import Client.Login.Controllerlogin;
import Client.Predizione.Controllerpredizione;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * la classe controller corrispondente alla pagina fxml scelta
 */
public class Controllerscelta {

    @FXML
    private Label errorLabel;
    private Client client;

    @FXML
    private Button databaseButton;
    @FXML
    private Button confermaButton;

    @FXML
    private Button fileDiTestoButton;

    @FXML
    private Button fileBinarioButton;

    @FXML
    private Label fileLabel;

    @FXML
    private TextField nomeFileField;
    @FXML
    private Label confermaLabel;
    private int decision = 0;
    @FXML
    private Button avantiBotton;

    @FXML
    void conferma(ActionEvent event) {
        confermaLabel.setVisible(false);
        confermaButton.setVisible(false);

        if (decision == 1 || decision == 2) {
            fileLabel.setVisible(true);
            avantiBotton.setVisible(true);
            nomeFileField.setVisible(true);
            fileLabel.setText("inserire nome del file");
        } else {
            try {
                client.writeInput(3);
                Parent root;
                Stage stage;
                Scene scene;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Login/loginpage.fxml"));
                root = loader.load();

                Controllerlogin controllerlogin = loader.getController();
                controllerlogin.setClient(client);
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                scene = new Scene(root);

                stage.setScene(scene);

                stage.show();
            } catch (IOException | ClassNotFoundException e) {
            }
        }
    }

    @FXML
    void avanti(ActionEvent event) {
        fileDiTestoButton.setVisible(false);
        fileBinarioButton.setVisible(false);
        databaseButton.setVisible(false);
        Stage stage;

        Scene scene;

        Parent root;
        try {
            if (decision != 0) {
                client.writeInput(decision);
            }
            client.writeInput(nomeFileField.getText());

            if (!client.talking5()) {
                decision = 0;
                nomeFileField.setText("");
                errorLabel.setVisible(true);
                errorLabel.setText("inserire un nome valido");
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Predizione/predict.fxml"));

                root = loader.load();

                Controllerpredizione controllerpredizione = loader.getController();
                controllerpredizione.setClient(client);
                controllerpredizione.impostaPagina();
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                scene = new Scene(root);

                stage.setScene(scene);

                stage.show();
            }

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * imposta la pagina rendendo visibile il campo per l inserimento del nome della
     * tabella e invisibile gli altri campi
     */
    public void impostaPagina() {
        databaseButton.setVisible(false);
        fileDiTestoButton.setVisible(false);
        fileBinarioButton.setVisible(false);
        nomeFileField.setVisible(true);
        fileLabel.setVisible(true);
        fileLabel.setText("inserire il nome della tabella");
        avantiBotton.setVisible(true);
    }

    @FXML
    void database(ActionEvent event) {
        if (decision != 0) {
            fileLabel.setVisible(false);
            nomeFileField.setVisible(false);
        }
        decision = 3;
        confermaLabel.setVisible(true);
        confermaButton.setVisible(true);
        avantiBotton.setVisible(false);
    }

    @FXML
    void fileBinario(ActionEvent event) {
        if (decision != 0) {
            fileLabel.setVisible(false);
            nomeFileField.setVisible(false);
        }
        decision = 2;
        confermaLabel.setVisible(true);
        confermaButton.setVisible(true);
        avantiBotton.setVisible(false);
    }

    @FXML
    void fileDiTesto(ActionEvent event) {
        if (decision != 0) {
            fileLabel.setVisible(false);
            nomeFileField.setVisible(false);
        }
        decision = 1;
        confermaLabel.setVisible(true);
        confermaButton.setVisible(true);
        avantiBotton.setVisible(false);
    }

    /**
     * imposta il client corrente sovrascrivendolo al client passato come argomento
     * 
     * @param c
     *          rappresenta il client collegato con il server
     */
    public void setClient(Client c) {
        client = c;
    }

}
