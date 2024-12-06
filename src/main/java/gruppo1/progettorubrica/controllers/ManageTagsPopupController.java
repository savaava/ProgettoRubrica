package gruppo1.progettorubrica.controllers;

import gruppo1.progettorubrica.models.AddressBook;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageTagsPopupController implements Initializable {
    private AddressBook addressBook; ///< Riferimento all'AddressBook

    @FXML
    private ListView<String> tagsListView; ///< Riferimento alla lista di tag

    @FXML
    private Button addButton, updateButton, deleteButton; ///< Riferimento ai bottoni di aggiunta, modifica ed eliminazione

    /**
     * @brief Inizializza il main controller
     *
     *  Questo metodo permette l'inizializzazione del controller in fase di apertura.
     *
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * @brief Aggiunge un tag alla lista osservabile "tags" in AddressBook
     * @see AddressBook
     */
    @FXML
    private void onAdd(ActionEvent event) {

    }

    /**
     * @brief Aggiorna un tag alla lista osservabile "tags" in AddressBook
     * @see AddressBook
     */
    @FXML
    private void onUpdate(ActionEvent event) {

    }

    /**
     * @brief Elimina un tag alla lista osservabile "tags" in AddressBook
     * @see AddressBook
     */
    @FXML
    private void onDelete(ActionEvent event) {

    }
}
