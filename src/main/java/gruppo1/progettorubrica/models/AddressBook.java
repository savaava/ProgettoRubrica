package gruppo1.progettorubrica.models;

import java.io.Serializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

/**
 * @brief Tale classe rappresenta una rubrica telefonica, può contenere 0 o più contatti.
 * @see Contact
 */
public class AddressBook implements Serializable{
    private transient static AddressBook instance; ///< Unica istanza di AddressBook.
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

    
    /**
     * @brief Salva la rubrica telefonica nel file Data.bin in locale. 
     */
    public void saveOBJ() {

    }

   
    /**
     * @brief Carica la rubrica telefonica dal file Data.bin, aggiunge i contatti di quest'ultima nella rubrica corrente.
     * @return La rubrica telefonica caricata.
     */
    public static AddressBook loadOBJ() {
        return null;
    }

    
    /**
     * @brief Salva la rubrica telefonica sul database.
     */
    public void saveOnDB() {

    }

    /**
     * @brief Carica la rubrica telefonica dal database.
     * @return La rubrica telefonica caricata.
     */
    public static AddressBook loadFromDB() {
        return null;
    }
}
