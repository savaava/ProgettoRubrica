package gruppo1.progettorubrica.controllers;

import gruppo1.progettorubrica.models.AddressBook;
import gruppo1.progettorubrica.models.Contact;
import java.io.ByteArrayInputStream;
import gruppo1.progettorubrica.models.Tag;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.binding.BooleanBinding;
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

    private TextField numberField2,numberField3,emailField2,emailField3;
    
    
    /**
     * @brief Inizializza il main controller
     *
     *  Questo metodo permette l'inizializzazione del controller in fase di apertura 
     *  tramite il metodo getInstance {@link AddressBook#getInstance()}.
     *
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.addressBook = AddressBook.getInstance();
        } catch (IOException ex) {ex.printStackTrace();}

        //Nasconde la visione dettagliata
        this.contactDetailsPane.setVisible(false);

        //Riempie la tabella con i contatti
        this.contactsTable.setItems(addressBook.getAllContacts());
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        //Crea il menù contestuale
        this.contextMenu = this.createContextMenu();
     
        //Imposta immagine contatto circolare
        this.setImageCircle();

        //Imposta immagine imbuto
        filterImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/filter.png"))));
        
        numberField2=new TextField();
        numberField3=new TextField();
        emailField2=new TextField();
        emailField3=new TextField();
        
        BooleanBinding op1 = nameField.textProperty().isEmpty();
        BooleanBinding op2 = surnameField.textProperty().isEmpty();
        saveButton.disableProperty().bind(op1.and(op2));
    }

    /**
     * @brief Imposta la foto profilo circolare
     *
     *  Questo metodo permette di avere la foto profilo di un contatto circolare (di default la foto è di forma rettangolare).
     *
     */
    private void setImageCircle() {
        int radius = 75;

        profileImageView.setFitWidth(radius * 2);
        profileImageView.setFitHeight(radius * 2);

        Circle clip = new Circle(radius,radius, radius*0.75);
        profileImageView.setClip(clip);

        Circle border = new Circle(radius, radius, radius);
        border.setStroke(Color.BLUE);
        border.setStrokeWidth(4);
        border.setFill(Color.TRANSPARENT);
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
        deleteButton.setDisable(true);
        editButton.setDisable(true);

        //Imposta immagine profilo di default
        profileImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/base_profile.jpg"))));       
        
        //appena si inserisce 1 carattere nel primo numberTextField, compare il secondo TextField
        numberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && !numbersPane.getChildren().contains(numberField2)){
                numbersPane.add(numberField2, 1, 1);
            }else if(newValue.isEmpty()) {//se ha cancellato il contenuto del primo TextField devo rimuovere il secondo
                if(!numberField2.getText().isEmpty()){ //prima di rimuovere il secondo TextField vedo se c'è scritto qualcosa e lo inserisco nel primo e cancello quanto scritto nel secondo
                    numberField.setText(numberField2.getText());
                    numberField2.clear();
                }                
                numbersPane.getChildren().remove(numberField2);
            }
        });
        
        numberField2.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && !numbersPane.getChildren().contains(numberField3))
                numbersPane.add(numberField3, 1, 2);
            else if(newValue.isEmpty()){
                if(!numberField3.getText().isEmpty()){ 
                    numberField2.setText(numberField3.getText());
                    numberField3.clear();
                }
                numbersPane.getChildren().remove(numberField3);
            }
        });
        
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && !emailsPane.getChildren().contains(emailField2))
                emailsPane.add(emailField2, 1, 1);
            else if(newValue.isEmpty()){
                if(!emailField2.getText().isEmpty()){ 
                    emailField.setText(emailField2.getText());
                    emailField2.clear();
                }
                emailsPane.getChildren().remove(emailField2);
            }
        });
        
        emailField2.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && !emailsPane.getChildren().contains(emailField3))
                emailsPane.add(emailField3, 1, 2);
            else if(newValue.isEmpty()){
                if(!emailField3.getText().isEmpty()){ 
                    emailField2.setText(emailField3.getText());
                    emailField3.clear();;
                }
                emailsPane.getChildren().remove(emailField3);
            }
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
        emailField2.setEditable(false);
        emailField3.setEditable(false);
        numberField.setDisable(false);
        numberField2.setDisable(false);
        numberField3.setDisable(false);
        
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
        nameField.setEditable(true);
        surnameField.setEditable(true);
        emailField.setEditable(true);
        emailField2.setEditable(true);
        emailField3.setEditable(true);
        numberField.setDisable(true);
        numberField2.setDisable(true);
        numberField3.setDisable(true);
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
    private void onDeleteContact(ActionEvent event) throws IOException {
        Contact selectedContact = contactsTable.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/Confirm_popup.fxml")));
            Parent root = loader.load();
            ConfirmPopupController cpc = loader.getController();
            showPopup("Confirm_popup.fxml", "Conferma eliminazione");
            if (cpc.getChoice()) 
                addressBook.removeContact(selectedContact); 
        }
    }

    /**
     * @brief Salva contatto
     *
     * Guarda anche: onModifyContact()
     * Questo metodo permette, una volta cliccato il pulsante "Salva", di modificare/creare il contatto in visione dettagliata.
     *
     * @pre c'è almeno un nome o un cognome da salvare
     * 
     * @param[in] event
     * @see onModifyContact()
     */
    @FXML
    private void onSaveContact(ActionEvent event) throws IOException {
        Contact selectedContact = contactsTable.getSelectionModel().getSelectedItem();
        
        Contact contactToAdd = new Contact(nameField.getText(),surnameField.getText());
        String num[] = new String[3];
        if(! numberField3.getText().isEmpty())
            num[2] = numberField3.getText();
        if(! numberField2.getText().isEmpty())
            num[1] = numberField2.getText();
        if(! numberField.getText().isEmpty())
            num[0] = numberField.getText();
        contactToAdd.setNumbers(num);
            
        String mail[] = new String[3];
        if(! emailField3.getText().isEmpty())
            mail[2] = emailField3.getText();
        if(! emailField2.getText().isEmpty())
            mail[1] = emailField2.getText();
        if(! emailField.getText().isEmpty())
            mail[0] = emailField.getText();
        contactToAdd.setEmails(mail);
            
        //contactToAdd.setProfilePicture();
            
        System.out.println("Contatto selezionato:\n "+selectedContact+"\n\nContatto nuovo: \n"+contactToAdd);
        
        if (selectedContact != null){
            /* l'utente ha modificato il contatto */
            addressBook.removeContact(selectedContact);
        }       
        
        //addressBook.addContact(contactToAdd);
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
        Contact selectedContact = contactsTable.getSelectionModel().getSelectedItem();
        if (selectedContact == null){
            /* se è in fase di aggiunta */
            nameField.setText("");
            surnameField.setText("");
            
            if(! numberField3.getText().isEmpty()) numberField3.setText("");
            if(! numberField2.getText().isEmpty()) numberField2.setText("");
            numberField.setText("");
            
            if(! emailField3.getText().isEmpty()) emailField3.setText("");
            if(! emailField2.getText().isEmpty()) emailField2.setText("");
            emailField.setText("");
        }
        contactDetailsPane.setVisible(false);
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
    private void showImportPopup(ActionEvent event) throws IOException{
        this.showPopup("Import_popup.fxml", "Importa rubrica");
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
    private void showExportPopup(ActionEvent event) throws IOException {
        this.showPopup("Export_popup.fxml", "Esporta rubrica");
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
        showPopup("Config_popup.fxml", "Configurazione database");
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
    private void showManageTagsPopup(ActionEvent event) throws IOException {
        showPopup("ManageTags_popup.fxml", "Gestione tag",500,500);
    }

    /**
     * @brief Crea menù contestuale
     *
     * Questo metodo, eseguito solo in fase di inizializzazione della rubrica, crea il menù contestuale
     * @return Il menù contestuale
     */
    private ContextMenu createContextMenu(){
        ContextMenu contextMenu = new ContextMenu();

        // Opzione per ordinare per nome
        MenuItem sortByNameAsc = new MenuItem("Ordina per Nome (A-Z)");
        MenuItem sortByNameDesc = new MenuItem("Ordina per Nome (Z-A)");

        // Opzione per ordinare per cognome
        MenuItem sortByLastNameAsc = new MenuItem("Ordina per Cognome (A-Z)");
        MenuItem sortByLastNameDesc = new MenuItem("Ordina per Cognome (Z-A)");

        // Aggiungi MenuItem al ContextMenu
        contextMenu.getItems().addAll(sortByNameAsc, sortByNameDesc, new SeparatorMenuItem(), sortByLastNameAsc, sortByLastNameDesc, new SeparatorMenuItem());
        
        for(Tag t : addressBook.getAllTags()){
            CustomMenuItem tagItem = new CustomMenuItem(new CheckBox(t.getDescription()));
            tagItem.setHideOnClick(false);
            tagItem.setId(String.valueOf(t.getId()));
            contextMenu.getItems().add(tagItem);
        }

        return contextMenu;
    }
    
    @FXML
    private void showImagePopup(MouseEvent event) throws IOException {
        showPopup("Image_popup.fxml", "Gestione immagini",975,200);
        //profileImageView = getSelectedImage();
    } 
    
    //Metodi di utilità
    private void showPopup(String path, String title, double d1, double d2) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/" + path)));
        Scene scene = new Scene(root,d1,d2);

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle(title);
        popup.setResizable(false);
        popup.setScene(scene);
        popup.showAndWait();
    }
    private void showPopup(String path, String title) throws IOException {
        showPopup(path, title, 300, 250);
    }   
}
