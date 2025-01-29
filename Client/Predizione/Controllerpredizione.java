package Client.Predizione;

import java.io.IOException;
import java.util.ArrayList;

import Client.Client;
import Client.Risultato.Controllerrisultato;
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
 * la classe controller corrispondente alla pagina fxml predict
 */

public class Controllerpredizione {
    @FXML
    private Label errorLabel;

    @FXML
    private Label valoreDiKLabel;

    @FXML
    private Label valorePrimaColonnaLabel;

    @FXML
    private Label valoreSecondaColonnaLabel;

    @FXML
    private TextField valoreDiKField;

    @FXML
    private Label valoreTerzaColonnaLabel;

    @FXML
    private Label valoreQuartaColonnaLabel;

    @FXML
    private TextField valorePrimaColonnaField;

    @FXML
    private TextField valoreQuartaColonnaField;

    @FXML
    private TextField valoreSecondaColonnaField;
    @FXML
    private TextField valoreTerzaColonnaField;
    private Client client;
    private ArrayList<String> valori;

    @FXML
    void predizione(ActionEvent event) {
        valori = new ArrayList<>();
        valori.add(valorePrimaColonnaField.getText());
        valori.add(valoreSecondaColonnaField.getText());
        valori.add(valoreTerzaColonnaField.getText());
        valori.add(valoreQuartaColonnaField.getText());
        boolean flag = client.talking3(valori, valoreDiKField.getText());
        if (flag == false) {
            errorLabel.setVisible(true);
            valorePrimaColonnaField.setText("");
            valoreSecondaColonnaField.setText("");
            valoreDiKField.setText("");
            valoreTerzaColonnaField.setText("");
            valoreQuartaColonnaField.setText("");
            errorLabel.setText("inserire dei valori corretti");
        } else {
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Risultato/risultatopredizione.fxml"));
            try {
                root = loader.load();
                Controllerrisultato controllerrisultato = loader.getController();
                controllerrisultato.setClient(client);
                controllerrisultato.impostaRisultato();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);

                stage.setScene(scene);

                stage.show();
            } catch (IOException e) {
            }
        }
    }

    /**
     * imposta il client
     * 
     * @param c
     *          rappresenta il client collegato con il server
     */
    public void setClient(Client c) {
        client = c;
    }

    /**
     * imposta la pagina corrente rendendo visibili o meno le label e caselle di
     * testo in base al numero di colonne presenti nel file contenente il training
     * set
     */
    public void impostaPagina() {
        String risposta;
        int i = 0;
        try {
            client.writeInput(4);
            risposta = "";
            while (!risposta.equals("@ENDEXAMPLE")) {
                risposta = client.talking2();
                if (risposta.equals("@READSTRING")) {
                    switch (i) {
                        case 0:
                            valorePrimaColonnaLabel.setText("inserire una stringa per la prima colonna");
                            break;
                        case 1:
                            valoreSecondaColonnaLabel.setText("inserire una stringa per la seconda colonna");
                            break;
                        case 2: {
                            valoreTerzaColonnaLabel.setVisible(true);
                            valoreTerzaColonnaField.setVisible(true);
                            valoreTerzaColonnaLabel.setText("inserire una stringa per la terza colonna");
                            break;
                        }
                        case 3: {
                            valoreQuartaColonnaLabel.setVisible(true);
                            valoreQuartaColonnaField.setVisible(true);
                            valoreQuartaColonnaLabel.setText("inserire una stringa per la quarta colonna");
                            break;
                        }
                    }

                } else if (risposta.equals("@READDOUBLE")) {
                    switch (i) {
                        case 0:
                            valorePrimaColonnaLabel.setText("inserire un numero per la prima colonna");
                            break;
                        case 1:
                            valoreSecondaColonnaLabel.setText("inserire un numero per la seconda colonna");
                            break;
                        case 2: {
                            valoreTerzaColonnaLabel.setVisible(true);
                            valoreTerzaColonnaField.setVisible(true);
                            valoreTerzaColonnaLabel.setText("inserire un numero per la terza colonna");
                            break;
                        }
                        case 3:
                            valoreQuartaColonnaField.setVisible(true);
                            valoreQuartaColonnaLabel.setVisible(true);
                            valoreQuartaColonnaLabel.setText("inserire un numero per la quarta colonna");
                            break;
                    }
                }
                i++;
            }
        } catch (ClassNotFoundException | IOException e) {

            e.printStackTrace();
        }

    }
}
