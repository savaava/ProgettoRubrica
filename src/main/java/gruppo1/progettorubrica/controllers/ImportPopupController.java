package gruppo1.progettorubrica.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import gruppo1.progettorubrica.models.ContactManager;
import gruppo1.progettorubrica.models.AddressBook;
import gruppo1.progettorubrica.models.ContactManager;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @brief Controller che visualizza il popup per l'import di una rubrica.
 * 
 * Tramite tale popup è possibile selezionare il percorso del file .csv o .vCard da cui importare la rubrica.
 */
public class ImportPopupController implements Initializable {
    private ContactManager contactManager;  ///< Istanza dell'interfaccia con i metodi utilizzabili dal controller.

    @FXML
    private Button importButton;   ///< Riferimento al pulsante per importare la rubrica da file.

    /**
     * @brief Inizializzazione controller.
     *
     *  Tramite questo metodo carichiamo su questo controller l'istanza della rubrica in esame.
     * 
     * @param[in] location
     * @param[in] resources 
     */
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.contactManager = AddressBook.getInstance();
    }
    
    /**
     * @brief  Selezione percorso.
     *
     *  Tramite questo metodo l'utente fornisce al controller il file di import della rubrica.
     * 
     * @param[in] event
     */
    @FXML
    private void selectFile(ActionEvent event) {

    }

    /**
     * @brief Importa rubrica.
     *
     * Questo metodo, che viene invocato cliccando il pulsante "Importa", permette di importare la rubrica.
     * @param[in] event
     */
    
    @FXML
    private void onImport(ActionEvent event) {

    }

    /**
     * @brief Controllo formato .csv.
     *
     *  Tramite questo metodo il controller verifica che il file fornito sia di formato .csv.
     * 
     */
    private boolean checkCSVFormat() {
        return false;
    }

    /**
     * @brief Controllo formato .VCard.
     *
     *  Tramite questo metodo il controller verifica che il file fornito sia di formato .csv.
     * 
     */
    private boolean checkVCardFormat() {
        return false;
    }
}
