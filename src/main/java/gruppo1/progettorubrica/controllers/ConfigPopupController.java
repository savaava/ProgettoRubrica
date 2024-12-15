package gruppo1.progettorubrica.controllers;

import gruppo1.progettorubrica.models.AddressBook;
import gruppo1.progettorubrica.services.Database;
import java.io.IOException;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @brief Controller per la finestra di configurazione dell'URL del database.
 */
public class ConfigPopupController implements Initializable {
    private AddressBook addressBook;

    @FXML
    private Label resultLabel;

    @FXML
    private Button verifyButton; ///< Bottone per verificare la configurazione.

    @FXML
    private Button confirmButton; ///< Bottone per confermare la configurazione una volta verificata.

    @FXML
    private TextField textField; ///< Campo di testo per inserire l'URL del database.

    /**
     * @brief Inizializza il main controller.
     *
     *  Questo metodo permette l'inizializzazione del controller in fase di apertura.
     *
     * @pre Nessuna
     * @post - Viene ottenuta l'istanza singleton della rubrica
     *       - Viene effettuato il binding per verificare che la stringa presente nel textField
     *       convalidi l'espressione regex del link al database.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.addressBook = AddressBook.getInstance();
        } catch (IOException ex) {ex.printStackTrace();}

        BooleanBinding buttonBinding = new BooleanBinding() {
            {
                super.bind(textField.textProperty());
            }
            @Override
            protected boolean computeValue() {
                return !textField.getText().matches("^mongodb\\+srv:\\/\\/([^:]+):([^@]+)@([^\\/]+)(\\/?.*)$");
            }
        };

        verifyButton.disableProperty().bind(buttonBinding);
    }

    /**
     * @brief Metodo che viene chiamato quando si preme il bottone verifyButton.
     * @param[in] event
     * 
     * Metodo che viene chiamato quando si preme il bottone per 
     * verificare l'URL del database inserito nel textField
     */
    @FXML
    private void onVerify(ActionEvent event) {
        String url = textField.getText();

        if(Database.verifyDBUrl(url)) {
            resultLabel.setVisible(true);
            resultLabel.setText("Link Valido");
            confirmButton.setDisable(false);
        } else {
            resultLabel.setVisible(true);
            resultLabel.setText("Link Non valido");
            confirmButton.setDisable(true);
        }

        //Disabilitiamo il confirmButton e nascondiamo la resultLabel nel caso in cui viene modificato textField
        textField.setOnKeyPressed(e -> {
            confirmButton.setDisable(true);
            resultLabel.setVisible(false);
        });
    }

    /**
     * @brief Metodo che viene chiamato quando si preme il bottone confirmButton
     * @param[in] event
     * 
     * Metodo che viene chiamato quando si preme il bottone per 
     * confermare l'URL del database inserito nel textField
     */
    @FXML
    private void onConfirm(ActionEvent event) throws IOException {
        //Imposta l'url del database nell'attributo corrispondente di addressBook
        addressBook.setDBUrl(textField.getText());
        //Viene effettuato il salvataggio della configurazione
        addressBook.saveConfig();
        //Inizializza il database
        addressBook.initDB();
        //Chiusura della finestra
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}
