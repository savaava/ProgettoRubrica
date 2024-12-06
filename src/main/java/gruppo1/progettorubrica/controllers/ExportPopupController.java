package gruppo1.progettorubrica.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import gruppo1.progettorubrica.models.AddressBook;

import java.net.URL;
import java.util.ResourceBundle;

public class ExportPopupController implements Initializable {
    private AddressBook addressBook;

    @FXML
    private ChoiceBox<String> exportChoiceBox;

    @FXML
    private RadioButton vCardButton, csvButton;

    @FXML
    private ToggleGroup ext;

    @FXML
    private Button saveButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.addressBook = AddressBook.getInstance();
    }

    @FXML
    private void choosePath(ActionEvent event) {

    }

    @FXML
    private void onExportButtonPressed(ActionEvent event) {

    }
}
