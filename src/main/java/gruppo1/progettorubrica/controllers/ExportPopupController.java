package gruppo1.progettorubrica.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import gruppo1.progettorubrica.models.AddressBook;
import gruppo1.progettorubrica.models.ContactManager;

import java.net.URL;
import java.util.ResourceBundle;

public class ExportPopupController implements Initializable {
    private ContactManager contactManager;

    @FXML
    private ChoiceBox<String> exportChoiceBox;

    @FXML
    private RadioButton vCardButton, csvButton;

    @FXML
    private ToggleGroup ext;

    @FXML
    private Button saveButton;

    /**
     * @brief Inizializzazione controller
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
     * @brief Scelta del percorso del file di esportazione
     *
     * Invocando questo metodo il controller permette all'utente di scegliere 
     * il path su dove salvare il file di export della rubrica
     *
     * @param[in] event
     */
    @FXML
    private void choosePath(ActionEvent event) {

    }

    /**
     * @brief Esporta la rubrica
     * 
     * Questo metodo, che viene invocato cliccando il pulsante "Esporta", permette di esportare la rubrica
     * @param[in] event 
     * @see onExportCSV(ActionEvent event)
     * @see onExportVCard(ActionEvent event)
     */
    @FXML
    private void onExport(ActionEvent event) {

    }
    
    
    /**
     * @brief Esporta la rubrica in un file nel formato .csv
     * @param[in] event 
     */
    private void onExportCSV(ActionEvent event) {
        
    }
    
    /**
     * @brief Esporta la rubrica in un file nel formato .vCard
     * @param[in] event 
     */
    private void onExportVCard(ActionEvent event) {
        
    }
    
}
