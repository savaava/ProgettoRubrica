package gruppo1.progettorubrica.controllers;

import gruppo1.progettorubrica.models.AddressBook;
import gruppo1.progettorubrica.models.Tag;
import gruppo1.progettorubrica.models.TagManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * @brief Controller che visualizza il popup dei tag.
 * 
 * Tale popup permette di aggiungere, modificare o eliminare i tag.
 */
public class ManageTagsPopupController implements Initializable {
    private TagManager tagManager; ///< Riferimento all'interfaccia TagManager, implementata da AddressBook
   
    
    @FXML
    private ListView<Tag> tagsListView; ///< Riferimento alla lista di tag

    @FXML
    private Button addButton, updateButton, deleteButton; ///< Riferimento ai bottoni di aggiunta, modifica ed eliminazione
    
    @FXML
    private TextField nameField; ///< contiene la stringa associata al tag da aggiungere o aggiornare.

    

    /**
     * @brief Inizializza il popup per la gestione dei tag.
     *
     * @pre Avere cliccato su "Gestisci tag" nel menù a tendina "Rubrica".
     * @post Si visualizza il popup in cui è possibile aggiungere, aggiornare o eliminare un tag.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tagManager = AddressBook.getInstance();
        
        tagsListView.setItems(tagManager.getAllTags()); //lego la ListView e la lista osservabile dei tag
        
        //indico cosa deve contenere ogni cella della ListView:
        tagsListView.setCellFactory(new Callback<ListView<Tag>, ListCell<Tag>>(){
            
            @Override
            public ListCell<Tag> call(ListView<Tag> param) {
                ListCell<Tag> lc=new ListCell<Tag>(){
                    
                    @Override
                    public void updateItem(Tag t, boolean empty){
                        super.updateItem(t, empty);
                        if(t==null || empty==true){
                            this.setGraphic(null);
                            this.setText(null);
                        }
                        else
                            this.setText(t.getDescrizione());
                    }
                    
                };
                return lc;
            }
            
        });
        
        this.addButton.disableProperty().bind(this.nameField.textProperty().isEmpty()); //addButton è disattivato finquando non si scrive qualcosa nel TextField
        
        //deleteButton e updateButton sono disabilitati finquando non si clicca su un tag
        this.deleteButton.setDisable(true); 
        this.updateButton.setDisable(true);
        
    }

    /**
     * @brief Aggiunge un tag alla lista osservabile "tags" in AddressBook
     * @see AddressBook
     */
    @FXML
    private void onAdd(ActionEvent event) {
        String name = nameField.getText();
        Tag tag = new Tag(name);
        this.tagManager.addTag(tag);
    }

    /**
     * @brief Aggiorna un tag della lista osservabile "tags" in AddressBook
     * @see AddressBook
     */
    @FXML
    private void onUpdate(ActionEvent event) {
        this.updateButton.setDisable(true);
        
        Tag tag=this.tagsListView.getSelectionModel().getSelectedItem(); //ottengo il tag selezionato dall'utente nella ListView
        
        tagManager.removeTag(tag); 
        
        tag.setDescrizione(this.nameField.getText()); //aggiorno il campo descrizione con quanto inserito dall'utente nel TextField
        
        tagManager.addTag(tag); //inserisco il tag aggiornato
    }

    /**
     * @brief Elimina un tag alla lista osservabile "tags" in AddressBook 
     * 
     * Vedi anche ConfirmPopupController
     * 
     * Prima di eliminare tramite il ConfirmPopupController viene richiesta la conferma dell'operazione.
     * @see AddressBook
     * @see ConfirmPopupController
     */
    @FXML
    private void onDelete(ActionEvent event) {
        this.deleteButton.setDisable(true);
        
        Tag tag=this.tagsListView.getSelectionModel().getSelectedItem();
        this.tagManager.removeTag(tag);
    }

    /**
     * @brief Rileva quando l'utente clicca su un tag.
     * @param[in] event 
     */
    @FXML
    private void onTagClicked(MouseEvent event) {
        this.deleteButton.setDisable(false);
        this.updateButton.setDisable(false);
        
        Tag tag=this.tagsListView.getSelectionModel().getSelectedItem();
        this.nameField.setText(tag.getDescrizione());
    }
}
