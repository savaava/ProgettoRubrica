package gruppo1.progettorubrica.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import gruppo1.progettorubrica.models.AddressBook;
import gruppo1.progettorubrica.models.ContactManager;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @brief Controller che visualizza il popup per l'export della rubrica.
 * 
 * Tramite tale popup è possibile selezionare il percorso del file .csv o .vCard in cui esportare la rubrica.
 */

public class ExportPopupController implements Initializable {
    private ContactManager contactManager; ///< Riferimento all'interfaccia ContactManager, implementata da AddressBook.

    @FXML
    private ChoiceBox<String> exportChoiceBox; ///< Permette all'utente di scegliere quali contatti esportare.

    @FXML
    private Button saveButton; ///<Pulsante per concludere l'esportazione.

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
     * @brief Scelta del percorso del file di esportazione.
     *
     * Invocando questo metodo il controller permette all'utente di scegliere 
     * il path su dove salvare il file di export della rubrica.
     *
     * @param[in] event
     */
    @FXML
    private void choosePath(ActionEvent event) {

    }

    /**
     * @brief Esporta la rubrica.
     * 
     * Questo metodo, che viene invocato cliccando il pulsante "Esporta", permette di esportare la rubrica.
     * @param[in] event 
     * @see onExportCSV(ActionEvent event)
     * @see onExportVCard(ActionEvent event)
     */
    @FXML
    private void onExport(ActionEvent event) {

    }
    
    
    /**
     * @brief Esporta la rubrica in un file nel formato .csv.
     * @param[in] event 
     */
    private void onExportCSV(ActionEvent event) {
        
    }
    
    /**
     * @brief Esporta la rubrica in un file nel formato .vCard.
     * @param[in] event 
     */
    private void onExportVCard(ActionEvent event) {
        
    }
    
}
