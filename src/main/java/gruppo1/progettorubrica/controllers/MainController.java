package gruppo1.progettorubrica.controllers;

import gruppo1.progettorubrica.models.AddressBook;
import gruppo1.progettorubrica.models.Contact;
import gruppo1.progettorubrica.models.Tag;
import gruppo1.progettorubrica.services.Converter;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

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
    
    private ContextMenu contextMenu;  ///< menù per esportare e configurare database

    private FilteredList<Contact> filteredContacts;  ///< lista filtrata in base a tag e/o alla sottostringa presente in searchField

    private TextField numberField2,numberField3,emailField2,emailField3;
    
    private String pathsImages[] = {"/images/base_profile.jpg",
                                    "/images/FotoProfilo1.jpg",
                                    "/images/FotoProfilo2.jpg",
                                    "/images/FotoProfilo3.jpg",
                                    "/images/FotoProfilo4.jpg"};
    
    private String pathImage;
    
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

        // Crea una SortedList dalla FilteredList
        SortedList<Contact> sortedContacts = new SortedList<>(this.filteredContacts);

        // Associa il comparatore della SortedList al comparatore della TableView
        sortedContacts.comparatorProperty().bind(this.contactsTable.comparatorProperty());

        // Riempie la tabella con i contatti ordinabili
        this.contactsTable.setItems(sortedContacts);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        //Sorting per cognome di default
        contactsTable.getSortOrder().add(surnameColumn);

        //Crea il menù contestuale
        this.contextMenu = this.createContextMenu();
     
        //Imposta immagine contatto circolare
        this.setImageCircle();

        //Imposta immagine imbuto
        filterImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/filter.png"))));

        //Gestisce larghezza delle colonne
        nameColumn.prefWidthProperty().bind(contactsTable.widthProperty().divide(2));
        surnameColumn.prefWidthProperty().bind(contactsTable.widthProperty().divide(2));

        numberField2  = new TextField();
        numberField3  = new TextField();
        emailField2   = new TextField();
        emailField3   = new TextField();
        
        pathImage = pathsImages[0];
        profileImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathImage))));

        //Gestione eventi
        choiceBoxTag.setOnAction(this::onChoiceBoxSelected);

        //Set font size
        tagVBox.setStyle("-fx-font-size: 16px;");

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

            FilteredList<MenuItem> selectedTagItems = contextMenu.getItems().filtered(e -> "rb".equals(((RadioButton) ((CustomMenuItem) e).getContent()).getId()) && ((RadioButton) ((CustomMenuItem) e).getContent()).isSelected());

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
        this.contextMenu.show(filterImage, mouseEvent.getScreenX(), mouseEvent.getScreenY());  
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
        contactsTable.getSelectionModel().clearSelection();
        
        BooleanBinding op1 = nameField.textProperty().isEmpty();
        BooleanBinding op2 = surnameField.textProperty().isEmpty();
        saveButton.disableProperty().bind(op1.and(op2));
        
        this.contactDetailsPane.setVisible(true);
        emailsPane.setVisible(true);
        numbersPane.setVisible(true);
        tagsPane.setVisible(true);

        tagVBox.getChildren().clear();
        
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
        
        choiceBoxTag.getItems().clear();

        for(Tag tag : addressBook.getAllTags()) {
            String description = tag.getDescription();
            choiceBoxTag.getItems().add(description);
        }
        
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

        if(selectedContact == null) return;
       
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

        if(selectedContact.getProfilePicture() != null){
            byte[] image = Converter.toPrimitive(selectedContact.getProfilePicture());
            profileImageView.setImage(new Image(new ByteArrayInputStream(image)));
        }else{
            profileImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathsImages[0]))));
        }
        
        nameField.setText(selectedContact.getName());
        surnameField.setText(selectedContact.getSurname());
        
        String[] emails = selectedContact.getEmails();
        emailsPane.setVisible(emails[0] != null && !emails[0].isEmpty());
        emailField.setText(emails[0] != null ? emails[0] : "");
        emailsPane.getChildren().removeAll(emailField2, emailField3);

        if (emails[1] != null && !emails[1].isEmpty()) {
            emailField2.setText(emails[1]);
            emailsPane.add(emailField2, 1, 1);
        } else {
            emailField2.clear();
            emailsPane.getChildren().remove(emailField2);
        }

        if (emails[2] != null && !emails[2].isEmpty()) {
            emailField3.setText(emails[2]);
            emailsPane.add(emailField3, 1, 2);
        } else {
            emailField3.clear();
            emailsPane.getChildren().remove(emailField3);
        }
        
        String[] numbers = selectedContact.getNumbers();
        numbersPane.setVisible(numbers[0] != null && !numbers[0].isEmpty());
        numberField.setText(numbers[0] != null ? numbers[0] : "");
        numbersPane.getChildren().removeAll(numberField2, numberField3);

        if (numbers[1] != null && !numbers[1].isEmpty()) {
            numberField2.setText(numbers[1]);
            numbersPane.add(numberField2, 1, 1);
        } else {
            numberField2.clear();
            numbersPane.getChildren().remove(numberField2);
        }

        if (numbers[2] != null && !numbers[2].isEmpty()) {
            numberField3.setText(numbers[2]);
            numbersPane.add(numberField3, 1, 2);
        } else {
            numberField3.clear();
            numbersPane.getChildren().remove(numberField3);
        }
        
        tagVBox.getChildren().clear();
        for(int i : selectedContact.getAllTagIndexes()){
            Tag tag = addressBook.getTag(i);
            if(tag == null) {
                selectedContact.removeTagIndex(i);
                continue;
            }
            Label tagLabel = new Label(tag.getDescription());
            tagLabel.setId(String.valueOf(i));
            tagLabel.setOnMouseClicked(e -> {
                if(!nameField.isEditable()) return;
                tagVBox.getChildren().remove(tagLabel);
            });
            tagVBox.getChildren().add(tagLabel);
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
        Contact selectedContact=contactsTable.getSelectionModel().getSelectedItem();

        if(selectedContact == null)
            return;
        
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

        choiceBoxTag.getItems().clear();

        for(Tag tag : addressBook.getAllTags()) {
            String description = tag.getDescription();
            choiceBoxTag.getItems().add(description);
        }
        
    }

    @FXML
    private void onChoiceBoxSelected(ActionEvent event) {
        String selectedTag = choiceBoxTag.getValue();
        if(selectedTag == null) return;
        
        Tag tag = addressBook.getTag(selectedTag);
        if(tag == null) return;

        if(tagVBox.getChildren().filtered(e -> ((Label) e).getText().equals(selectedTag)).size() > 0 || tagVBox.getChildren().size() == 3){
            choiceBoxTag.getSelectionModel().clearSelection();
            return;
        };
        
        Label tagLabel = new Label(selectedTag);
        tagLabel.setId(String.valueOf(tag.getId()));
        tagLabel.setOnMouseClicked(e -> {
            if(!nameField.isEditable()) return;
            tagVBox.getChildren().remove(tagLabel);
        });
        tagVBox.getChildren().add(tagLabel);

        choiceBoxTag.getSelectionModel().clearSelection();
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

        numberField.textProperty().unbind();
        numberField2.textProperty().unbind();
        emailField.textProperty().unbind();
        emailField2.textProperty().unbind();
        
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
        
        if(!pathImage.equals(pathsImages[0])){
            Byte[] imageBytes = Converter.imageViewToByteArray(profileImageView);
            contactToAdd.setProfilePicture(imageBytes);
        }

        tagVBox.getChildren().forEach(e -> {
            Label tagLabel = (Label) e;
            contactToAdd.addTagIndex(Integer.parseInt(tagLabel.getId()));
        });

        
        if (selectedContact != null){
            contactToAdd.setId(selectedContact.getId());
        }
        
        addressBook.addContact(contactToAdd);
        
        contactDetailsPane.setVisible(false);

        TableColumn<Contact, ?> c = contactsTable.getSortOrder().get(0);

        contactsTable.getSortOrder().clear();
        contactsTable.getSortOrder().add(c);
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
        this.contextMenu = createContextMenu();  //ricreo il contextMenu per aggiornarla con i tag eventualmente aggiunti dopo l'apertura della rubrica
    }

    /**
     * @brief Crea menù contestuale
     *
     * Questo metodo, eseguito in fase di inizializzazione della rubrica o da {@link MainController#contactsTable}, crea il menù contestuale
     * @return Il menù contestuale
     */
    private ContextMenu createContextMenu(){
        ContextMenu contextMenu = new ContextMenu();
        ToggleGroup n = new ToggleGroup();

        //Aggiungo il radiobutton "Tutti" per visualizzare tutti i contatti
        RadioButton r = new RadioButton("Tutti");
        r.setToggleGroup(n);
        r.setSelected(true);
        r.selectedProperty().addListener((observable, oldValue, newValue) -> {
            onViewUpdate();
        });
        contextMenu.getItems().add(new CustomMenuItem(r));

        //Aggiungo i radiobutton per visualizzare i contatti con un tag specifico
        for(Tag t : addressBook.getAllTags()){
            RadioButton cb = new RadioButton(t.getDescription());
            cb.setToggleGroup(n);
            cb.setId("rb");
            cb.selectedProperty().addListener((observable, oldValue, newValue) -> {
               onViewUpdate();
            });

            CustomMenuItem tagItem = new CustomMenuItem(cb);
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
        ImagePopupController imageController = loader.getController();
                
        Scene scene = new Scene(root, 975, 250);
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Gestione immagini");
        popup.setResizable(false);
        popup.setScene(scene);
        popup.showAndWait();
        
        if(imageController.getImageIndex() == -1) {
            pathImage = pathsImages[imageController.getImageIndex()];
            return;
        };
        
        if(imageController.getImageIndex() == 5){
            File selectedImageFile = imageController.getSelectedImage();
            Image image = new Image(selectedImageFile.toURI().toString());
            profileImageView.setImage(image);
            pathImage = selectedImageFile.toURI().toString();
            return ;
        }
        
        pathImage = pathsImages[imageController.getImageIndex()];
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
}