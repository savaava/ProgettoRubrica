package gruppo1.progettorubrica.controllers;

import gruppo1.progettorubrica.models.AddressBook;
import gruppo1.progettorubrica.models.Contact;
import java.io.ByteArrayInputStream;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

/**
 * @brief Controller che si occupa della scena iniziale.
 * 
 * In tale scena è possibile aggiungere, rimuovere, modificare e cercare contatti.
 * Inoltre è possibile aggiungere un'immagine profilo ad un contatto.
 */

public class MainController implements Initializable {
    private AddressBook addressBook;

    @FXML
    private Button deleteButton, saveButton, editButton, cancelButton;  ///< riferimenti ai pulsanti della visione dettagliata del contatto

    @FXML
    private VBox tagVBox;   ///< VBox in cui sono elencati i tag associati al contatto, se presenti

    @FXML
    private TextField searchField;  ///< campo di ricerca dei contatti
    
    @FXML
    private TextField nameField;
    
    @FXML
    private TextField surnameField, emailField, numberField; ///< campi testuali del contatto nella visione dettagliata {@link MainController#}

    @FXML
    private ImageView profileImageView; ///< immagine profilo del contatto
    @FXML
    private ImageView filterImage; ///< immagine del filtro

    @FXML
    private TableView<Contact> contactsTable;   ///< elenco con tutti i contatti della rubrica

    @FXML
    private TableColumn<Contact, String> nameColumn,surnameColumn;   ///< colonne della {@link MainController#contactsTable}.

    @FXML
    private AnchorPane contactDetailsPane;   ///< Pane con la visione dettagliata del contatto

    @FXML
    private GridPane numbersPane, emailsPane;  ///< GridPane appartenente a {@link MainController#contactsDetailsPane} che contiene gli eventuali numeri di telefono e/o emails

    private ContextMenu contextMenu;  ///< menù per importare, esportare e configurare database

    private FilteredList<Contact> filteredContacts;  ///< lista filtrata in base a tag e/o alla sottostringa presente in searchField

    
    
    /**
     * @brief Inizializza il main controller
     *
     *  Questo metodo permette l'inizializzazione del controller in fase di apertura 
     *  tramite il metodo getInstance {@link AddressBook#getInstance()}.
     *
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.addressBook = AddressBook.getInstance();
        this.contactDetailsPane.setVisible(false);
        this.contactsTable.setItems(addressBook.getAllContacts());
        nameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory("surname"));
        
     
        
    }

    /**
     * @brief Imposta la foto profilo circolare
     *
     *  Questo metodo permette di avere la foto profilo di un contatto circolare (di default la foto è di forma rettangolare).
     *
     */
    private void setImageCircle() {

    }

    /**
     * @brief Aggiorna l'elenco dei contatti
     *
     *  Questo metodo si occupa di svolgere il binding che permette l'aggiornamento della listView in base ai parametri di ricerca.
     */
    private void searchFieldBinding() {

    }

    /**
     * @brief Gestisce l'evento di cliccare sull'icona di filtraggio
     *
     * Questo metodo permette, al verificarsi del mouseEvent, di aprire il menù contestuale di gestione dei tags.
     *
     * @param[in] mouseEvent
     */
    @FXML
    public void onFilterIconClicked(MouseEvent mouseEvent) {

    }

    /**
     * @brief Aggiunge contatto
     *
     * Questo metodo per l'aggiunta del contatto viene lanciato sia dal pulsante + che da "Crea contatto" nel menù contestuale.
     *
     * @param[in] event
     */
    @FXML
    private void onAddContact(ActionEvent event) {
        this.contactDetailsPane.setVisible(true);
        deleteButton.setDisable(false);
        editButton.setDisable(false);
        
        TextField numberField2=new TextField();
        TextField numberField3=new TextField();
        TextField emailField2=new TextField();
        TextField emailField3=new TextField();
        
        
        //appena si inserisce 1 carattere nel primo numberTextField, compare il secondo TextField
        numberField.textProperty().addListener( (observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && !numbersPane.getChildren().contains(numberField2)){
                GridPane.setRowIndex(numberField2, 1);
                GridPane.setColumnIndex(numberField2, 1);
                numbersPane.getChildren().add(numberField2);
            }
        else if(newValue.isEmpty()) {//cancello il secondo TextField se ha cancellato il contenuto del primo TextField e se il secondo TextField è vuoto
                if(!numberField2.getText().isEmpty() && numberField.getText().isEmpty()){
                    numberField.setText(numberField2.getText());
                    numberField2.setText("");
                }
                numbersPane.getChildren().remove(numberField2);
        }
        });
        
