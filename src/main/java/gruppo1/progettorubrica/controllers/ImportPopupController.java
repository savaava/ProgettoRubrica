package gruppo1.progettorubrica.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import gruppo1.progettorubrica.models.AddressBook;
import gruppo1.progettorubrica.models.ContactManager;

import java.net.URL;
import java.util.ResourceBundle;

public class ImportPopupController implements Initializable {
    private ContactManager contactManager;

    @FXML
    private Button importButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.contactManager = AddressBook.getInstance();
    }

    @FXML
    private void selectFile(ActionEvent event) {

    }

    @FXML
    private void onImport(ActionEvent event) {

    }

    private boolean checkCSVFormat() {
        return false;
    }

    private boolean checkVCardFormat() {
        return false;
    }
}
