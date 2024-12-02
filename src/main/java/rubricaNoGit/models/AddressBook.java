package rubricaNoGit.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

public class AddressBook {
    private static AddressBook instance;
    private ObservableList<Contact> contacts;
    private ObservableSet<String> tags;
    private String dbUrl;

    private AddressBook() {
        this.contacts = FXCollections.observableArrayList();
        this.tags = FXCollections.observableSet();
    }

    public static AddressBook getInstance() {
        // Crea l'oggetto solo se NON esiste:
        if (instance == null) {
            instance = new AddressBook();
        }
        return instance;
    }

    public ObservableList<Contact> getContacts() {
        return contacts;
    }

    public void addContact(Contact c) {

    }

    //rimuove la prima occorrenza del contatto
    public Contact removeContact(Contact c) {
        return null;
    }

    public ObservableSet<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {

    }

    public String removeTag(String tag) {
        return null;
    }

    public void setDbUrl(String dbUrl) {

    }

    public String getDbUrl() {
        return dbUrl;
    }

    //SALVATAGGIO IN LOCALE

    //Salva Data.bin in memoria
    public void saveOBJ(String filename) {

    }

    //Carica Data.bin da file
    public static AddressBook loadOBJ(String filename) {
        return null;
    }

    //SALVATAGGIO SU DATABASE

    public void saveOnDB() {

    }

    public static AddressBook loadFromDB() {
        return null;
    }
}
