package rubricaNoGit.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

/**
 * @brief Rappresenta una rubrica telefonica, può contenere 0 o più contatti.
 * @see Contact
 */
public class AddressBook {
    private static AddressBook instance; ///< Unica istanza di AddressBook.
    private ObservableList<Contact> contacts; ///< Lista dei contatti inseriti in rubrica.
    private ObservableSet<String> tags; ///< Insieme dei tag inseriti.
    private String dbUrl; ///< Link del database.

    /**
     * @brief Crea un'istanza della classe AddressBook.
     */
    private AddressBook() {
        this.contacts = FXCollections.observableArrayList();
        this.tags = FXCollections.observableSet();
    }

    /**
     * @brief Controlla se è stata creata l'istanza di AddressBook, altrimenti la crea.
     *
     * @return L'istanza creata o quella già esistente.
     */
    public static AddressBook getInstance() {
        // Crea l'oggetto solo se NON esiste:
        if (instance == null) {
            instance = new AddressBook();
        }
        return instance;
    }

    /**
     * @return La lista di contatti.
     */
    public ObservableList<Contact> getContacts() {
        return contacts;
    }

    /**
     * @brief Aggiunge un contatto alla rubrica telefonica.
     * 
     * @param[in] c Contatto da aggiungere.
     */
    public void addContact(Contact c) {

    }

    /**
     * @brief Rimuove la prima occorrenza del contatto.
     * @param[in] c Contatto da rimuovere.
     * @return Contatto rimosso.
     */
    public Contact removeContact(Contact c) {
        return null;
    }
    
    /**
     * 
     * @return L'insieme di tag inseriti. 
     */
    public ObservableSet<String> getTags() {
        return tags;
    }

    /**
     * @brief Aggiunge un tag.
     * @param[in] tag Tag da aggiungere.
     */
    public void addTag(String tag) {

    }

    /**
     * @brief Rimuove un tag.
     * @param[in] tag Tag da rimuovere.
     * @return Il tag rimosso.
     */
    public String removeTag(String tag) {
        return null;
    }

    /**
     * @brief Valorizza l'attributo dbUrl inserendo il link del database.
     * @param[in] dbUrl Link del database.
     */
    public void setDbUrl(String dbUrl) {

    }

    /**
     * @return Il link del database.
     */
    public String getDbUrl() {
        return dbUrl;
    }

    //SALVATAGGIO IN LOCALE

    //Salva Data.bin in memoria
    /**
     * @brief Esporta la rubrica telefonica nel file in locale. 
     * @param[in] filename File nella quale esportare la rubrica telefonica.
     */
    public void saveOBJ(String filename) {

    }

    //Carica Data.bin da file
    /**
     * @brief Importa una rubrica telefonica da un file, aggiunge i contatti di quest'ultima nella rubrica corrente.
     * @param[in] filename File da cui importare la rubrica telefonica.
     * @return La rubrica telefonica importata.
     */
    public static AddressBook loadOBJ(String filename) {
        return null;
    }

    //SALVATAGGIO SU DATABASE
    /**
     * @brief Esporta la rubrica telefonica sul database.
     */
    public void saveOnDB() {

    }

    /**
     * @brief Importa una rubrica telefonica dal database.
     * @return La rubrica telefonica importata.
     */
    public static AddressBook loadFromDB() {
        return null;
    }
}
