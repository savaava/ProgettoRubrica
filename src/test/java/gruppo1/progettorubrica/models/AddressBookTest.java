package gruppo1.progettorubrica.models;

import gruppo1.progettorubrica.services.Database;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class AddressBookTest {
    private AddressBook a;
    private static final String url = "mongodb+srv://rubricaContatti:tqHPmDYFftuxXE3g@mongisacluster.o8crvzq.mongodb.net/?retryWrites=true&w=majority&appName=MongisaCluster";
    private static final String pathData = "Data.bin";
    private static final String pathConfig = "Config.bin";
    
    @Before
    public void setUp() throws Exception {
        /*  (LEGGERO) */
        System.out.print("\n---Prossimo metodo da testare:  ");
        /* per testare la classe singleton devo resettare l'istanza statica di AddressBook a null */
        Field instanceField = AddressBook.class.getDeclaredField("instance");
        instanceField.setAccessible(true); /* Rendo il campo accessibile */
        instanceField.set(null, null); /* Imposta il campo statico (null) a null per resettarlo */
        instanceField.setAccessible(false);
    }
    @After
    public void tearDown(){
        /*  (LEGGERO) */
        AddressBook.removeConfig();
        AddressBook.removeOBJ();
    }
    
    
    /* metodi di utilità per i test */
    private Contact getContattoCasuale(){
        Random random = new Random();

        String nomi[] = {"Pippo", "Paperino", "Pluto", "Gis"};
        String cognomi[] = {"Rossi", "Bianchi", "Verdi", "Neri"};
        
        String name = nomi[random.nextInt(nomi.length)];
        String surname = cognomi[random.nextInt(cognomi.length)];

        int numNumeri = random.nextInt(3)+1;
        String[] numbers = new String[numNumeri];
        for (int i=0; i<numNumeri; i++) {
            numbers[i] = Integer.toString(random.nextInt(1000000000)+10000000);
        }

        int numEmails = random.nextInt(3)+1;
        String[] emails = new String[numEmails];
        for (int i=0; i<numEmails; i++) {
            emails[i] = name.toLowerCase()+"."+surname.toLowerCase()+(i+1)+"@gmail.com";
        }

        int numBytes = random.nextInt(10)+1;
        byte[] profilePicture = new byte[numBytes];
        for (int i=0; i<numBytes; i++) {
            profilePicture[i] = (byte)(random.nextInt(1000)+1);
        }

        Contact c = new Contact(name, surname);
        c.setNumbers(numbers);
        c.setEmails(emails);
        c.setProfilePicture(profilePicture);

        return c;
    }
    private Tag getTagCasuale(){
        return getTagCasuale(new Random().nextInt(100)+1);
    }
    private Tag getTagCasuale(int id){     
        Random random = new Random();
        
        String descrizioni[] = {"Famiglia", "Lavoro", "Calcetto", "Giochi", "Amici"};
        
        return new Tag(descrizioni[random.nextInt(descrizioni.length)], id);
    }
    private ArrayList<Contact> getListaContattiCasuali(int num){
        ArrayList<Contact> out = new ArrayList<>();        
        for(int i=0; i<num; i++){
            out.add(getContattoCasuale());
        }        
        return out;
    }
    private ArrayList<Tag> getListaTagCasuali(int num){
        ArrayList<Tag> out = new ArrayList<>();        
        for(int i=0; i<num; i++){
            out.add(getTagCasuale());
        }        
        return out;
    } 
    
     
    /**
     * UTC 1.1 (LEGGERO)
     * 
     * Testa la prima apertura dell'applicazione (no Config.bin (no dbUrl), no Data.bin).
     * Quindi è previsto il salvataggio dei contatti e tags in locale nel file Data.bin,
     * ma questo ancora non esiste perchè non è stato fatta nessuna aggiunta.
     */
    @Test
    public void testGetInstance1() {
        System.out.println("getInstance1");
        
        a = AddressBook.getInstance();
        
        assertTrue(a.getAllContacts().isEmpty() && a.getAllTags().isEmpty());
        assertTrue(a.getDBUrl().isEmpty());
        assertNull(a.getDB());
        assertFalse(new File(pathConfig).exists());
        assertFalse(new File(pathData).exists());
    }
    
    /**
     * UTC 1.2 (LEGGERO)
     * 
     * Testa un'inizializzazione dell'applicazione nello scenario in cui esiste solo Data.bin.
     * Ipotetico scenario in cui l'utente apre l'applicazione che non ha mai connesso ad un DB 
     * e la sua nuova sessione ripristina i dati di quella precedente, attraverso Data.bin.
     */
    @Test
    public void testGetInstance2() {
        System.out.println("getInstance2");
               
        List<Contact> contactsProva = getListaContattiCasuali(2);   
        List<Tag> tagsProva = getListaTagCasuali(3);
        
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(pathData)))){
            oos.writeObject(contactsProva);
            oos.writeObject(tagsProva);
        }catch(IOException ex){ex.printStackTrace();}
        
        a = AddressBook.getInstance();
        
        assertEquals(contactsProva, a.getAllContacts());
        assertEquals(tagsProva, a.getAllTags());
        assertTrue(a.getDBUrl().isEmpty());
        assertNull(a.getDB());
        assertFalse(new File(pathConfig).exists());
        assertTrue(new File(pathData).exists());
    }
    
    /**
     * UTC 1.3 (ONEROSO)
     * 
     * Testa un'inizializzazione dell'applicazione nello scenario in cui
     * esiste solo Config.bin e il link contenuto è valido.
     * 
     * Nel database ci sono dei contatti e tag salvati eventualmente dalla sessione precedente
     * e quindi l'oracolo vuole che la rubrica prelevi tali valori.
     */
    //@Test
    public void testGetInstance3() {
        System.out.println("getInstance3");
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(pathConfig)))){
            dos.writeUTF(url);
        }catch(IOException ex){ex.printStackTrace();}
        
        List<Contact> contactsProva = getListaContattiCasuali(2);   
        List<Tag> tagsProva = getListaTagCasuali(3);
        
        Database database = new Database(url);  
        
        /* salvo lo stato iniziale del DB */
        Collection<Contact> savedContacts = database.getAllContacts();
        Collection<Tag> savedTags = database.getAllTags();
        database.deleteAllContacts();
        database.deleteAllTags();    
        
        database.insertManyContacts(contactsProva);
        database.insertManyTags(tagsProva);        
        a = AddressBook.getInstance();
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllContacts();
        database.deleteAllTags();
        if(!savedContacts.isEmpty())
            database.insertManyContacts(savedContacts);
        if(!savedTags.isEmpty())
            database.insertManyTags(savedTags);
        
        assertEquals(contactsProva.size(), a.getAllContacts().size());
        assertEquals(tagsProva.size(), a.getAllTags().size());
        assertTrue(a.getAllTags().containsAll(tagsProva));
        assertTrue(a.getAllContacts().containsAll(contactsProva));
        assertEquals(url, a.getDBUrl());
        assertNotNull(a.getDB());
    }
    
    /**
     * UTC 1.4 (ONEROSO)
     * 
     * Testa un'inizializzazione dell'applicazione nello scenario in cui
     * esiste Config.bin che conserva un dbUrl valido;
     * esiste il file Data.bin.
     * 
     * Ci sono dei contatti e tag nel DB e nel file Data.bin
     * L'oracolo stabilisce che che la rubrica salva le informazioni sul DB,
     * e che venga eliminato il file Data.bin.
     */
    //@Test
    public void testGetInstance4() {
        System.out.println("getInstance4");
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(pathConfig)))){
            dos.writeUTF(url);
        }catch(IOException ex){ex.printStackTrace();}

        List<Contact> contactsProvaData = getListaContattiCasuali(2);   
        List<Contact> contactsProvaDb = getListaContattiCasuali(2);   
        List<Tag> tagsProvaData = getListaTagCasuali(3);
        List<Tag> tagsProvaDb = getListaTagCasuali(3);
        
        Database database = new Database(url);
        
        /* salvo lo stato iniziale del DB */
        Collection<Contact> savedContacts = database.getAllContacts();
        Collection<Tag> savedTags = database.getAllTags();
        database.deleteAllContacts();
        database.deleteAllTags();
        
        database.insertManyContacts(contactsProvaDb);
        database.insertManyTags(tagsProvaDb);
        
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(pathData)))){
            oos.writeObject(contactsProvaData);
            oos.writeObject(tagsProvaData);
        }catch(IOException ex){ex.printStackTrace();}
        
        a = AddressBook.getInstance();
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllContacts();
        database.deleteAllTags();
        if(!savedContacts.isEmpty())
            database.insertManyContacts(savedContacts);
        if(!savedTags.isEmpty())
            database.insertManyTags(savedTags);
        
        assertFalse(new File(pathData).exists());
        assertTrue(new File(pathConfig).exists());
        assertEquals(contactsProvaData.size(), a.getAllContacts().size());
        assertEquals(tagsProvaData.size(), a.getAllTags().size());
        assertEquals(contactsProvaData, a.getAllContacts());
        assertEquals(tagsProvaData, a.getAllTags());
        assertEquals(url, a.getDBUrl());
        assertNotNull(a.getDB());
    }

    /**
     * UTC 1.5 (LEGGERO)
     */
    @Test
    public void testGetAllContacts() {
        System.out.println("getAllContacts");
        
        List<Contact> contactsProva = getListaContattiCasuali(2);
        
        a = AddressBook.getInstance();
        a.addManyContacts(contactsProva);
        
        assertEquals(contactsProva, a.getAllContacts());
    }
    
    /**
     * UTC 1.6 (LEGGERO)
     * 
     * Il contatto si salva in locale, perchè non esiste il DB.
     */
    @Test
    public void testAddContact1() {
        System.out.println("addContact1");
        
        List<Contact> contactsProva = getListaContattiCasuali(2);
        
        a = AddressBook.getInstance();
        for(Contact c : contactsProva){
            a.addContact(c);
        }
        
        List<Contact> contattiLetti = null;
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(pathData)))){
            try {
                contattiLetti = new ArrayList<>((Collection<Contact>)ois.readObject());
            } catch (ClassNotFoundException ex) {ex.printStackTrace();}
        }catch(IOException ex){ex.printStackTrace();}
        
        assertEquals(contactsProva, a.getAllContacts());
        assertEquals(contattiLetti, a.getAllContacts());
    }
    
    /**
     * UTC 1.7 (MOLTO ONEROSO)
     * 
     * I contatti non si salvano in locale, bensì sul DB.
     * Quindi non esiste il file Data.bin e non si deve creare.
     */
    //@Test
    public void testAddContact2() {
        System.out.println("addContact2");
        
        List<Contact> contactsProva = getListaContattiCasuali(2);
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(pathConfig)))){
            dos.writeUTF(url);
        }catch(IOException ex){ex.printStackTrace();}
        
        Database database = new Database(url);
        
        /* salvo lo stato iniziale del DB */
        Collection<Contact> savedContacts = database.getAllContacts();
        database.deleteAllContacts();
        
        a = AddressBook.getInstance();
        for(Contact c : contactsProva){
            a.addContact(c);
        }
        
        List<Contact> contattiLetti = new ArrayList<>(database.getAllContacts());
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllContacts();
        if(!savedContacts.isEmpty())
            database.insertManyContacts(savedContacts);
        
        assertEquals(contattiLetti, a.getAllContacts());
        assertFalse(new File(pathData).exists());
        assertTrue(new File(pathConfig).exists());
    }
    
    /**
     * UTC 1.8 (ONEROSO)
     * 
     * Il contatto non si salva in locale, bensì sul DB.
     * Quindi non esiste il file Data.bin e non si deve creare.
     */
    //@Test
    public void testAddManyContacts1() {
        System.out.println("addManyContacts1");
        
        List<Contact> contactsProva = getListaContattiCasuali(2);
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(pathConfig)))){
            dos.writeUTF(url);
        }catch(IOException ex){ex.printStackTrace();}
        
        Database database = new Database(url);
        
        /* salvo lo stato iniziale del DB */
        Collection<Contact> savedContacts = database.getAllContacts();
        database.deleteAllContacts();
        
        a = AddressBook.getInstance();
        a.addManyContacts(contactsProva);
        
        List<Contact> contattiLetti = new ArrayList<>(database.getAllContacts());
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllContacts();
        if(!savedContacts.isEmpty())
            database.insertManyContacts(savedContacts);
        
        assertEquals(contattiLetti, a.getAllContacts());
        assertFalse(new File(pathData).exists());
    }
    
    /**
     * UTC 1.9 (MOLTO ONEROSO)
     * 
     * L'utente possiede un DB funzionante e sta inserendo i suoi contatti e tag
     * Ma il DB diventa improvvisamente irraggiungibile (cambio l'url con uno non valido) e l'utente continua ad aggiungere, però su Data.bin
     * Poi il DB torna a funzionare (cambio l'url con quello valido) durante la stessa sessione, e in fase di aggiunta si carica sul DB.
     */
    //@Test
    public void testAddManyContacts2() throws Exception {
        System.out.println("addManyContacts2");
        
        List<Contact> contactsProva = getListaContattiCasuali(2);
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(pathConfig)))){
            dos.writeUTF(url);
        }catch(IOException ex){ex.printStackTrace();}
        
        Database database = new Database(url);
        
        /* salvo lo stato iniziale del DB */
        Collection<Contact> savedContacts = database.getAllContacts();
        database.deleteAllContacts();
        
        a = AddressBook.getInstance();
        a.addManyContacts(contactsProva);
        
        /* improvvisamente non funziona più il DB */
        Field urlField = AddressBook.class.getDeclaredField("dbUrl");
        urlField.setAccessible(true);
        urlField.set(a, "urlNonValido.it");
        
        contactsProva = getListaContattiCasuali(2); 
        a.addManyContacts(contactsProva); /* si crea il file Data.bin, in cui verranno salvati i successivi contatti e tag */
        
        /* improvvisamente il DB torna a funzionare */
        urlField.set(a, url);
        urlField.setAccessible(false);
        
        contactsProva = getListaContattiCasuali(2); 
        a.addManyContacts(contactsProva);
        
        List<Contact> contattiLetti = new ArrayList<>(database.getAllContacts());
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllContacts();
        if(!savedContacts.isEmpty())
            database.insertManyContacts(savedContacts);
        
        assertTrue(new File(pathConfig).exists());
        assertFalse(new File(pathData).exists());
        assertEquals(contattiLetti, a.getAllContacts());
    }
    
    /**
     * UTC 1.10 (LEGGERO)
     * 
     * Il contatto si rimuove dal file Data.bin e non dal DB perchè non è stato specificato.
     * Si rimuove l'unico contatto presente quindi l'oracolo stabiliscew anche che non deve più esistere Data.bin
     */
    @Test
    public void testRemoveContact1() {
        System.out.println("removeContact1");
        
        Contact c = getContattoCasuale();
        
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(pathData)))){
            oos.writeObject(new ArrayList<>(Arrays.asList(c)));
            oos.writeObject(new ArrayList<Tag>());
        }catch(IOException ex){ex.printStackTrace();}
        
        a = AddressBook.getInstance();
        a.removeContact(c);
        
        assertTrue(a.getAllContacts().isEmpty());
        assertTrue(a.getAllTags().isEmpty());
        assertFalse(new File(pathData).exists());
    }
    
    /**
     * UTC 1.11 (ONEROSO)
     * 
     * Il contatto si rimuove dal DB e non dal file Data.bin.
     */
    //@Test
    public void testRemoveContact2() {
        System.out.println("removeContact2");
        
        Contact cVett[] = {getContattoCasuale(), getContattoCasuale()};       
        Tag tagVett[] = {getTagCasuale(), getTagCasuale(), getTagCasuale()};
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(pathConfig)))){
            dos.writeUTF(url);
        }catch(IOException ex){ex.printStackTrace();}
        
        Database database = new Database(url);
        
        /* salvo lo stato iniziale del DB */
        Collection<Contact> savedContacts = database.getAllContacts();
        Collection<Tag> savedTags = database.getAllTags();
        database.deleteAllContacts();
        database.deleteAllTags();
        
        a = AddressBook.getInstance();
        a.addManyContacts(Arrays.asList(cVett));
        a.addManyTags(Arrays.asList(tagVett));
        a.removeContact(cVett[0]);
        
        List<Contact> contattiLetti = new ArrayList<>(database.getAllContacts());
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllContacts();
        database.deleteAllTags();
        if(!savedContacts.isEmpty())
            database.insertManyContacts(savedContacts);
        if(!savedTags.isEmpty())
            database.insertManyTags(savedTags);
        
        assertEquals(1, a.getAllContacts().size());
        assertTrue(a.getAllContacts().contains(cVett[1]));
        assertEquals(tagVett.length, a.getAllTags().size());
        assertEquals(contattiLetti, a.getAllContacts());
    }
    
    /**
     * UTC 1.12 (MOLTO ONEROSO)
     * 
     * L'utente possiede un DB funzionante e sta inserendo i suoi contatti e tag.
     * Tuttavia rimuove un contatto quando non funziona più, pertanto si genera Data.bin
     * Continuando ad usare l'applicazione il DB torna a funzionare e
     * l'oracolo stabilisce che Data.bin non esiste più e il database contiene le stesse info dell'AddressBook
     */
    //@Test
    public void testRemoveContact3() throws Exception{
        System.out.println("removeContact3");
        
        List<Contact> contactsProva = getListaContattiCasuali(2);
        List<Tag> tagsProva = getListaTagCasuali(2);
        
        Database database = new Database(url);
        
        /* salvo lo stato iniziale del DB */
        Collection<Contact> savedContacts = database.getAllContacts();
        Collection<Tag> savedTags = database.getAllTags();
        database.deleteAllContacts();
        database.deleteAllTags();
        
        a = AddressBook.getInstance();
        a.addManyContacts(contactsProva);
        a.setDBUrl(url);
        a.addManyTags(tagsProva);
        
        /* improvvisamente non funziona più il DB */
        Field urlField = AddressBook.class.getDeclaredField("dbUrl");
        urlField.setAccessible(true);
        urlField.set(a, "urlNonValido.it");
        
        contactsProva = getListaContattiCasuali(2);
        a.addManyContacts(contactsProva);
        a.removeContact(contactsProva.get(0));
        
        /* improvvisamente il DB torna a funzionare */
        urlField.set(a, url);
        contactsProva = getListaContattiCasuali(2); 
        a.addManyContacts(contactsProva);
        
        List<Contact> contattiLetti = new ArrayList<>(database.getAllContacts());
        List<Tag> tagsLetti = new ArrayList<>(database.getAllTags());
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllContacts();
        database.deleteAllTags();        
        if(!savedContacts.isEmpty())
            database.insertManyContacts(savedContacts);
        if(!savedTags.isEmpty())
            database.insertManyTags(savedTags);
        
        assertTrue(new File(pathConfig).exists());
        assertFalse(new File(pathData).exists());
        assertEquals(5, a.getAllContacts().size());
        assertEquals(contattiLetti, a.getAllContacts());
        assertEquals(tagsLetti, a.getAllTags());
    }
    
    /**
     * UTC 1.13 (LEGGERO)
     */
    @Test
    public void testGetAllTags() {
        System.out.println("getAllTags");
        
        List<Tag> tagsProva = getListaTagCasuali(3);
        
        a = AddressBook.getInstance();
        a.addManyTags(tagsProva);
        
        assertEquals(tagsProva, a.getAllTags());
    }
    
    /**
     * UTC 1.14 (LEGGERO)
     * 
     * Tag presente
     */
    @Test
    public void testGetTag1() {
        System.out.println("getTag1");
        
        Tag tagVett[] = {getTagCasuale(111), getTagCasuale(222), getTagCasuale(333)};
        
        a = AddressBook.getInstance();
        a.addManyTags(Arrays.asList(tagVett));
        
        Tag tagPrelevato = a.getTag(222);
        
        assertEquals(tagVett[1], tagPrelevato);
    }
    
    /**
     * UTC 1.15 (LEGGERO)
     * 
     * Tag non presente
     */
    @Test
    public void testGetTag2() {
        System.out.println("getTag2");
        
        Tag tagVett[] = {getTagCasuale(111), getTagCasuale(222), getTagCasuale(333)};
        
        a = AddressBook.getInstance();
        a.addManyTags(Arrays.asList(tagVett));
        
        Tag tagPrelevato = a.getTag(123);
        
        assertNull(tagPrelevato);
    }
    
    /**
     * UTC 1.16 (LEGGERO)
     * 
     * Il tag si salva in locale, perchè non esiste il DB.
     */
    @Test
    public void testAddTag1(){
        System.out.println("addTag1");
        
        List<Tag> tagsProva = getListaTagCasuali(3);
        
        a = AddressBook.getInstance();
        
        for(Tag tag : tagsProva){
            a.addTag(tag);
        }
        
        List<Tag> tagLetti = null;
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(pathData)))){
            try {
                ois.readObject();
                tagLetti = new ArrayList<>((Collection<Tag>)ois.readObject());
            } catch (ClassNotFoundException ex) {ex.printStackTrace();}
        }catch(IOException ex){ex.printStackTrace();}
        
        assertEquals(tagsProva, a.getAllTags());
        assertEquals(tagLetti, a.getAllTags());
    }
    
    /**
     * UTC 1.17 (MOLTO ONEROSO)
     * 
     * Il tag non si salva in locale, bensì sul DB.
     */
    //@Test
    public void testAddTag2() {
        System.out.println("addTag2");
        
        List<Tag> tagsProva = getListaTagCasuali(3);
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(pathConfig)))){
            dos.writeUTF(url);
        }catch(IOException ex){ex.printStackTrace();}
        
        Database database = new Database(url);
        
        /* salvo lo stato iniziale del DB */
        Collection<Tag> savedTags = database.getAllTags();
        database.deleteAllTags();
        
        a = AddressBook.getInstance();
        for(Tag tag : tagsProva){
            a.addTag(tag);
        }
        
        List<Tag> tagLetti = new ArrayList<>(database.getAllTags());
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllTags();
        if(!savedTags.isEmpty())
            database.insertManyTags(savedTags);
        
        assertEquals(tagsProva, a.getAllTags());    
        assertEquals(tagLetti, a.getAllTags());
        assertFalse(new File(pathData).exists());
    }
    
    /**
     * UTC 1.18 (ONEROSO)
     * 
     * I tags non si salvano in locale, bensì sul DB.
     */
    //@Test
    public void testAddManyTags() {
        System.out.println("addManyTags");
        
        List<Tag> tagsProva = getListaTagCasuali(3);
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(pathConfig)))){
            dos.writeUTF(url);
        }catch(IOException ex){ex.printStackTrace();}
        
        Database database = new Database(url);
        
        /* salvo lo stato iniziale del DB */
        Collection<Tag> savedTags = database.getAllTags();
        database.deleteAllTags();
        
        a = AddressBook.getInstance();
        a.addManyTags(tagsProva);
        
        List<Tag> tagLetti = new ArrayList<>(database.getAllTags());
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllTags();
        if(!savedTags.isEmpty())
            database.insertManyTags(savedTags);
        
        assertEquals(tagsProva, a.getAllTags());    
        assertEquals(tagLetti, a.getAllTags());
        assertFalse(new File(pathData).exists());
        assertTrue(new File(pathConfig).exists());
    }
    
    /**
     * UTC 1.19 (LEGGERO)
     * 
     * Il tag si rimuove dal file Data.bin e non dal DB perchè non esiste.
     */
    @Test
    public void testRemoveTag1() {
        System.out.println("removeTag1");
        
        Tag tagVett[] = {getTagCasuale(), getTagCasuale(), getTagCasuale()};
        
        a = AddressBook.getInstance();
        a.addManyTags(new ArrayList<>(Arrays.asList(tagVett)));
        
        a.removeTag(tagVett[1]);
        
        List<Tag> tagLetti = null;
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(pathData)))){
            try {
                ois.readObject();
                tagLetti = new ArrayList<>((Collection<Tag>)ois.readObject());
            } catch (ClassNotFoundException ex) {ex.printStackTrace();}
        }catch(IOException ex){ex.printStackTrace();}
        
        assertEquals(2, a.getAllTags().size());
        assertTrue(a.getAllTags().contains(tagVett[0])
                && a.getAllTags().contains(tagVett[2]));
        assertEquals(tagLetti, a.getAllTags());
    }
    
    /**
     * UTC 1.20 (ONEROSO)
     * 
     * Il tag si rimuove dal DB e non dal file Data.bin.
     */
    //@Test
    public void testRemoveTag2() {
        System.out.println("removeTag2");
        
        Tag tagVett[] = {getTagCasuale(), getTagCasuale()};
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(pathConfig)))){
            dos.writeUTF(url);
        }catch(IOException ex){ex.printStackTrace();}
        
        Database database = new Database(url);
        
        /* salvo lo stato iniziale del DB */
        Collection<Tag> savedTags = database.getAllTags();
        database.deleteAllTags();
        
        database.insertManyTags(Arrays.asList(tagVett));        
        a = AddressBook.getInstance();
        a.removeTag(tagVett[1]);
        
        List<Tag> tagLetti = new ArrayList<>(database.getAllTags());
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllTags();
        if(!savedTags.isEmpty())
            database.insertManyTags(savedTags);
        
        assertEquals(1, a.getAllTags().size());
        assertTrue(a.getAllTags().contains(tagVett[0]));
        assertEquals(tagLetti, a.getAllTags());
        assertFalse(new File(pathData).exists());
        assertTrue(new File(pathConfig).exists());        
    }

    /**
     * UTC 1.21 (ONEROSO)
     * 
     * Nessun contatto o tag nella rubrica inizialmente
     * Ipotetico scenario in cui l'utente ha la rubrica vuota (no Data.bin) e imposta il DB che possiede eventuali info.
     */
    //@Test
    public void testSetDBUrl1() {
        System.out.println("setDBUrl1");
        
        List<Contact> contactsProvaDb = getListaContattiCasuali(2);
        
        Database database = new Database(url);
        
        /* salvo lo stato iniziale del DB */
        Collection<Contact> savedContacts = database.getAllContacts();
        database.deleteAllContacts();
        
        /* il DB possiede già dei contatti e tags inizialmente: */
        database.insertManyContacts(contactsProvaDb);
        
        a = AddressBook.getInstance();
        
        a.setDBUrl(url); /* rubrica vuota (nessun Data.bin) e l'utente aggiunge il DB */
        
        List<Contact> contattiLetti = new ArrayList<>(database.getAllContacts());
        List<Tag> tagsLetti = new ArrayList<>(database.getAllTags());
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllContacts();
        if(!savedContacts.isEmpty())
            database.insertManyContacts(savedContacts);
        
        assertTrue(new File(pathConfig).exists());
        assertFalse(new File(pathData).exists());
        assertEquals(url, a.getDBUrl());
        assertNotNull(a.getDB());
        assertEquals(contattiLetti, a.getAllContacts());
        assertEquals(tagsLetti, a.getAllTags());
    }
    
    /**
     * UTC 1.22 (ONEROSO)
     * 
     * Inizialmente Contatti o tags presenti nel file Data.bin, da caricare ora sul DB vuoto.
     * Ipotetico scenario in cui l'utente carica i propri contatti sulla rubrica
     * e poi decide di aggiungere un DB vuoto e quindi li carica qui.
     * Il DB verrà svuotato e poi verranno caricate le informazioni di Data.bin
     */
    //@Test
    public void testSetDBUrl2() {
        System.out.println("setDBUrl2");
        
        List<Tag> tagsProva = getListaTagCasuali(3);
        
        Database database = new Database(url);
        
        /* salvo lo stato iniziale del DB */
        Collection<Tag> savedTags = database.getAllTags();
        database.deleteAllTags();
        
        a = AddressBook.getInstance();
        a.addManyTags(tagsProva); /* qui è presente ancora il Data.bin */
        
        a.setDBUrl(url);
        
        List<Contact> contattiLetti = new ArrayList<>(database.getAllContacts());
        List<Tag> tagsLetti = new ArrayList<>(database.getAllTags());
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllTags();
        if(!savedTags.isEmpty())
            database.insertManyTags(savedTags);
        
        assertTrue(new File(pathConfig).exists());
        assertFalse(new File(pathData).exists());
        assertEquals(url, a.getDBUrl());
        assertNotNull(a.getDB());
        assertEquals(contattiLetti, a.getAllContacts());
        assertEquals(tagsLetti, a.getAllTags());
    }
    
    /**
     * UTC 1.23 (ONEROSO)
     * 
     * Contatti o tags nella rubrica inizialmente anche se esiste già il file Config.bin, da caricare sul DB.
     * Ipotetico scenario in cui l'utente carica i propri contatti sulla rubrica perchè il link
     * che aveva inserito per il DB non era valido quindi Config.bin esiste già ma non è utilizzabile.
     * L'utente poi decide di aggiungere un DB valido e quindi carica qui le informazioni della rubrica.
     */
    //@Test
    public void testSetDBUrl3() {
        System.out.println("setDBUrl3");
        
        List<Contact> contactsProva = getListaContattiCasuali(2);
        
        Database database = new Database(url);
        
        /* salvo lo stato iniziale del DB */
        Collection<Contact> savedContacts = database.getAllContacts();
        database.deleteAllContacts();
        
        a = AddressBook.getInstance();
        
        a.setDBUrl("link_non_valido.it");        
        
        a.addManyContacts(contactsProva); /* qui è presente ancora il Data.bin */
        
        a.setDBUrl(url);
        
        List<Contact> contattiLetti = new ArrayList<>(database.getAllContacts());
        List<Tag> tagsLetti = new ArrayList<>(database.getAllTags());
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllContacts();
        if(!savedContacts.isEmpty())
            database.insertManyContacts(savedContacts);
        
        assertTrue(new File(pathConfig).exists());
        assertFalse(new File(pathData).exists());
        assertEquals(url, a.getDBUrl());
        assertNotNull(a.getDB());
        assertEquals(contattiLetti, a.getAllContacts());
        assertEquals(tagsLetti, a.getAllTags());
    }
    
    /**
     * UTC 1.24 (ONEROSO)
     * 
     * Inizialmente Contatti o tags presenti nel file Data.bin, da caricare ora sul DB non vuoto.
     * Ipotetico scenario in cui l'utente carica i propri contatti sulla rubrica
     * e poi decide di aggiungere un DB non vuoto con delle informazioni aggiuntive,
     * quindi il metodo inserisce Data.bin nel DB che viene prima svuotato,
     * eliminando Data.bin alla fine.
     */
    //@Test
    public void testSetDBUrl4() {
        System.out.println("setDBUrl4");
        
        List<Contact> contactsProvaData = getListaContattiCasuali(2);
        List<Contact> contactsProvaDb = getListaContattiCasuali(2);
        List<Tag> tagsProvaDb = getListaTagCasuali(2);
        
        /* esiste un DB ancora non associato alla rubrica */
        Database database = new Database(url);
        
        /* salvo lo stato iniziale del DB */
        Collection<Contact> savedContacts = database.getAllContacts();
        Collection<Tag> savedTags = database.getAllTags();
        database.deleteAllContacts();
        database.deleteAllTags();
        
        /* il DB possiede già dei contatti e tags inizialmente: */
        database.insertManyContacts(contactsProvaDb);
        database.insertManyTags(tagsProvaDb);
        
        a = AddressBook.getInstance();  /* nessun DB perchè nessun Config.bin */
        a.addManyContacts(contactsProvaData); /* qui è presente ancora il Data.bin */
        
        a.setDBUrl(url); /* qui carica Data.bin su DB e preleva i contatti e tags che già esistevano sul DB */
        
        List<Contact> contattiLetti = new ArrayList<>(database.getAllContacts());
        List<Tag> tagsLetti = new ArrayList<>(database.getAllTags());
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllContacts();
        database.deleteAllTags();
        if(!savedContacts.isEmpty())
            database.insertManyContacts(savedContacts);
        if(!savedTags.isEmpty())
            database.insertManyTags(savedTags);
        
        assertTrue(new File(pathConfig).exists());
        assertFalse(new File(pathData).exists());
        assertEquals(a.getDBUrl(), url);
        assertNotNull(a.getDB());
        assertEquals(contattiLetti, a.getAllContacts());
        assertEquals(tagsLetti, a.getAllTags());
    }
    
    /**
     * UTC 1.25 (ONEROSO)
     * 
     * L'utente possiede un DB funzionante e sta inserendo i suoi contatti e tag
     * Ma l'utente inserisce un DBurl non valido durante la sessione, e continua ad aggiungere però il salvataggio avviene ora su Data.bin
     * Poi l'utente inserisce l'url valido e in fase di aggiunta si carica sul DB.
     */
    //@Test
    public void testSetDBUrl5() {
        System.out.println("setDBUrl5");
        
        List<Contact> contactsProva = getListaContattiCasuali(2);
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(pathConfig)))){
            dos.writeUTF(url);
        }catch(IOException ex){ex.printStackTrace();}
        
        Database database = new Database(url);
        
        /* salvo lo stato iniziale del DB */
        Collection<Contact> savedContacts = database.getAllContacts();
        database.deleteAllContacts();
        
        a = AddressBook.getInstance();
        a.addManyContacts(contactsProva);
        
        a.setDBUrl("urlNonValido.it"); /* l'utente aggiorna il file Config.bin */        
        contactsProva = getListaContattiCasuali(2); 
        a.addManyContacts(contactsProva); /* si crea il file Data.bin, in cui verranno salvati i successivi contatti e tag */
        
        a.setDBUrl(url); /* ora si trasferisce Data.bin sul DB vuoto e si elimina Data.bin */
        
        List<Contact> contattiLetti = new ArrayList<>(database.getAllContacts());
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllContacts();
        if(!savedContacts.isEmpty())
            database.insertManyContacts(savedContacts);
        
        assertEquals(contattiLetti, a.getAllContacts());
        assertFalse(new File(pathData).exists());
        assertTrue(new File(pathConfig).exists());
    }

    /**
     * UTC 1.26 (LEGGERO)
     * 
     * In fase di inizializzazione esiste il file Config.bin e quindi il campo dbUrl è valorizzato
     */
    @Test
    public void testGetDBUrl1() {
        System.out.println("getDBUrl1");
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(pathConfig)))){
            dos.writeUTF("urlCasuale.it");
        }catch(IOException ex){ex.printStackTrace();}
        
        a = AddressBook.getInstance();
        
        assertEquals("urlCasuale.it", a.getDBUrl());
    }
    
    /**
     * UTC 1.27 (LEGGERO)
     * 
     * In fase di inizializzazione NON esiste il file Config.bin, ma l'utente inserisce durante la sessione l'url del DB.
     */
    @Test
    public void testGetDBUrl2() {
        System.out.println("getDBUrl2");
        
        a = AddressBook.getInstance();
        a.setDBUrl("urlCasuale.it");
        
        assertEquals("urlCasuale.it", a.getDBUrl());
        assertTrue(new File(pathConfig).exists());
    }

    /**
     * UTC 1.28 (LEGGERO)
     * 
     * Vi sono sia contatti che tags da salvare in locale. (non esiste il DB).
     */
    @Test
    public void testSaveOBJ1() {
        System.out.println("saveOBJ1");
        
        List<Contact> contactsProva = getListaContattiCasuali(2);
        List<Tag> tagsProva = getListaTagCasuali(2);
        
        /* qui potrebbe caricare dei contatti o tags dal file Data.bin se questo era già presente */
        a = AddressBook.getInstance();
        a.addManyContacts(contactsProva);
        a.addManyTags(tagsProva);
        a.saveOBJ(); /* anche se avviene alle 2 istruzioni sopra */
        
        List<Contact> c = null;
        List<Tag> t = null;
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(pathData)))){
            try {
                c = new ArrayList<>((Collection<Contact>)ois.readObject());
                t = new ArrayList<>((Collection<Tag>)ois.readObject());
            } catch (ClassNotFoundException ex) {ex.printStackTrace();}
        }catch(IOException ex){ex.printStackTrace();}
        
        assertEquals(a.getAllContacts(), c);
        assertEquals(a.getAllTags(), t);
    }
    
    /**
     * UTC 1.29 (LEGGERO)
     * 
     * Vi sono solo contatti da salvare in locale. (non esiste il DB).
     */
    @Test
    public void testSaveOBJ2() {
        System.out.println("saveOBJ2");
        
        List<Contact> contactsProva = getListaContattiCasuali(2);
        
        /* qui potrebbe caricare dei contatti o tags dal file Data.bin se questo era già presente */
        a = AddressBook.getInstance();
        a.addManyContacts(contactsProva);
        a.saveOBJ(); /* anche se avviene all' istruzione sopra */
        
        List<Contact> c = null;
        List<Tag> t = null;
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(pathData)))){
            try {
                c = new ArrayList<>((Collection<Contact>)ois.readObject());
                t = new ArrayList<>((Collection<Tag>)ois.readObject());
            } catch (ClassNotFoundException ex) {ex.printStackTrace();}
        }catch(IOException ex){ex.printStackTrace();}
        
        assertEquals(a.getAllContacts(), c);
        assertEquals(a.getAllTags(), t);
    }
    
    /**
     * UTC 1.30 (LEGGERO)
     * 
     * Vi sono solo tags da salvare in locale. (non esiste il DB).
     */
    @Test
    public void testSaveOBJ3() {
        System.out.println("saveOBJ3");
        
        List<Tag> tagsProva = getListaTagCasuali(3);
        
        /* qui potrebbe caricare dei contatti o tags dal file Data.bin se questo era già presente */
        a = AddressBook.getInstance();
        a.addManyTags(tagsProva);
        a.saveOBJ(); /* anche se avviene all' istruzione sopra */
        
        List<Contact> c = null;
        List<Tag> t = null;
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(pathData)))){
            try {
                c = new ArrayList<>((Collection<Contact>)ois.readObject());
                t = new ArrayList<>((Collection<Tag>)ois.readObject());
            } catch (ClassNotFoundException ex) {ex.printStackTrace();}
        }catch(IOException ex){ex.printStackTrace();}
        
        assertEquals(a.getAllContacts(), c);
        assertEquals(a.getAllTags(), t);        
    }

    /**
     * UTC 1.31 (LEGGERO)
     * 
     * La rubrica preleva entrambi contatti e tags dal file Data.bin
     */
    @Test
    public void testLoadOBJ1() {
        System.out.println("loadOBJ1");
        
        List<Contact> contactsProva = getListaContattiCasuali(2);
        List<Tag> tagsProva = getListaTagCasuali(2);
        
        a = AddressBook.getInstance();
        a.addManyContacts(getListaContattiCasuali(10));
        
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(pathData)))){
            oos.writeObject(contactsProva);
            oos.writeObject(tagsProva);
        }catch(IOException ex){ex.printStackTrace();}
        
        a.loadOBJ();
        
        assertEquals(contactsProva, a.getAllContacts());
        assertEquals(tagsProva, a.getAllTags());
    }
    
    /**
     * UTC 1.32 (LEGGERO)
     * 
     * La rubrica preleva solo contatti dal file Data.bin
     */
    @Test
    public void testLoadOBJ2() {
        System.out.println("loadOBJ2");
        
        List<Contact> contactsProva = getListaContattiCasuali(2);
        
        a = AddressBook.getInstance();
        a.addManyContacts(getListaContattiCasuali(10));
        
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(pathData)))){
            oos.writeObject(contactsProva);
            /* Data.bin ha sempre 2 oggetti scritti al suo interno */
            oos.writeObject(new ArrayList<Tag>());
        }catch(IOException ex){ex.printStackTrace();}
        
        a.loadOBJ();
        
        assertEquals(contactsProva, a.getAllContacts());
        assertEquals(0, a.getAllTags().size());
    }
    
    /**
     * UTC 1.33 (LEGGERO)
     * 
     * La rubrica preleva solo tag dal file Data.bin
     */
    @Test
    public void testLoadOBJ3() {
        System.out.println("loadOBJ3");
        
        Tag tagVett[] = {getTagCasuale(), getTagCasuale()};
        List<Tag> tagsProva = new ArrayList<>(Arrays.asList(tagVett));
        
        a = AddressBook.getInstance();
        a.addManyContacts(getListaContattiCasuali(10));
        
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(pathData)))){
            /* Data.bin ha sempre 2 oggetti scritti al suo interno */
            oos.writeObject(new ArrayList<Contact>());
            oos.writeObject(tagsProva);
            oos.writeObject(new ArrayList<Tag>());
        }catch(IOException ex){ex.printStackTrace();}
        
        a.loadOBJ();
        
        assertEquals(0, a.getAllContacts().size());
        assertEquals(tagsProva, a.getAllTags());
    }

    /**
     * UTC 1.34 (LEGGERO)
     */
    @Test
    public void testRemoveOBJ() {
        System.out.println("removeOBJ");
        
        a = AddressBook.getInstance();
        
        /* qui si crea Data.bin */
        a.addContact(getContattoCasuale());
        
        a.removeOBJ();
        
        assertFalse(new File(pathData).exists());
    }

    /**
     * UTC 1.35 (ONEROSO)
     */
    //@Test
    public void testSaveToDB() {
        System.out.println("saveToDB");
        
        Database database = new Database(url);
        
        /* salvo lo stato iniziale del DB */
        Collection<Contact> savedContacts = database.getAllContacts();
        database.deleteAllContacts();
        
        a = AddressBook.getInstance();
        a.setDBUrl(url);        
        a.addManyContacts(getListaContattiCasuali(2));
        a.saveToDB();
        
        List<Contact> contattiLetti = new ArrayList<>(database.getAllContacts());
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllContacts();
        database.deleteAllTags();
        if(!savedContacts.isEmpty())
            database.insertManyContacts(savedContacts);
        
        assertEquals(a.getAllContacts(), contattiLetti);
    } 
}
