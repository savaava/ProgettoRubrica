package gruppo1.progettorubrica.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * @brief Classe controller per la finestra di configurazione dell'URL del database
 */
public class ConfigPopupController {
    @FXML
    private Button verifyButton; ///< Bottone per verificare la configurazione

    @FXML
    private Button confirmButton; ///< Bottone per confermare la configurazione una volta verificata

    @FXML
    private TextField textField; ///< Campo di testo per inserire l'URL del database

    /**
     * @brief Metodo che viene chiamato quando si preme il bottone verifyButton
     * @param[in] event
     * 
     * Metodo che viene chiamato quando si preme il bottone per 
     * verificare l'URL del database inserito nel textField
     */
    @FXML
    private void onVerify(ActionEvent event) {
        
    }

    /**
     * @brief Metodo che viene chiamato quando si preme il bottone confirmButton
     * @param[in] event
     * 
     * Metodo che viene chiamato quando si preme il bottone per 
     * confermare l'URL del database inserito nel textField
     */
    @FXML
    private void onConfirm(ActionEvent event) {
        
    }
}
