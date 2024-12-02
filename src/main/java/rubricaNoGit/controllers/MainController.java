package rubricaNoGit.controllers;

import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseEvent;
import rubricaNoGit.models.Contact;
import rubricaNoGit.models.AddressBook;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private AddressBook addressBook;

    @FXML
    private Button deleteButton, saveButton, editButton, cancelButton;

    @FXML
    private VBox tagVBox, lettersVBox;

    @FXML
    private TextField searchField, nameField, surnameField, emailField, numberField;

    @FXML
    private ImageView profileImageView, filterImage;

    @FXML
    private ListView<Contact> contactsListView;

    @FXML
    private AnchorPane contactDetailsPane;

    @FXML
    private GridPane numbersPane, emailsPane;

    private ContextMenu contextMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.addressBook = AddressBook.getInstance();
    }

    private void setImageCircle() {

    }

    private void searchFieldBinding() {

    }

    @FXML
    public void onFilterIconClicked(MouseEvent mouseEvent) {

    }

    //Menu RUBRICA
    //Questo metodo viene lanciato sia dal pulsante +
    //che da "Crea contatto" nel menù contestuale
    @FXML
    private void onAddButtonPressed(ActionEvent event) {

    }

    @FXML
    private void onContactClicked(MouseEvent event) {

    }

    @FXML
    private void onModifyContact(ActionEvent event) {

    }

    @FXML
    private void onDeleteContact(ActionEvent event) {

    }

    @FXML
    private void onSaveContact(ActionEvent event) {

    }

    @FXML
    private void onCancelContact(ActionEvent event) {

    }

    //Menu FILE
    @FXML
    private void showImportPopup(ActionEvent event) {

    }

    @FXML
    private void showExportPopup(ActionEvent event) {

    }

    @FXML
    private void showConfigPopup(ActionEvent event) {

    }

    @FXML
    private void showImagePopup(ActionEvent event) {
        //Bisogna ottenere il controller tramite il metodo getController()
        //Successivamente bisogna usare i metodi getSelectedImage() e getImageIndex()
    }

    @FXML
    private void showManageTagsPopup(ActionEvent event) {

    }

}
