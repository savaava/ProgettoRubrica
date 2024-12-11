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
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @brief Modella una rubrica telefonica, può contenere
 * 0 o più contatti,
 * 0 o più tags,
 * 0 o 1 database. 
 * 
 * @see Contact
 * @see Tag
 * @see gruppo1.progettorubrica.services.Database
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
     * @post crea un'istanza di AddressBook in 4 scenari diversi:
     * 1. esiste un link valido per il database in Config.bin ed esiste anche un file Data.bin;
     * 2. esiste solo il link valido per il database;
     * 3. esiste solo il file Data.bin;
     * 4. non esiste nè il DB, nè il file (è la prima apertura).
     */
    private AddressBook() {
        this.contacts = FXCollections.observableArrayList();
        this.tags = FXCollections.observableArrayList();
        
        if(new File("Config.bin").exists())
            loadConfig(); /* ci fornisce l'informazione per cui è presente o meno il DB */
        
        if(Database.verifyDBUrl(dbUrl)) {
            /* esiste sia un database valido che un file Data.bin con i contatti, oppure */
            /* esiste un database valido funzionante quindi lo associamo ad AddressBook */
            initDB();
        }else if(new File("Data.bin").exists()){
            loadOBJ();
        }
    }

    /**
     * @brief Fornisce l'unica istanza di AddressBook, controllando se è stata creata, altrimenti la crea.
     * @pre nessuna
     * @post Il client ottiene l'istanza dell'unica rubrica,
     * se esisteva già ritorna l'istanza già esistente,
     * se non esisteva ancora viene creata.
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
        if(c != null){
            contacts.add(c);
            if(Database.verifyDBUrl(dbUrl)){
                db.insertContact(c);
            }else{
                saveOBJ();
            }
        }
    }

    @Override
    public void addManyContacts(Collection<Contact> c){
        if(c != null){
            contacts.addAll(c);
            if(Database.verifyDBUrl(dbUrl)){
                db.insertManyContacts(c);
            }else{
                saveOBJ();
            }
        }
    }
    
    @Override
    public void removeContact(Contact c) {
        if(c != null){
            contacts.remove(c);
            if(Database.verifyDBUrl(dbUrl)){
                db.removeContact(c);
            }else{
                saveOBJ();
            }
        }
    }

    @Override
    public ObservableList<Tag> getAllTags() {
        return tags;
    }

    @Override
    public Tag getTag(int id) {
        if(id <= 0) 
            return null;
        
        for(Tag t : tags){
            if(t.getId() == id)
                return t;
        }
        return null;
    }

    @Override
    public void addTag(Tag tag) {
        if(tag != null){
            tags.add(tag);
            if(Database.verifyDBUrl(dbUrl)){
                db.insertTag(tag);
            }else{
                saveOBJ();
            }
        }
    }
    
    @Override
    public void addManyTags(Collection<Tag> c){
        if(c != null){
            tags.addAll(c);
            if(Database.verifyDBUrl(dbUrl)){
                db.insertManyTags(c);
            }else{
                saveOBJ();
            }
        }
    }

    @Override
    public void removeTag(Tag tag) {
        if(tag != null){
            tags.remove(tag);
            if(Database.verifyDBUrl(dbUrl)){
                db.removeTag(tag);
            }else{
                saveOBJ();
            }
        }
    }

    /**
     * @brief Valorizza l'attributo dbUrl inserendo il link del database.
     * @param[in] dbUrl Link del database.
     * @pre Il client fornisce una stringa qualsiasi, che dovrebbe essere il link al suo DB.
     * @post L'istanza AddressBook possiede ora questo parametro in ingresso come il link del DB.
     * @post Viene creato il file Config.bin o modificato se era già presente.
     * @post Istanzia il database se il link è valido.
     */
    public void setDBUrl(String dbUrl) {
        this.dbUrl = dbUrl;
        
        saveConfig();
        
        if(Database.verifyDBUrl(dbUrl))
            initDB();
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
     * @pre nessuna (ci possono essere 0 o più contatti e tags e può non esistere il file Data.bin).
     * @post Salva i contatti e i tag dell'AddressBook sul file di salvataggio Data.bin.
     * @throws IOException se il file non esiste o se non si riesce a fare la lettura.
     */
    public void saveOBJ() {
        System.out.print("Scrittura file Data.bin... ");
        
        /* FORMATO fiel Data.bin:
        listacontatti listatags
        */
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Data.bin")))){
            oos.writeObject(new ArrayList<>(contacts));
            oos.writeObject(new ArrayList<>(tags));
            System.out.println("completata con successo");
        }catch(IOException ex){
            System.out.print("ECCEZIONE in scrittura serializzata del file Data.bin -> ");
            ex.printStackTrace();
        }
    }

   
    /**
     * @brief Carica la rubrica telefonica dal file Data.bin, aggiunge i contatti di quest'ultima nella rubrica corrente.
     * @pre nessuna
     * @post Carica i contatti e i tag dal file Data.bin nell'AddressBook.
     * @throws  IOException se non trova il file Data.bin o se non riesce a caricare il contenuto.
     * @throws  ClassNotFoundException se non riesce a convertire il contenuto del file in una lista di Contact o Tag.
     */
    public void loadOBJ() {
        System.out.print("Lettura file Data.bin... ");
        
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("Data.bin")))){
            try {
                contacts.setAll((Collection<Contact>)ois.readObject());
                tags.setAll((Collection<Tag>)ois.readObject());
            } catch (ClassNotFoundException ex) {ex.printStackTrace();}
            System.out.println("completata con successo");
        }catch(IOException ex){
            System.out.println("ECCEZIONE in lettura serializzata del file Data.bin -> ");
            ex.printStackTrace();
        }
    }

    /**
     * @brief Elimina il file Data.bin se presente.
     * @pre nessuna (può essere presente o meno il file Data.bin). 
     * @post il file di salvataggio dati Data.bin viene eliminato, se presente.
     */
    public static void removeOBJ() {
        new File("Data.bin").delete();
    }

    /**
     * @brief Elimina il file Config.bin se presente.
     * @pre nessuna (può essere presente o meno il file Config.bin). 
     * @post il file di configurazione per il db Config.bin viene eliminato, se presente.
     */
    public static void removeConfig() {
        new File("Config.bin").delete();
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
     * @brief trasferisce tutti contatti e tag del file Data.bin sul DB.
     * @pre esiste il DB ed è funzionante.
     * @pre esiste il file Data.bin con eventuali contatti e tags.
     * @post tutti i contatti e tags del file Data.bin vengono inseriti nel DB.
     */
    public void dataToDB(){
        System.out.print("Trasferimento Data.bin sul DB... ");
        
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("Data.bin")))){
            try {
                List<Contact> contattiData = new ArrayList<>((Collection<Contact>)ois.readObject());
                if(! contattiData.isEmpty())
                    db.insertManyContacts(contattiData);
                
                List<Tag> tagsData = new ArrayList<>((Collection<Tag>)ois.readObject());
                if(! tagsData.isEmpty())
                    db.insertManyTags(tagsData);
            } catch (ClassNotFoundException ex) {ex.printStackTrace();}
            System.out.println("completato con successo");
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
     * @brief Carica la configurazione da Config.bin, se presente.
     * @pre nessuna
     * @post Preleva dal file Config.bin l'informazione del link del DB scelto dall'utente.
     * @throws IOException se non lo trova o se non riesce a leggere il file Config.bin.
     */
    public void loadConfig() {
        System.out.print("Lettura del file Config.bin... ");
        
        try(DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream("Config.bin")))){
            dbUrl = dis.readUTF();
            System.out.println("con successo");
        }catch(IOException ex){
            System.out.print("ECCEZIONE in lettura del file Config.bin -> ");
            ex.printStackTrace();
        }
    }

    /**
     * @brief Salva la configurazione su Config.bin.
     * @pre È valorizzato il campo dbUrl, può già esistere un file Config.bin.
     * @post Crea il file Config.bin che conserva l'informazione del link del DB,
     * se già esiste allora lo sovrascrive.
     */
    public void saveConfig() {
        System.out.print("Scrittua file Config.bin... ");
        
        /* SCHEMA DI SALVATAGGIO: una sola stringa nell'unica riga
        path_file_Config.bin */
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Config.bin")))){
            dos.writeUTF(dbUrl);
            System.out.println("completata con successo");
        }catch(IOException ex){
            System.out.print("ECCEZIONE in scrittura del file Config.bin -> ");
            ex.printStackTrace();
        }
    }

    /**
     * @brief Inizializza il database dell'AddressBook e prepara la rubrica al salvataggio sul dB e non più in locale.
     * @pre È valorizzato il campo dbUrl ed è valido.
     * @post L'attributo db contiene l'istanza del database.
     * @post Elimina Data.bin se presente.
     * @post Salva eventuali contatti e tags dal db.
     */
    public void initDB() {
        db = new Database(dbUrl);
        
        if(new File("Data.bin").exists()){
            dataToDB();
            loadFromDB();
            removeOBJ();
            return ;
        }
        
        /* fase di inizializzazione nello scenario in cui non esiste Data.bin */
        if(!contacts.isEmpty())
            db.insertManyContacts(contacts);
        if(!tags.isEmpty())
            db.insertManyTags(tags);
        loadFromDB();
    }
    
    /**
     * @brief Preleva l'istanza del DB.
     * @pre nessuna (può non esistere ancora il DB).
     * @post Il client ottiene l'istanza del DB.
     * @return L'istanza del db, null se non esiste ancora.
     */    
    public Database getDB(){
        return db;
    }
    
    @Override
    public String toString(){
        StringBuffer strb = new StringBuffer(
                "******************** AddressBook ********************\n");
        
        strb.append(contacts.size()).append(" CONTATTI:\n");
        for(Contact c : contacts){
            strb.append(c).append("\n");
        }
        strb.append(tags.size()).append(" TAGS:\n");
        for(Tag tag : tags){
            strb.append(tag).append("\n");
        }        
        strb.append("********************  FINE  ********************\n");
        
        return strb.toString();
    }
}