        numberField2.textProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue==null && newValue!=null)
                numbersPane.add(numberField3, 1, 2);
        else if(oldValue!=null && newValue==null && (numberField3.getText()==null))
                numbersPane.getChildren().remove(numberField3);
        });
        
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue==null && newValue!=null)
                emailsPane.add(emailField2, 1, 1);
        else if(oldValue!=null && newValue==null && (emailField2.getText()==null))
                emailsPane.getChildren().remove(emailField2);
        });
        
        emailField2.textProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue==null && newValue!=null)
                emailsPane.add(emailField3, 1, 2);
        else if(oldValue!=null && newValue==null && (emailField3.getText()==null))
                emailsPane.getChildren().remove(emailField3);
        });
        
        
    }
    

    /**
     * @brief Vista dettagliata del contatto
     *
     * Questo metodo permette, al verificarsi dell'evento, di poter rendere visibile la visione dettagliata del contatto scelto.
     *
     * @param[in] event
     */
    @FXML
    private void onContactClicked(MouseEvent event) {
        this.contactDetailsPane.setVisible(true);
        
        nameField.setEditable(false);
        surnameField.setEditable(false);
        emailField.setEditable(false);
        numberField.setDisable(false);
        
        saveButton.setDisable(true);
        editButton.setDisable(true);
        
        Contact contactSelected=contactsTable.getSelectionModel().getSelectedItem();
        
        nameField.setText(contactSelected.getName());
        surnameField.setText(contactSelected.getSurname());
        
        String emails[]=contactSelected.getEmails();
        if(emails[0]!=null) emailField.setText(emails[0]);
        if(emails[1]!=null) emailsPane.add(new TextField(emails[1]), 1, 1);
        if(emails[2]!=null) emailsPane.add(new TextField(emails[2]), 1, 2);
        
        String numbers[]=contactSelected.getNumbers();
        if(numbers[0]!=null) numberField.setText(numbers[0]);
        if(numbers[1]!=null) numbersPane.add(new TextField(numbers[1]), 1, 1);
        if(numbers[2]!=null) numbersPane.add(new TextField(numbers[2]), 1, 2);
        
        Byte imageInByte[]=contactSelected.getProfilePicture();
        byte image[]=new byte[imageInByte.length];
        for(int i=0; i<imageInByte.length; i++)
            image[i]=imageInByte[i];
        profileImageView.setImage(new Image(new ByteArrayInputStream(image)));
        
        for(Integer id: contactSelected.getAllTagIndexes())
            tagVBox.getChildren().addAll(new Label(addressBook.getTag(id).getDescription()));
        
    }
    
    

    /**
     * @brief Modifica contatto
     *
     * Questo metodo permette, una volta cliccato il pulsante "Modifica", di modificare il contatto in visione dettagliata.
     *
     * @param[in] event
     */
    @FXML
    private void onModifyContact(ActionEvent event) {

    }

    /**
     * @brief Cancella contatto
     *
     * Vedi anche ConfirmPopupController
     * Tramite questo metodo, cliccando il pulsante "Elimina" dalla visione dettagliata del contatto scelto, si può eliminare.
     * Prima di eliminarlo tramite il ConfirmPopupController viene richiesta la conferma dell'operazione
     *
     * @param[in] event
     * @see ConfirmPopupController
     */
    @FXML
    private void onDeleteContact(ActionEvent event) {

    }

    /**
     * @brief Salva contatto
     *
     * Guarda anche: onModifyContact()
     * Questo metodo permette, una volta cliccato il pulsante "Salva", di modificare/creare il contatto in visione dettagliata.
     *
     * @param[in] event
     * @see onModifyContact()
     */
    @FXML
    private void onSaveContact(ActionEvent event) {
        
    }

    /**
     * @brief Annulla modifiche contatto
     *
     * Guarda anche: onModifyContact()
     * Questo metodo permette, una volta cliccato il pulsante "Annulla", di annullare la modifica/creazione di un contatto in visione dettagliata.
     *
     * @param[in] event
     * @see onModifyContact()
     */
    @FXML
    private void onCancel(ActionEvent event) {

    }

    /**
     * @brief Apre popup di import
     *
     * Guarda anche ImportPopupController
     * Questo metodo permette, una volta cliccato sulla voce "Importa" del menù FILE a tendina, di aprire il popup per l'import dei contatti.
     *
     * @param[in] event
     * @see ImportPopupController
     */
    @FXML
    private void showImportPopup(ActionEvent event) {
        try{
            this.openPopup("/views/Import_popup.fxml");
        } catch (IOException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Errore IO");
            alert.setContentText("Dettagli: " + ex.getMessage());
        }
    }

    /**
     * @brief Apre popup di export
     *
     * Guarda anche ExportPopupController
     * Questo metodo permette, una volta cliccato sulla voce "Esporta" del menù FILE a tendina, di aprire il popup per l'export dei contatti.
     *
     * @param[in] event
     * @see ExportPopupController
     */
    @FXML
    private void showExportPopup(ActionEvent event) {
        try{
            this.openPopup("/views/Export_popup.fxml");
        } catch (IOException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Errore IO");
            alert.setContentText("Dettagli: " + ex.getMessage());
        }
    }

    /**
     * @brief Apre popup di collegamento al database
     *
     * Tramite questo metodo, una volta cliccato sulla voce "Configurazione" del menù a tendina FILE, apriamo il popup per aggiungere il database su cui memorizzare i contatti.
     *
     *
     * @param[in] event
     */
    @FXML
    private void showConfigPopup(ActionEvent event) throws IOException {
        showPopup("Config_popup.fxml");
    }


    /**
     * @brief Mostra il popup di gestione dei tag
     *
     * Guarda anche ManageTagsPopupController
     * Questo metodo si occupa della gestione dei tag.
     *
     * @param[in] event
     * @see ManageTagsPopupController
     */
    @FXML
    private void showManageTagsPopup(ActionEvent event) {

    }

    /**
     * @brief Crea menù contestuale
     *
     * Questo metodo, eseguito solo in fase di inizializzazione della rubrica, crea il menù contestuale
     * @return Il menù contestuale
     */
    private ContextMenu createContextMenu(){
        return null;
    }
    
    private void openPopup(String path) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        
        Stage newStage = new Stage();
        newStage.setScene(scene);
        
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.show();
    }

    //Metodi di utilità

    private void showPopup(String path) throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/" + path)));
        Scene scene = new Scene(root);

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setResizable(false);
        popup.setScene(scene);
        popup.showAndWait();
    }

    @FXML
    private void showImagePopup(MouseEvent event) {
    }
    
}
