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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * @brief Controller che visualizza il popup dei tag.
 * 
 * Tale popup permette di aggiungere, modificare o eliminare i tag.
 */
public class ManageTagsPopupController implements Initializable {
    private TagManager tagManager; ///< Riferimento all'interfaccia TagManager, implementata da AddressBook

    @FXML
    private TextField nameField;   
    
    @FXML
    private ListView<String> tagsListView; ///< Riferimento alla lista di tag

    @FXML
    private Button addButton, updateButton, deleteButton; ///< Riferimento ai bottoni di aggiunta, modifica ed eliminazione

    /**
     * @brief Inizializza il main controller
     *
     *  Questo metodo permette l'inizializzazione del controller in fase di apertura tramite il metodo getInstance {@link AddressBook#getInstance()}.
     *
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tagManager = AddressBook.getInstance();
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
     * @brief Aggiorna un tag alla lista osservabile "tags" in AddressBook
     * @see AddressBook
     */
    @FXML
    private void onUpdate(ActionEvent event) {
        //updateButton.setDisable(true);
        nameField.textProperty().bind(tagsListView.getSelectionModel().selectedItemProperty().asString());
        nameField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.equals(oldValue)) { 
                updateButton.setDisable(false);
            } else if(oldValue.equals(null)) {
                updateButton.setDisable(true);
            } else
                updateButton.setDisable(true);
        });
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

    }
}
