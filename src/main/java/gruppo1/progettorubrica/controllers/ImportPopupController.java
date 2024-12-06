package gruppo1.progettorubrica.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import gruppo1.progettorubrica.models.AddressBook;

import java.net.URL;
import java.util.ResourceBundle;

public class ImportPopupController implements Initializable {
    private AdressBook AdressBook;

    @FXML
    private Button importButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.AdressBook = AdressBook.getInstance();
    }

    @FXML
    private void selectFile(ActionEvent event) {

    }

    @FXML
    private void onImportButtonPressed(ActionEvent event) {

    }

    private boolean checkCSVFormat() {
        return false;
    }

    private boolean checkVCardFormat() {
        return false;
    }
}