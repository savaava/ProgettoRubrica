package gruppo1.progettorubrica.controllers;

import gruppo1.progettorubrica.models.AddressBook;
import gruppo1.progettorubrica.models.Tag;
import gruppo1.progettorubrica.models.TagManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
        try {
            this.tagManager = AddressBook.getInstance();
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore di caricamento");
            alert.setHeaderText("Impossibile caricare AddressBook");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
        
        tagsListView.setItems(tagManager.getAllTags()); 
        
        
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
                            this.setText(t.getDescription());
                    }
                    
                };
                return lc;
            }
            
        });
        
        
        this.deleteButton.setDisable(true); 
        this.updateButton.setDisable(true);
        this.addButton.setDisable(true);
        
        
        nameField.textProperty().addListener((observable, oldValue, newValue) -> { 
            if(!newValue.isEmpty()){
                if(tagManager.getAllTags().contains(new Tag(newValue))){
                    addButton.setDisable(true);
                    updateButton.setDisable(true);
                    deleteButton.setDisable(false);
                }
                else{
                    addButton.setDisable(false);
                    updateButton.setDisable(true);
                }
                
                if(!tagsListView.getSelectionModel().getSelectedItems().isEmpty() 
                        && !tagManager.getAllTags().contains(new Tag(nameField.getText()))){
                    addButton.setDisable(false);
                    updateButton.setDisable(false);
                }
            }else{
                addButton.setDisable(true);
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
            }
            
        });
        
    }

    /**
     * @brief Aggiunge un tag alla lista osservabile "tags" in AddressBook
     * @see AddressBook
     */
    @FXML

    private void onAdd(ActionEvent event) throws IOException {
        String name = nameField.getText();
        this.tagManager.addTag(new Tag(name));
        nameField.clear();
        
        
        tagsListView.getSelectionModel().clearSelection();
    }

    /**
     * @brief Aggiorna un tag della lista osservabile "tags" in AddressBook
     * @see AddressBook
     */
    @FXML
private void onUpdate(ActionEvent event) throws IOException {
        this.updateButton.setDisable(true);
        
        Tag tag=this.tagsListView.getSelectionModel().getSelectedItem();
        
        tagManager.removeTag(tag); 
        
        tag.setDescription(this.nameField.getText()); 
        
        tagManager.addTag(tag); 
        
        nameField.clear();
        
        tagsListView.getSelectionModel().clearSelection();
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
    private void onDelete(ActionEvent event) throws IOException {
        this.deleteButton.setDisable(true);
        
        Tag tag=new Tag(nameField.getText());
        this.tagManager.removeTag(tag);
        nameField.clear();
        
        tagsListView.getSelectionModel().clearSelection();
    }

    /**
     * @brief Rileva quando l'utente clicca su un tag.
     * @param[in] event 
     */
    @FXML
    private void onTagClicked(MouseEvent event) {
        Tag tag=this.tagsListView.getSelectionModel().getSelectedItem();
        
        if(tag!=null){
            deleteButton.setDisable(false);
            updateButton.setDisable(true);
            addButton.setDisable(true);
            this.nameField.setText(tag.getDescription());
        }
        
        
        
        
        
    }

}
