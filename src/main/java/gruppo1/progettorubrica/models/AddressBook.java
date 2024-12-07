package gruppo1.progettorubrica.models;

import gruppo1.progettorubrica.services.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @brief Tale classe rappresenta una rubrica telefonica, può contenere 0 o più contatti.
 * @see Contact
 */
public class AddressBook implements TagManager, ContactManager {
    private static AddressBook instance; ///< Unica istanza di AddressBook.
    private final ObservableList<Contact> contacts; ///< Lista dei contatti inseriti in rubrica.
    private final ObservableList<Tag> tags; ///< Insieme dei tag inseriti.
    private String dbUrl; ///< Link del database.
    private Database db;

    /**
     * @brief Crea un'istanza della classe AddressBook.
     */
    private AddressBook() {
        this.contacts = FXCollections.observableArrayList();
        this.tags = FXCollections.observableArrayList();
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

    
    @Override
    public ObservableList<Contact> getAllContacts() {
        return contacts;
    }

    
    @Override
    public void addContact(Contact c) {

    }

    
    @Override
    public Contact removeContact(Contact c) {
        return null;
    }

    @Override
    public ObservableList<Tag> getAllTags() {
        return tags;
    }

    @Override
    public Tag getTag(int id) {
        return null;
    }

    @Override
    public void addTag(Tag tag) {

    }

    @Override
    public Tag removeTag(Tag tag) {
        return null;
    }

    /**
     * @brief Valorizza l'attributo dbUrl inserendo il link del database.
     * @param[in] dbUrl Link del database.
     */
    public void setDBUrl(String dbUrl) {

    }

    /**
     * @return Il link del database.
     */
    public String getDBUrl() {
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
     * @brief Elimina il file Data.bin.
     */
    public void removeOBJ() {

    }

    
    /**
     * @brief Salva la rubrica telefonica sul database.
     */
    public void saveToDB() {

    }

    /**
     * @brief Carica la rubrica telefonica dal database.
     * @return La rubrica telefonica caricata.
     */
    public AddressBook loadFromDB() {
        return null;
    }

    /**
     * @brief Carica la configurazione da Config.bin, se presente
     */
    public void loadConfig() {

    }

    /**
     * @brief Salva la configurazione su Config.bin
     */
    public void saveConfig() {

    }

    /**
     * @brief Inizializza il database
     * @pre È valorizzato il campo dbUrl
     * @post L'attributo db contiene l'istanza del database
     */
    public void initDB() {

    }
}
