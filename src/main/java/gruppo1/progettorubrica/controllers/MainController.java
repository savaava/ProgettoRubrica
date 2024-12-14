package gruppo1.progettorubrica.controllers;

import gruppo1.progettorubrica.models.AddressBook;
import gruppo1.progettorubrica.models.Contact;
import gruppo1.progettorubrica.models.Tag;
import java.awt.image.BufferedImage;

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
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;

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
    private GridPane numbersPane, emailsPane, tagsPane;  ///< GridPane appartenente a {@link MainController#contactsDetailsPane} che contiene gli eventuali numeri di telefono, emails e tag
    
    @FXML
    private ChoiceBox<String> choiceBoxTag;
    
    private ChoiceBox<String> choiceBoxTag2,choiceBoxTag3;
    
    private ContextMenu contextMenu;  ///< menù per importare, esportare e configurare database

    private FilteredList<Contact> filteredContacts;  ///< lista filtrata in base a tag e/o alla sottostringa presente in searchField

    private TextField numberField2,numberField3,emailField2,emailField3;
    
    private String pathsImages[] = {"/images/base_profile.jpg",
                                    "/images/FotoProfilo1.jpg",
                                    "/images/FotoProfilo2.jpg",
                                    "/images/FotoProfilo3.jpg",
                                    "/images/FotoProfilo4.jpg"};
    
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
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore di caricamento");
            alert.setHeaderText("Impossibile caricare AddressBook");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }

        //Nasconde la visione dettagliata
        this.contactDetailsPane.setVisible(false);

        this.filteredContacts = new FilteredList<>(addressBook.getAllContacts());

        //Riempie la tabella con i contatti
        this.contactsTable.setItems(filteredContacts);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        //Crea il menù contestuale
        this.contextMenu = this.createContextMenu();
     
        //Imposta immagine contatto circolare
        this.setImageCircle();

        //Imposta immagine imbuto
        filterImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/filter.png"))));

        numberField2  = new TextField();
        numberField3  = new TextField();
        emailField2   = new TextField();
        emailField3   = new TextField();
        choiceBoxTag2 = new ChoiceBox<>();
        choiceBoxTag3 = new ChoiceBox<>();
               
        profileImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathsImages[0]))));

        searchFieldBinding();
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
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            onViewUpdate();
        });
    }

    private void onViewUpdate() {
        filteredContacts.setPredicate(contact -> {
            boolean matchesText =
                    searchField.getText().isEmpty() ||
                    contact.getName().toLowerCase().contains(searchField.getText().toLowerCase()) ||
                    contact.getSurname().toLowerCase().contains(searchField.getText().toLowerCase());

            FilteredList<MenuItem> selectedTagItems = contextMenu.getItems().filtered(e -> e.getId() != null && ((CheckBox) ((CustomMenuItem) e).getContent()).isSelected());

            boolean matchesTag = selectedTagItems.isEmpty() || selectedTagItems.stream().anyMatch(e -> contact.getAllTagIndexes().contains(Integer.parseInt(e.getId())));

            return matchesText && matchesTag;
        });
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
        MenuItem m = new MenuItem("Filtri");
        m.setDisable(true);
        if(contextMenu.getItems().isEmpty())
            contextMenu.getItems().add(m);
        contextMenu.show(contactDetailsPane, mouseEvent.getScreenX(), mouseEvent.getScreenY());
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
        BooleanBinding op1 = nameField.textProperty().isEmpty();
        BooleanBinding op2 = surnameField.textProperty().isEmpty();
        saveButton.disableProperty().bind(op1.and(op2));
        
        this.contactDetailsPane.setVisible(true);
        emailsPane.setVisible(true);
        numbersPane.setVisible(true);
        tagsPane.setVisible(true);
        
        deleteButton.setDisable(true);
        editButton.setDisable(true);
        
        nameField.setEditable(true);
        nameField.clear();
        surnameField.setEditable(true);
        surnameField.clear();
        emailField.setEditable(true);
        emailField2.setEditable(true);
        emailField3.setEditable(true);
        numberField.setEditable(true);
        numberField2.setEditable(true);
        numberField3.setEditable(true);
        
        emailField.clear();
        emailField2.clear();
        emailField3.clear();
        numberField.clear();
        numberField2.clear();
        numberField3.clear();
        choiceBoxTag.setValue("Nessuno");
        choiceBoxTag2.setValue("Nessuno");
        choiceBoxTag3.setValue("Nessuno");
        
        //Imposta immagine profilo di default
        profileImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathsImages[0]))));
        profileImageView.setMouseTransparent(false);
        
        //appena si inserisce 1 carattere nel primo numberTextField, compare il secondo TextField
        numberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && !numbersPane.getChildren().contains(numberField2)){
                numbersPane.add(numberField2, 1, 1);
            }else if(newValue.isEmpty()) {//se ha cancellato il contenuto del primo TextField devo rimuovere il secondo
                if(!numberField2.getText().isEmpty()){ //prima di rimuovere il secondo TextField vedo se c'è scritto qualcosa e lo inserisco nel primo e cancello quanto scritto nel secondo
                    numberField.setText(numberField2.getText());
                    numberField2.clear();
                }else{
                    numbersPane.getChildren().remove(numberField2);
                }
            }
        });
        
        numberField2.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && !numbersPane.getChildren().contains(numberField3))
                numbersPane.add(numberField3, 1, 2);
            else if(newValue.isEmpty()){
                if(!numberField3.getText().isEmpty()){
                    numberField2.setText(numberField3.getText());
                    numberField3.clear();
                }else{
                    numbersPane.getChildren().remove(numberField3);
                }
            }
        });
        
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && !emailsPane.getChildren().contains(emailField2))
                emailsPane.add(emailField2, 1, 1);
            else if(newValue.isEmpty()){
                if(!emailField2.getText().isEmpty()){
                    emailField.setText(emailField2.getText());
                    emailField2.clear();
                }else{
                    emailsPane.getChildren().remove(emailField2);
                }
            }
        });
        
        emailField2.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && !emailsPane.getChildren().contains(emailField3))
                emailsPane.add(emailField3, 1, 2);
            else if(newValue.isEmpty()){
                if(!emailField3.getText().isEmpty()){
                    emailField2.setText(emailField3.getText());
                    emailField3.clear();
                }else{
                    emailsPane.getChildren().remove(emailField3);
                }
            }
        });
        
        Collection<String> descriptionTags = new ArrayList<>();
        descriptionTags.add("Nessuno");
        for(Tag tag : addressBook.getAllTags())
            descriptionTags.add(tag.getDescription());
        choiceBoxTag.getItems().setAll(descriptionTags);
        choiceBoxTag2.getItems().setAll(descriptionTags);
        choiceBoxTag3.getItems().setAll(descriptionTags);
        
        choiceBoxTag.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals("Nessuno") && !tagsPane.getChildren().contains(choiceBoxTag2)){
                tagsPane.add(choiceBoxTag2, 1, 1);
            }else if(newValue.equals("Nessuno")){
                if(!choiceBoxTag2.getValue().equals("Nessuno")){
                    choiceBoxTag.setValue(choiceBoxTag2.getValue());
                    choiceBoxTag2.setValue("Nessuno");
                }else{
                    tagsPane.getChildren().remove(choiceBoxTag2);
                }
            }
        });
        
        choiceBoxTag2.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals("Nessuno") && !tagsPane.getChildren().contains(choiceBoxTag3)){
                tagsPane.add(choiceBoxTag3, 1, 2);
            }else if(newValue.equals("Nessuno")){
                if(!choiceBoxTag3.getValue().equals("Nessuno")){
                    choiceBoxTag2.setValue(choiceBoxTag3.getValue());
                    choiceBoxTag3.setValue("Nessuno");
                }else{
                    tagsPane.getChildren().remove(choiceBoxTag3);
                }
            }
        });
        
        emailField.clear();
        emailField2.clear();
        emailField3.clear();
        numberField.clear();
        numberField2.clear();
        numberField3.clear();
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
        Contact selectedContact=contactsTable.getSelectionModel().getSelectedItem();

        if(selectedContact == null)
            return;
       
        System.out.println("CONTATTO SELEZIONATO:\n"+selectedContact);
       
        this.contactDetailsPane.setVisible(true);
        tagsPane.setVisible(false);
        
        saveButton.disableProperty().unbind();
        saveButton.setDisable(true);
        deleteButton.setDisable(false);
        editButton.setDisable(false);
        
        nameField.setEditable(false);
        surnameField.setEditable(false);
        emailField.setEditable(false);
        emailField2.setEditable(false);
        emailField3.setEditable(false);
        numberField.setEditable(false);
        numberField2.setEditable(false);
        numberField3.setEditable(false);
        
        profileImageView.setMouseTransparent(true);
        
        emailField.clear();
        emailField2.clear();
        emailField3.clear();
        numberField.clear();
        numberField2.clear();
        numberField3.clear();
        
        byte image[] = toPrimitive(selectedContact.getProfilePicture());
        profileImageView.setImage(new Image(new ByteArrayInputStream(image)));
        
        nameField.setText(selectedContact.getName());
        surnameField.setText(selectedContact.getSurname());
        
        String emails[]=selectedContact.getEmails();
        if(emails[0] == null){
            emailsPane.setVisible(false);
        }else{
            emailsPane.setVisible(true);
            emailField.setText(emails[0]);
            if(emails[1]!=null){
                emailsPane.getChildren().remove(emailField2);
                emailField2.setText(emails[1]);
                emailsPane.add(emailField2, 1, 1);
            }
            if(emails[2]!=null){
                emailsPane.getChildren().remove(emailField3);
                emailField3.setText(emails[2]);
                emailsPane.add(emailField3, 1, 2);
            }
        }
        
        String numbers[]=selectedContact.getNumbers();
        if(numbers[0] == null){
            numbersPane.setVisible(false);
        }else{
            numbersPane.setVisible(true);
            numberField.setText(numbers[0]);
            if(numbers[1]!=null){
            numbersPane.getChildren().remove(numberField2);
            numberField2.setText(numbers[1]);
            numbersPane.add(numberField2, 1, 1);
        }
        if(numbers[2]!=null){
            numbersPane.getChildren().remove(numberField3);
            numberField3.setText(numbers[2]);
            numbersPane.add(numberField3, 1, 2);
        }
        
        for(Integer id: selectedContact.getAllTagIndexes())
            tagVBox.getChildren().add(new Label(addressBook.getTag(id).getDescription()));
        }
    }

    /**
     * @brief Per modificare il contatto
     *
     * Questo metodo permette, una volta cliccato il pulsante "Modifica", di modificare il contatto in visione dettagliata.
     * Ma non è finalizzato al salvataggio del contatto modficato (operazione delegata a onSave)
     * 
     * @see onSaveContact
     * @param[in] event
     */
    @FXML
    private void onModifyContact(ActionEvent event) {
        BooleanBinding op1 = nameField.textProperty().isEmpty();
        BooleanBinding op2 = surnameField.textProperty().isEmpty();
        saveButton.disableProperty().bind(op1.and(op2));
        
        deleteButton.setDisable(true);
        editButton.setDisable(true);        
        saveButton.setVisible(true);        
        
        emailsPane.setVisible(true);
        numbersPane.setVisible(true);
        tagsPane.setVisible(true);
        
        nameField.setEditable(true);
        surnameField.setEditable(true);
        emailField.setEditable(true);
        emailField2.setEditable(true);
        emailField3.setEditable(true);
        numberField.setEditable(true);
        numberField2.setEditable(true);
        numberField3.setEditable(true);
        
        profileImageView.setMouseTransparent(false);
        
        numberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && !numbersPane.getChildren().contains(numberField2)){
                numbersPane.add(numberField2, 1, 1);
            }else if(newValue.isEmpty()) {//se ha cancellato il contenuto del primo TextField devo rimuovere il secondo
                if(!numberField2.getText().isEmpty()){ //prima di rimuovere il secondo TextField vedo se c'è scritto qualcosa e lo inserisco nel primo e cancello quanto scritto nel secondo
                    numberField.setText(numberField2.getText());
                    numberField2.clear();
                }else{
                    numbersPane.getChildren().remove(numberField2);
                }
            }
        });
        
        numberField2.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && !numbersPane.getChildren().contains(numberField3))
                numbersPane.add(numberField3, 1, 2);
            else if(newValue.isEmpty()){
                if(!numberField3.getText().isEmpty()){ 
                    numberField2.setText(numberField3.getText());
                    numberField3.clear();
                }else{
                    numbersPane.getChildren().remove(numberField3);
                }
            }
        });
        
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && !emailsPane.getChildren().contains(emailField2))
                emailsPane.add(emailField2, 1, 1);
            else if(newValue.isEmpty()){
                if(!emailField2.getText().isEmpty()){ 
                    emailField.setText(emailField2.getText());
                    emailField2.clear();
                }else{
                    emailsPane.getChildren().remove(emailField2);
                }
            }
        });
        
        emailField2.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() && !emailsPane.getChildren().contains(emailField3))
                emailsPane.add(emailField3, 1, 2);
            else if(newValue.isEmpty()){
                if(!emailField3.getText().isEmpty()){ 
                    emailField2.setText(emailField3.getText());
                    emailField3.clear();
                }else{
                    emailsPane.getChildren().remove(emailField3);
                }
            }
        });
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
        Scene scene = new Scene(root, 300, 200);
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Conferma");
        popup.setResizable(false);
        popup.setScene(scene);
        popup.showAndWait();

        if (cpc.getChoice()){
            addressBook.removeContact(selectedContact);
            this.contactDetailsPane.setVisible(false);
        }
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
        
        Byte[] imageBytes = ImageViewToByteArray(profileImageView);
        contactToAdd.setProfilePicture(imageBytes);

        if (selectedContact != null){
            /* l'utente ha modificato il contatto */
            addressBook.removeContact(selectedContact);
        }       
        
        addressBook.addContact(contactToAdd);
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
        /* l'utente annulla l'aggiunta o la modifica di un contatto */
        contactDetailsPane.setVisible(false);
        nameField.clear();
        surnameField.clear();
        emailField.clear();
        emailField2.clear();
        emailField3.clear();
        numberField.clear();
        numberField2.clear();
        numberField3.clear();        
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

        for(Tag t : addressBook.getAllTags()){
            CheckBox cb = new CheckBox(t.getDescription());
            cb.selectedProperty().addListener((observable, oldValue, newValue) -> {
               onViewUpdate();
            });

            CustomMenuItem tagItem = new CustomMenuItem();
            tagItem.setHideOnClick(false);
            tagItem.setId(String.valueOf(t.getId()));
            contextMenu.getItems().add(tagItem);
        }
        return contextMenu;
    }
    
    @FXML
    private void showImagePopup(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/Image_popup.fxml")));
        Parent root = loader.load();
        ImagePopupController ImageController = loader.getController();
                
        Scene scene = new Scene(root, 975, 250);
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Gestione immagini");
        popup.setResizable(false);
        popup.setScene(scene);
        popup.showAndWait();
        
        if(ImageController.getImageIndex() == 5){
            File selectedImageFile = ImageController.getSelectedImage();
            Image image = new Image(selectedImageFile.toURI().toString());
            profileImageView.setImage(image);
            return ;
        }
        
        if(ImageController.getImageIndex() == -1)
            return;
        
        String pathImage = pathsImages[ImageController.getImageIndex()];
        profileImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathImage))));
    } 
    
    /* Metodi di utilità */
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
    
    private Byte[] toWrapper(byte[] byteArray) {
        Byte[] byteObjects = new Byte[byteArray.length];
        for(int i=0; i<byteArray.length; i++) {
            byteObjects[i] = byteArray[i];
        }
        return byteObjects;
    }
    private byte[] toPrimitive(Byte[] byteObjectArray) {
        byte[] byteArray = new byte[byteObjectArray.length];
        for(int i=0; i<byteObjectArray.length; i++) {
            byteArray[i] = byteObjectArray[i];
        }
        return byteArray;
    }
    private Byte[] ImageViewToByteArray(ImageView imageView) throws IOException {
        // Ottieni l'immagine dall'ImageView
        Image fxImage = imageView.getImage();

        if (fxImage == null) {
            throw new IOException("L'ImageView non contiene nessuna immagine.");
        }

        // Converti l'immagine JavaFX in BufferedImage
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);

        // Scrivi l'immagine in un ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

        // Ottieni l'array di byte
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        // Converti il byte[] in Byte[]
        return toWrapper(byteArray);
    }
}
