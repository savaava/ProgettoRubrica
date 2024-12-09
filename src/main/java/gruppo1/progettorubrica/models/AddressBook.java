package gruppo1.progettorubrica.models;

import gruppo1.progettorubrica.services.Database;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @brief Modella una rubrica telefonica, può contenere 0 o più contatti.
 * @see Contact
 * @see Tag
 * @see Database
 */
public class AddressBook implements TagManager, ContactManager {
    private static AddressBook instance; ///< Unica istanza di AddressBook.
    private final ObservableList<Contact> contacts; ///< Lista dei contatti inseriti in rubrica.
    private final ObservableList<Tag> tags; ///< Insieme dei tag inseriti.
    private String dbUrl; ///< Link del database.
    private Database db;

    /**
     * @brief Costruttore privato che crea un'istanza della classe AddressBook.
     * @pre nessuno
     * @post crea un'istanza di AddressBook in 4 casi diversi:
     * 1. esiste un link valido per il database in Config.bin ed esiste anche un file Data.bin;
     * 2. esiste solo il link valido per il database;
     * 3. esiste solo il file Data.bin;
     * 4. non esiste nè il DB, nè il file (è l'apertura iniziale).
     */
    private AddressBook() {
        this.contacts = FXCollections.observableArrayList();
        this.tags = FXCollections.observableArrayList();
        
        if(new File("Config.bin").exists())
            loadConfig(); /* ci fornisce l'informazione per cui è presente o meno il DB */
        
        if(Database.verifyDBUrl(dbUrl) && new File("Data.bin").exists()) {
            /* esiste sia un database valido che un file Data.bin con i contatti */
            initDB();
            dataToDB();
            removeOBJ();
            loadFromDB();
        }else if(Database.verifyDBUrl(dbUrl)){
            /* esiste un database valido funzionante quindi lo associamo ad AddressBook */
            initDB();
            loadFromDB();
        }else if(new File("Data.bin").exists()){
            loadOBJ();
        }
    }

