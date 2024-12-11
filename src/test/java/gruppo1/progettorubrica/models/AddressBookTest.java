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
    private static final String url = 
        "mongodb+srv://rubricaContatti:tqHPmDYFftuxXE3g@mongisacluster.o8crvzq.mongodb.net/?retryWrites=true&w=majority&appName=MongisaCluster";
    
    @Before
    public void setUp() throws Exception {
        /*  (LEGGERO) */
        System.out.print("\n---Prossimo metodo da testare:  ");
        /* per testare la classe singleton devo resettare l'istanza statica di AddressBook a null */
        Field instanceField = AddressBook.class.getDeclaredField("instance");
        instanceField.setAccessible(true); // Rendi il campo accessibile
        instanceField.set(null, null); // Imposta il campo statico (null) a null per resettarlo
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
     * Quindi è previsto il salvataggio in locale nel file Data.bin.
     */
    @Test
    public void testGetInstance1() {
        System.out.println("getInstance1");
        
        a = AddressBook.getInstance();
        
        assertTrue(a.getAllContacts().isEmpty() && a.getAllTags().isEmpty());
        assertNull(a.getDBUrl());
        assertNull(a.getDB());
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
        
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Data.bin")))){
            oos.writeObject(contactsProva);
            oos.writeObject(tagsProva);
        }catch(IOException ex){ex.printStackTrace();}
        
        a = AddressBook.getInstance();
        
        assertTrue(a.getAllTags().containsAll(tagsProva));
        assertTrue(a.getAllContacts().containsAll(contactsProva));
        assertNull(a.getDBUrl());
        assertNull(a.getDB());
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
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Config.bin")))){
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
     * esiste Config.bin e il link contenutovi è valido;
     * esiste il file Data.bin.
     * 
     * Ci sono dei contatti e tag nel DB e nel file Data.bin
     * quindi l'oracolo vuole che la rubrica prelevi tali valori.
     */
    //@Test
    public void testGetInstance4() {
        System.out.println("getInstance4");
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Config.bin")))){
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
        
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Data.bin")))){
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
        
        assertEquals(contactsProvaData.size()+contactsProvaDb.size(), a.getAllContacts().size());
        assertEquals(tagsProvaData.size()+tagsProvaDb.size(), a.getAllTags().size());
        assertTrue(a.getAllContacts().containsAll(contactsProvaDb));
        assertTrue(a.getAllContacts().containsAll(contactsProvaData));        
        assertTrue(a.getAllTags().containsAll(tagsProvaDb));
        assertTrue(a.getAllTags().containsAll(tagsProvaData));
        assertEquals(a.getDBUrl(), url);
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
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("Data.bin")))){
            try {
                contattiLetti = new ArrayList<>((Collection<Contact>)ois.readObject());
            } catch (ClassNotFoundException ex) {ex.printStackTrace();}
        }catch(IOException ex){ex.printStackTrace();}
        
        assertEquals(contactsProva, a.getAllContacts());
        assertEquals(contattiLetti, a.getAllContacts());
    }
    
    /**
     * UTC 1. (MOLTO ONEROSO)
     * 
     * Il contatto non si salva in locale, bensì sul DB.
     * Quindi non esiste il file Data.bin e non si deve creare.
     */
    //@Test
    public void testAddContact2() {
        System.out.println("addContact2");
        
        List<Contact> contactsProva = getListaContattiCasuali(2);
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Config.bin")))){
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
        assertFalse(new File("Data.bin").exists());
        assertTrue(new File("Config.bin").exists());
    }
    
    /**
     * UTC 1. (ONEROSO)
     * 
     * Il contatto non si salva in locale, bensì sul DB.
     * Quindi non esiste il file Data.bin e non si deve creare.
     */
    //@Test
    public void testAddManyContacts() {
        System.out.println("addManyContacts");
        
        List<Contact> contactsProva = getListaContattiCasuali(2);
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Config.bin")))){
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
        assertFalse(new File("Data.bin").exists());
    }
    
    /**
     * UTC 1.7 (LEGGERO)
     * 
     * Il contatto si rimuove dal file Data.bin e non dal DB perchè non esiste.
     */
    //@Test
    public void testRemoveContact1() {
        System.out.println("removeContact1");
        
        Contact cVett[] = {getContattoCasuale(), getContattoCasuale()};
        
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Data.bin")))){
            oos.writeObject(new ArrayList<>(Arrays.asList(cVett)));
            oos.writeObject(new ArrayList<Tag>());
        }catch(IOException ex){ex.printStackTrace();}
        
        a = AddressBook.getInstance();
        a.removeContact(cVett[0]);
        
        List<Contact> contattiLetti = null;
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("Data.bin")))){
            try {
                contattiLetti = new ArrayList<>((Collection<Contact>)ois.readObject());
            } catch (ClassNotFoundException ex) {ex.printStackTrace();}
        }catch(IOException ex){ex.printStackTrace();}
        
        assertEquals(1, a.getAllContacts().size());
        assertTrue(a.getAllContacts().contains(cVett[1]));
        assertEquals(0, a.getAllTags().size());
        assertEquals(contattiLetti, a.getAllContacts());
    }
    
    /**
     * UTC 1.8 (ONEROSO)
     * 
     * Il contatto si rimuove dal DB e non dal file Data.bin.
     */
    //@Test
    public void testRemoveContact2() {
        System.out.println("removeContact2");
        
        Contact cVett[] = {getContattoCasuale(), getContattoCasuale()};       
        Tag tagVett[] = {getTagCasuale(), getTagCasuale(), getTagCasuale()};
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Config.bin")))){
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
     * UTC 1.9 (LEGGERO)
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
     * UTC 1.10 (LEGGERO)
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
     * UTC 1.11 (LEGGERO)
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
     * UTC 1. (LEGGERO)
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
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("Data.bin")))){
            try {
                ois.readObject();
                tagLetti = new ArrayList<>((Collection<Tag>)ois.readObject());
            } catch (ClassNotFoundException ex) {ex.printStackTrace();}
        }catch(IOException ex){ex.printStackTrace();}
        
        assertEquals(tagsProva, a.getAllTags());
        assertEquals(tagLetti, a.getAllTags());
    }
    
    /**
     * UTC 1.12 (MOLTO ONEROSO)
     * 
     * Il tag non si salva in locale, bensì sul DB.
     */
    //@Test
    public void testAddTag2() {
        System.out.println("addTag2");
        
        List<Tag> tagsProva = getListaTagCasuali(3);
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Config.bin")))){
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
        assertFalse(new File("Data.bin").exists());
    }
    
    /**
     * UTC 1. (ONEROSO)
     * 
     * I tags non si salvano in locale, bensì sul DB.
     */
    //@Test
    public void testAddManyTags() {
        System.out.println("addManyTags");
        
        List<Tag> tagsProva = getListaTagCasuali(3);
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Config.bin")))){
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
        assertFalse(new File("Data.bin").exists());
    }
    
    /**
     * UTC 1.13 (LEGGERO)
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
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("Data.bin")))){
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
     * UTC 1.14 (ONEROSO)
     * 
     * Il tag si rimuove dal DB e non dal file Data.bin.
     */
    //@Test
    public void testRemoveTag2() {
        System.out.println("removeTag2");
        
        Tag tagVett[] = {getTagCasuale(), getTagCasuale()};
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Config.bin")))){
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
        assertFalse(new File("Data.bin").exists());
        
    }

    /**
     * UTC 1.15 (ONEROSO)
     * 
     * Nessun contatto o tag nella rubrica inizialmente
     * Ipotetico scenario in cui l'utente apre la rubrica vuota e imposta subito il DB che possiede eventuali info.
     */
    //@Test
    public void testSetDBUrl1() {
        System.out.println("setDBUrl1");
        
        a = AddressBook.getInstance();
        
        a.setDBUrl(url);
        
        Database database = new Database(url);
        List<Contact> contattiLetti = new ArrayList<>(database.getAllContacts());
        List<Tag> tagsLetti = new ArrayList<>(database.getAllTags());
        
        assertTrue(new File("Config.bin").exists());
        assertFalse(new File("Data.bin").exists());
        assertEquals(url, a.getDBUrl());
        assertNotNull(a.getDB());
        assertEquals(contattiLetti, a.getAllContacts());
        assertEquals(tagsLetti, a.getAllTags());
    }
    
    /**
     * UTC 1.16 (ONEROSO)
     * 
     * Contatti o tags nel file Data.bin della rubrica inizialmente, da caricare ora sul DB.
     * Ipotetico scenario in cui l'utente carica i propri contatti sulla rubrica
     * e poi decide di aggiungere un DB e quindi li carica qui.
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
        
        assertTrue(new File("Config.bin").exists());
        assertFalse(new File("Data.bin").exists());
        assertEquals(url, a.getDBUrl());
        assertNotNull(a.getDB());
        assertEquals(contattiLetti, a.getAllContacts());
        assertEquals(tagsLetti, a.getAllTags());
    }
    
    /**
     * UTC 1. (ONEROSO)
     * 
     * Contatti o tags nella rubrica inizialmente anche se esiste già il file Config.bin, da caricare sul DB.
     * Ipotetico scenario in cui l'utente carica i propri contatti sulla rubrica perchè il link
     * che aveva inserito per il DB non era valido quindi Config.bin esiste già ma non è utilizzabile.
     * L'utente poi decide di aggiungere un DB valido e quindi carica qui le informazioni della rubrica.
     */
    //@Test
    public void testSetDBUrl3() {
        System.out.println("setDBUrl3");
        
        Contact cVett[] = {getContattoCasuale(), getContattoCasuale()};
        
        Database database = new Database(url);
        
        /* salvo lo stato iniziale del DB */
        Collection<Contact> savedContacts = database.getAllContacts();
        database.deleteAllContacts();
        
        a = AddressBook.getInstance();
        a.setDBUrl("link_non_valido.it");        
        a.addManyContacts(Arrays.asList(cVett)); /* qui è presente ancora il Data.bin */
        
        a.setDBUrl(url);
        
        List<Contact> contattiLetti = new ArrayList<>(database.getAllContacts());
        List<Tag> tagsLetti = new ArrayList<>(database.getAllTags());
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllContacts();
        if(!savedContacts.isEmpty())
            database.insertManyContacts(savedContacts);
        
        assertTrue(new File("Config.bin").exists());
        assertFalse(new File("Data.bin").exists());
        assertEquals(a.getDBUrl(), url);
        assertNotNull(a.getDB());
        assertEquals(contattiLetti, a.getAllContacts());
        assertEquals(tagsLetti, a.getAllTags());
    }
    
    /**
     * UTC 1. (ONEROSO)
     * 
     * Contatti o tags nel file Data.bin della rubrica inizialmente, da caricare ora sul DB.
     * Ipotetico scenario in cui l'utente carica i propri contatti sulla rubrica
     * e poi decide di aggiungere un DB con delle informazioni aggiuntive e quindi aggiunge Data.bin al DB
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
        
        assertTrue(new File("Config.bin").exists());
        assertFalse(new File("Data.bin").exists());
        assertEquals(a.getDBUrl(), url);
        assertNotNull(a.getDB());
        assertEquals(contattiLetti, a.getAllContacts());
        assertEquals(tagsLetti, a.getAllTags());
    }

    /**
     * UTC 1. (LEGGERO)
     * 
     * In fase di inizializzazione esiste il file Config.bin e quindi il campo dbUrl è valorizzato
     */
    @Test
    public void testGetDBUrl1() {
        System.out.println("getDBUrl1");
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Config.bin")))){
            dos.writeUTF("urlCasuale.it");
        }catch(IOException ex){ex.printStackTrace();}
        
        a = AddressBook.getInstance();
        
        assertEquals("urlCasuale.it", a.getDBUrl());
    }
    
    /**
     * UTC 1. (LEGGERO)
     * 
     * In fase di inizializzazione NON esiste il file Config.bin, ma l'utente inserisce durante la sessione l'url del DB.
     */
    @Test
    public void testGetDBUrl2() {
        System.out.println("getDBUrl2");
        
        a = AddressBook.getInstance();
        a.setDBUrl("urlCasuale.it");
        
        assertEquals("urlCasuale.it", a.getDBUrl());
        assertTrue(new File("Config.bin").exists());
    }

    /**
     * UTC 1. (LEGGERO)
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
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("Data.bin")))){
            try {
                c = new ArrayList<>((Collection<Contact>)ois.readObject());
                t = new ArrayList<>((Collection<Tag>)ois.readObject());
            } catch (ClassNotFoundException ex) {ex.printStackTrace();}
        }catch(IOException ex){ex.printStackTrace();}
        
        assertEquals(a.getAllContacts(), c);
        assertEquals(a.getAllTags(), t);
    }
    
    /**
     * UTC 1. (LEGGERO)
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
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("Data.bin")))){
            try {
                c = new ArrayList<>((Collection<Contact>)ois.readObject());
                t = new ArrayList<>((Collection<Tag>)ois.readObject());
            } catch (ClassNotFoundException ex) {ex.printStackTrace();}
        }catch(IOException ex){ex.printStackTrace();}
        
        assertEquals(a.getAllContacts(), c);
        assertEquals(a.getAllTags(), t);
    }
    
    /**
     * UTC 1. (LEGGERO)
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
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("Data.bin")))){
            try {
                c = new ArrayList<>((Collection<Contact>)ois.readObject());
                t = new ArrayList<>((Collection<Tag>)ois.readObject());
            } catch (ClassNotFoundException ex) {ex.printStackTrace();}
        }catch(IOException ex){ex.printStackTrace();}
        
        assertEquals(a.getAllContacts(), c);
        assertEquals(a.getAllTags(), t);        
    }

    /**
     * UTC 1. (LEGGERO)
     * 
     * La rubrica preleva entrambi contatti e tags dal file Data.bin
     */
    @Test
    public void testLoadOBJ1() {
        System.out.println("loadOBJ1");
        
        List<Contact> contactsProva = getListaContattiCasuali(2);
        List<Tag> tagsProva = getListaTagCasuali(2);
        
        a = AddressBook.getInstance();
        
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Data.bin")))){
            oos.writeObject(contactsProva);
            oos.writeObject(tagsProva);
        }catch(IOException ex){ex.printStackTrace();}
        
        a.loadOBJ();
        
        assertEquals(contactsProva, a.getAllContacts());
        assertEquals(tagsProva, a.getAllTags());
    }
    
    /**
     * UTC 1. (LEGGERO)
     * 
     * La rubrica preleva solo contatti dal file Data.bin
     */
    @Test
    public void testLoadOBJ2() {
        System.out.println("loadOBJ2");
        
        List<Contact> contactsProva = getListaContattiCasuali(2);
        
        a = AddressBook.getInstance();
        
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Data.bin")))){
            oos.writeObject(contactsProva);
            /* Data.bin ha sempre 2 oggetti scritti al suo interno */
            oos.writeObject(new ArrayList<Tag>());
        }catch(IOException ex){ex.printStackTrace();}
        
        a.loadOBJ();
        
        assertEquals(contactsProva, a.getAllContacts());
        assertEquals(0, a.getAllTags().size());
    }
    
    /**
     * UTC 1. (LEGGERO)
     * 
     * La rubrica preleva solo tag dal file Data.bin
     */
    @Test
    public void testLoadOBJ3() {
        System.out.println("loadOBJ3");
        
        Tag tagVett[] = {getTagCasuale(), getTagCasuale()};
        List<Tag> tagsProva = new ArrayList<>(Arrays.asList(tagVett));
        
        a = AddressBook.getInstance();
        
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Data.bin")))){
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
     * UTC 1.
     */
    //@Test
    public void testRemoveOBJ() {
        System.out.println("removeOBJ");
        fail("The test case is a prototype.");
    }

    /**
     * UTC 1.
     */
    //@Test
    public void testSaveToDB() {
        System.out.println("saveToDB");
        fail("The test case is a prototype.");
    }

    /**
     * UTC 1.
     */
    //@Test
    public void testDataToDB() {
        System.out.println("dataToDB");
        fail("The test case is a prototype.");
    }

    /**
     * UTC 1.
     */
    //@Test
    public void testLoadFromDB() {
        System.out.println("loadFromDB");
        fail("The test case is a prototype.");
    }

    /**
     * UTC 1.
     */
    //@Test
    public void testLoadConfig() {
        System.out.println("loadConfig");
        fail("The test case is a prototype.");
    }

    /**
     * UTC 1.
     */
    //@Test
    public void testSaveConfig() {
        System.out.println("saveConfig");
        fail("The test case is a prototype.");
    }

    /**
     * UTC 1.
     */
    //@Test
    public void testInitDB() {
        System.out.println("initDB");
        fail("The test case is a prototype.");
    }
    
}
