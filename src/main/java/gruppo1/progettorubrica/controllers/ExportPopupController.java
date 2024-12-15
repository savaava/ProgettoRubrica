package gruppo1.progettorubrica.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import gruppo1.progettorubrica.models.AddressBook;
import gruppo1.progettorubrica.models.Contact;
import gruppo1.progettorubrica.models.ContactManager;
import gruppo1.progettorubrica.models.Tag;
import gruppo1.progettorubrica.models.TagManager;
import gruppo1.progettorubrica.services.Converter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

/**
 * @brief Controller che visualizza il popup per l'export della rubrica.
 * 
 * Tramite tale popup è possibile selezionare il percorso del file .csv o .vCard in cui esportare la rubrica.
 */

public class ExportPopupController implements Initializable {
    private ContactManager contactManager; ///< Riferimento all'interfaccia ContactManager, implementata da AddressBook.
    
    private TagManager tagManager;  ///< Riferimento all'interfaccia ContactManager, implementata da AddressBook.
    
    private File file;  ///< Riferimento al percorso di export del file

    @FXML
    private ChoiceBox<String> exportChoiceBox; ///< Permette all'utente di scegliere quali contatti esportare.

    @FXML
    private Button saveButton; ///<Pulsante per concludere l'esportazione.
    

    /**
     * @brief Inizializzazione controller.
     *
     *  Tramite questo metodo carichiamo su questo controller l'istanza della rubrica in esame 
     *  utilizzando il metodo getInstance {@link AddressBook#getInstance()}.
     * 
     * @param[in] location
     * @param[in] resources 
     * 
     * @pre Nessuna
     * @post Il controller ha un'istanza di AddresssBook
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.tagManager = AddressBook.getInstance();
            this.contactManager = AddressBook.getInstance();
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore di caricamento");
            alert.setHeaderText("Impossibile caricare AddressBook");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
        saveButton.setDisable(true);
        Collection<String> descriptionTags = new ArrayList<>();
        descriptionTags.add("Tutti i contatti");
        for(Tag tag : tagManager.getAllTags())
            descriptionTags.add(tag.getDescription());
        exportChoiceBox.getItems().setAll(descriptionTags);
        exportChoiceBox.getSelectionModel().selectFirst();
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
    FileChooser filechooser = new FileChooser();
    filechooser.setTitle("Scegliere percorso file");
    filechooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("CSV", "*.csv"),
            new FileChooser.ExtensionFilter("VCard", "*.vcf")
    );
    this.file = filechooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
    if (file != null && exportChoiceBox.getValue() != null)
        saveButton.setDisable(false);
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
        String f = this.file.toString();
        String ext = f.substring(f.indexOf(".") + 1);
        String selectedTag = exportChoiceBox.getValue();
        ArrayList<Contact> c = null;
        try{
            if(ext.equalsIgnoreCase("csv")){
                if(selectedTag.equals("Tutti i contatti"))
                   c = new ArrayList<>(this.contactManager.getAllContacts());
                else{
                    c = new ArrayList<>(contactManager.getContactsFromTag(tagManager.getTag(selectedTag)));
                }  
                Converter.onExportCSV(c, this.file);
            }
            
            else if(ext.equalsIgnoreCase("vcf")){
                if(selectedTag.equals("Tutti i contatti"))
                   c = new ArrayList<>(this.contactManager.getAllContacts());
                else{
                    c = new ArrayList<>(contactManager.getContactsFromTag(tagManager.getTag(selectedTag)));
                }
                c = new ArrayList<>(this.contactManager.getAllContacts());
                Converter.onExportVCard(c, this.file);
            }
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch(IOException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Errore IO");
            alert.setContentText("Dettagli: " + ex.getMessage());
        }
        
    }
    
    
    
    
}