    /**
     * @brief Controlla se è stata creata l'istanza di AddressBook, altrimenti la crea.
     * @pre nessuna
     * @post il client ottiene l'istanza dell'unica rubrica, se esisteva già ritorna l'istanza stessa, se non 
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
        contacts.add(c);
    }

    
    @Override
    public void removeContact(Contact c) {
        contacts.remove(c);
    }

    @Override
    public ObservableList<Tag> getAllTags() {
        return tags;
    }

    @Override
    public Tag getTag(int id) {
        for(Tag t : tags){
            if(t.getId() == id)
                return t;
        }
        return null;
    }

    @Override
    public void addTag(Tag tag) {
        tags.add(tag);
    }

    @Override
    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    /**
     * @brief Valorizza l'attributo dbUrl inserendo il link del database.
     * @param[in] dbUrl Link del database.
     * @pre Il client fornisce una stringa qualsiasi, che dovrebbe essere il link al suo DB.
     * @post l'istanza AddressBook possiede ora questo parametro in ingresso come il link del DB.
     */
    public void setDBUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    /**
     * @pre nessuna
     * @post il client ottiene l'url del db, null se non esiste
     * @return Il link del database.
     */
    public String getDBUrl() {
        return dbUrl;
    }

    
    /**
     * @brief Salva la rubrica telefonica nel file Data.bin in locale. 
     * @pre ci possono essere 0 o più contatti e tags e può non esistere il file Data.bin
     * @post carica i contatti e i tag presenti dal file Data.bin se non lancia exception
     * @@throws  IOException se il file non esiste o se non si riesce a fare la lettura.
     */
    public void saveOBJ() {
        System.out.println("Scrittura fiel Data.bin ...");
        
        /* FORMATO fiel Data.bin:
        listacontatti listatags
        */
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Data.bin")))){
            oos.writeObject(new ArrayList<>(contacts));
            oos.writeObject(new ArrayList<>(tags));
        }catch(IOException ex){
            System.out.print("ECCEZIONE in scrittura serializzata del file Data.bin -> ");
            ex.printStackTrace();
        }
    }

   
    /**
     * @brief Carica la rubrica telefonica dal file Data.bin, aggiunge i contatti di quest'ultima nella rubrica corrente.
     * @throws  IOException se non trova il file Data.bin o se non riesce a caricare il contenuto
     * @throws  ClassNotFoundException se non riesce a convertire il contenuto del file in una lista di Contact o Tag
     * @pre nessuna
     * @post carica i contatti e i tag presenti dal file Data.bin se non lancia exception
     */
    public void loadOBJ() {
        System.out.println("Lettura file Data.bin ...");
        
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("Data.bin")))){
            try {
                contacts.setAll((Collection<Contact>)ois.readObject());
                tags.setAll((Collection<Tag>)ois.readObject());
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }catch(IOException ex){
            System.out.println("ECCEZIONE in lettura serializzata del file Data.bin -> ");
            ex.printStackTrace();
        }
    }

    /**
     * @brief Elimina il file Data.bin.
     * @pre può essere presente o meno il file Data.bin. 
     * @post il file di salvataggio dati Data.bin viene eliminato.
     */
    public void removeOBJ() {
        new File("Data.bin").delete();
    }

    
    /**
     * @brief Salva la rubrica telefonica sul database.
     * @pre esiste il database ed è funzionante.
     * @post salva sul database tutti i contatti e tags.
     */
    public void saveToDB() {
        db.insertManyContacts(contacts);
        db.insertManyTags(tags);
    }
    
    /**
     * @brief trasferisce tutti contatti e tag del file Data.bin sul DB
     * @param[in] filename è il percorso del file Data.bin
     * @pre esiste il DB ed è funzionante
     * @pre esiste il file Data.bin con contatti e tags
     * @post tutti i contatti e tags del file Data.bin vengono inseriti nel DB
     */
    public void dataToDB(){
        System.out.println("Trasferimento Data.bin sul DB ...");
        
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("Data.bin")))){
            try {
                db.insertManyContacts((Collection<Contact>)ois.readObject());
                db.insertManyTags((Collection<Tag>)ois.readObject());
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }catch(IOException ex){
            System.out.println("ECCEZIONE in lettura serializzata del file Data.bin -> ");
            ex.printStackTrace();
        }
    }

    /**
     * @brief Carica i contatti e tags dal database.
     * @pre il database esiste ed è funzionante e contiene eventuali contatti o tags.
     * @post le liste dei contatti e dei tags sono valorizzate ai nuovi valori presi dal DB.
     */
    public void loadFromDB() {
        contacts.setAll(db.getAllContacts());
        tags.setAll(db.getAllTags());
    }

    /**
     * @brief Carica la configurazione da Config.bin, se presente
     * @pre 
     * @throws IOException se non lo trova o se non riesce a leggere il file
     */
    public void loadConfig() {
        System.out.println("Lettura del file Config.bin ...");
        
        try(DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream("Config.bin")))){
            dbUrl = dis.readUTF();
        }catch(IOException ex){
            System.out.print("ECCEZIONE in lettura del file Config.bin -> ");
            ex.printStackTrace();
        }
    }

    /**
     * @brief Salva la configurazione su Config.bin
     */
    public void saveConfig() {
        System.out.println("Salvataggio del file Config.bin ...");
        
        /* SCHEMA DI SALVATAGGIO: una sola stringa nell'unica riga
        path_file_Config.bin */
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Config.bin")))){
            dos.writeUTF(dbUrl);
        }catch(IOException ex){
            System.out.print("ECCEZIONE in scrittura del file Config.bin -> ");
            ex.printStackTrace();
        }
    }

    /**
     * @brief Inizializza il database
     * @pre È valorizzato il campo dbUrl ed è valido
     * @post L'attributo db contiene l'istanza del database
     */
    public void initDB() {
        db = new Database(dbUrl);
    }
}
