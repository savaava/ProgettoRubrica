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
        /* per testare la classe singleton devo resettare l'istanza statica di AddressBook a null */
        Field instanceField = AddressBook.class.getDeclaredField("instance");
        instanceField.setAccessible(true); // Rendi il campo accessibile
        instanceField.set(null, null); // Imposta il campo statico (null) a null per resettarlo
    }
    @After
    public void tearDown(){
        AddressBook.removeConfig();
        AddressBook.removeOBJ();
    }
    
    
    
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
    
    
     
    /**
     * UTC 1.1
     * 
     * Testa la prima apertura dell'applicazione (no Config.bin, no Data.bin, no dbUrl).
     */
    @Test
    public void testGetInstance1() {
        System.out.println("-getInstance1");
        
        a = AddressBook.getInstance();
        
        assertTrue(a.getAllContacts().isEmpty() && a.getAllTags().isEmpty());
        assertNull(a.getDBUrl());
        assertNull(a.getDB());
    }
    
    /**
     * UTC 1.2
     * 
     * Testa un'inizializzazione dell'applicazione nello scenario in cui esiste solo Data.bin.
     */
    @Test
    public void testGetInstance2() {
        System.out.println("-getInstance2");
        
        Contact cVett[] = {getContattoCasuale(), getContattoCasuale()};              
        List<Contact> contactsProva = new ArrayList<>(Arrays.asList(cVett));        
        Tag tagVett[] = {getTagCasuale(), getTagCasuale(), getTagCasuale()};
        List<Tag> tagsProva = new ArrayList<>(Arrays.asList(tagVett));
        
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
     * UTC 1.3
     * 
     * Testa un'inizializzazione dell'applicazione nello scenario in cui
     * esiste solo Config.bin e il link contenuto è valido.
     * 
     * Nel database ci sono dei contatti e tag 
     * quindi l'oracolo vuole che la rubrica prelevi tali valori.
     */
    //@Test
    public void testGetInstance3() {
        System.out.println("-getInstance3");
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Config.bin")))){
            dos.writeUTF(url);
        }catch(IOException ex){ex.printStackTrace();}
        
        Contact cVett[] = {getContattoCasuale(), getContattoCasuale()};
        List<Contact> contactsProva = new ArrayList<>(Arrays.asList(cVett));
        Tag tagVett[] = {getTagCasuale(), getTagCasuale(), getTagCasuale()};
        List<Tag> tagsProva = new ArrayList<>(Arrays.asList(tagVett));
        
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
        
        assertEquals(a.getAllContacts().size(), cVett.length);
        assertEquals(a.getAllTags().size(), tagVett.length);
        assertTrue(a.getAllTags().containsAll(tagsProva));
        assertTrue(a.getAllContacts().containsAll(contactsProva));
        assertEquals(a.getDBUrl(), url);
        assertNotNull(a.getDB());
    }
    
    /**
     * UTC 1.4
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
        System.out.println("-getInstance4");
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Config.bin")))){
            dos.writeUTF(url);
        }catch(IOException ex){ex.printStackTrace();}
        
        Contact cVettData[] = {getContattoCasuale(), getContattoCasuale()};
        List<Contact> contactsProvaData = new ArrayList<>(Arrays.asList(cVettData));
        Contact cVettDb[] = {getContattoCasuale(), getContattoCasuale()};
        List<Contact> contactsProvaDb = new ArrayList<>(Arrays.asList(cVettDb));        
        Tag tagVettData[] = {getTagCasuale(), getTagCasuale(), getTagCasuale()};
        List<Tag> tagsProvaData = new ArrayList<>(Arrays.asList(tagVettData));
        Tag tagVettDb[] = {getTagCasuale(), getTagCasuale(), getTagCasuale()};
        List<Tag> tagsProvaDb = new ArrayList<>(Arrays.asList(tagVettDb));
        
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
        
        assertEquals(a.getAllContacts().size(), cVettData.length+cVettDb.length);
        assertEquals(a.getAllTags().size(), tagVettData.length+tagVettDb.length);
        assertTrue(a.getAllContacts().containsAll(contactsProvaDb));
        assertTrue(a.getAllContacts().containsAll(contactsProvaData));        
        assertTrue(a.getAllTags().containsAll(tagsProvaDb));
        assertTrue(a.getAllTags().containsAll(tagsProvaData));
        assertEquals(a.getDBUrl(), url);
        assertNotNull(a.getDB());
    }

    /**
     * UTC 1.5
     */
    @Test
    public void testGetAllContacts() {
        System.out.println("-getAllContacts");
        
        Contact cVett[] = {getContattoCasuale(), getContattoCasuale()};
        List<Contact> contactsProva = new ArrayList<>(Arrays.asList(cVett));
        
        a = AddressBook.getInstance();
        a.addManyContacts(contactsProva);
        
        assertEquals(a.getAllContacts(), contactsProva);
    }
    
    /**
     * UTC 1.6
     * 
     * Il contatto si salva in locale, perchè non esiste il DB.
     */
    @Test
    public void testAddContact1() {
        System.out.println("-addContact1");
        
        Contact cVett[] = {getContattoCasuale(), getContattoCasuale()};
        List<Contact> contactsProva = new ArrayList<>(Arrays.asList(cVett));
        
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
        
        assertEquals(a.getAllContacts(), contactsProva);
        assertEquals(a.getAllContacts(), contattiLetti);
    }
    
    /**
     * UTC 1.
     * 
     * Il contatto non si salva in locale, bensì sul DB.
     */
    //@Test
    public void testAddContact2() {
        System.out.println("-addContact2");
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Config.bin")))){
            dos.writeUTF(url);
        }catch(IOException ex){ex.printStackTrace();}
        
        Database database = new Database(url);
        
        /* salvo lo stato iniziale del DB */
        Collection<Contact> savedContacts = database.getAllContacts();
        database.deleteAllContacts();
        
        Contact cVett[] = {getContattoCasuale(), getContattoCasuale()};
        List<Contact> contactsProva = new ArrayList<>(Arrays.asList(cVett));
        a = AddressBook.getInstance();
        for(Contact c : contactsProva){
            a.addContact(c);
        }
        
        List<Contact> contattiLetti = new ArrayList<>(database.getAllContacts());
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllContacts();
        database.deleteAllTags();
        if(!savedContacts.isEmpty())
            database.insertManyContacts(savedContacts);
        
        assertEquals(a.getAllContacts(), contattiLetti);
    }
    
    /**
     * UTC 1.7
     * 
     * Il contatto si rimuove dal file Data.bin e non dal DB perchè non esiste.
     */
    @Test
    public void testRemoveContact1() {
        System.out.println("-removeContact1");
        
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
        
        assertEquals(a.getAllContacts().size(), 1);
        assertTrue(a.getAllContacts().contains(cVett[1]));
        assertEquals(a.getAllTags().size(), 0);
        assertEquals(a.getAllContacts(), contattiLetti);
    }
    
    /**
     * UTC 1.8
     * 
     * Il contatto si rimuove dal DB e non dal file Data.bin.
     */
    //@Test
    public void testRemoveContact2() {
        System.out.println("-removeContact2");
        
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
        a.addManyContacts(new ArrayList<>(Arrays.asList(cVett)));
        a.addManyTags(new ArrayList<>(Arrays.asList(tagVett)));
        a.removeContact(cVett[0]);
        
        List<Contact> contattiLetti = new ArrayList<>(database.getAllContacts());
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllContacts();
        database.deleteAllTags();
        if(!savedContacts.isEmpty())
            database.insertManyContacts(savedContacts);
        if(!savedTags.isEmpty())
            database.insertManyTags(savedTags);
        
        assertEquals(a.getAllContacts().size(), 1);
        assertTrue(a.getAllContacts().contains(cVett[1]));
        assertEquals(a.getAllTags().size(), tagVett.length);
        assertEquals(a.getAllContacts(), contattiLetti);
    }
    
    /**
     * UTC 1.9
     */
    @Test
    public void testGetAllTags() {
        System.out.println("-getAllTags");
        
        Tag tagVett[] = {getTagCasuale(), getTagCasuale(), getTagCasuale()};
        List<Tag> tagsProva = new ArrayList<>(Arrays.asList(tagVett));
        
        a = AddressBook.getInstance();
        a.addManyTags(tagsProva);
        
        assertEquals(a.getAllTags(), tagsProva);
    }
    
    /**
     * UTC 1.10
     * 
     * Tag presente
     */
    @Test
    public void testGetTag1() {
        System.out.println("-getTag1");
        
        Tag tagVett[] = {getTagCasuale(111), getTagCasuale(222), getTagCasuale(333)};
        
        a = AddressBook.getInstance();
        a.addManyTags(Arrays.asList(tagVett));
        
        Tag tagPrelevato = a.getTag(222);
        
        assertEquals(tagPrelevato, tagVett[1]);
    }
    
    /**
     * UTC 1.11
     * 
     * Tag non presente
     */
    @Test
    public void testGetTag2() {
        System.out.println("-getTag2");
        
        Tag tagVett[] = {getTagCasuale(111), getTagCasuale(222), getTagCasuale(333)};
        
        a = AddressBook.getInstance();
        a.addManyTags(Arrays.asList(tagVett));
        
        Tag tagPrelevato = a.getTag(123);
        
        assertNull(tagPrelevato);
    }
    
    /**
     * UTC 1.
     * 
     * Il contatto si salva in locale, perchè non esiste il DB.
     */
    @Test
    public void testAddTag1(){
        System.out.println("-addTag1");
        
        Tag tagVett[] = {getTagCasuale(), getTagCasuale(), getTagCasuale()};
        List<Tag> tagsProva = new ArrayList<>(Arrays.asList(tagVett));
        
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
        
        assertEquals(a.getAllTags(), tagsProva);
        assertEquals(a.getAllTags(), tagLetti);
    }
    
    /**
     * UTC 1.12
     * 
     * Il tag non si salva in locale, bensì sul DB.
     */
    //@Test
    public void testAddTag2() {
        System.out.println("-addTag2");
        
        Tag tagVett[] = {getTagCasuale(), getTagCasuale(), getTagCasuale()};
        List<Tag> tagsProva = new ArrayList<>(Arrays.asList(tagVett));
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Config.bin")))){
            dos.writeUTF(url);
        }catch(IOException ex){ex.printStackTrace();}
        
        Database database = new Database(url);
        
        /* salvo lo stato iniziale del DB */
        Collection<Tag> savedTags = database.getAllTags();
        database.deleteAllContacts();
        
        a = AddressBook.getInstance();
        for(Tag tag : tagsProva){
            a.addTag(tag);
        }
        
        List<Tag> tagLetti = new ArrayList<>(database.getAllTags());
        
        /* ripristino lo stato iniziale del DB */
        database.deleteAllTags();
        if(!savedTags.isEmpty())
            database.insertManyTags(savedTags);
        
        assertEquals(a.getAllTags(), tagsProva);    
        assertEquals(a.getAllTags(), tagLetti);
    }
    
    /**
     * UTC 1.13
     * 
     * Il tag si rimuove dal file Data.bin e non dal DB perchè non esiste.
     */
    @Test
    public void testRemoveTag1() {
        System.out.println("-removeTag1");
        
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
        
        assertEquals(a.getAllTags().size(), 2);
        assertTrue(a.getAllTags().contains(tagVett[0]) && a.getAllTags().contains(tagVett[2]));
        assertEquals(a.getAllTags(), tagLetti);
    }
    
    /**
     * UTC 1.14
     * 
     * Il tag si rimuove dal DB e non dal file Data.bin.
     */
    @Test
    public void testRemoveTag2() {
        System.out.println("-removeTag2");
        
        Tag tagVett[] = {getTagCasuale(), getTagCasuale(), getTagCasuale()};
        
        a = AddressBook.getInstance();
        a.addManyTags(new ArrayList<>(Arrays.asList(tagVett)));
        
        assertEquals(a.getAllTags().size(), 3);
        assertTrue(a.getAllTags().contains(tagVett[0]) 
                && a.getAllTags().contains(tagVett[1]) 
                && a.getAllTags().contains(tagVett[2]));
    }

    /**
     * UTC 1.15
     * 
     * Nessun contatto o tag nella rubrica inizialmente
     * Ipotetico scenario in cui l'utente apre la rubrica vuota e imposta subito il DB.
     */
    //@Test
    public void testSetDBUrl() {
        System.out.println("-setDBUrl1");
        
        a = AddressBook.getInstance();
        
        a.setDBUrl(url);
        
        assertTrue(new File("Config.bin").exists());
        assertEquals(a.getDBUrl(), url);
        assertNotNull(a.getDB());
    }
    
    /**
     * UTC 1.16
     * 
     * Contatti o tags nella rubrica inizialmente, da caricare sul DB.
     * Ipotetico scenario in cui l'utente carica i propri contatti sulla rubrica e poi decide di impostare un DB.
     */
    //@Test
    public void testSetDBUr2() {
        System.out.println("-setDBUrl2");
        
        a = AddressBook.getInstance();
        
        a.setDBUrl(url);
        
        assertTrue(new File("Config.bin").exists());
        assertEquals(a.getDBUrl(), url);
        assertNotNull(a.getDB());
    }

    /**
     * Test of getDBUrl method, of class AddressBook.
     */
    //@Test
    public void testGetDBUrl() {
        System.out.println("getDBUrl");
        fail("The test case is a prototype.");
    }

    /**
     * Test of saveOBJ method, of class AddressBook.
     */
    //@Test
    public void testSaveOBJ() {
        System.out.println("saveOBJ");
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadOBJ method, of class AddressBook.
     */
    //@Test
    public void testLoadOBJ() {
        System.out.println("loadOBJ");
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeOBJ method, of class AddressBook.
     */
    //@Test
    public void testRemoveOBJ() {
        System.out.println("removeOBJ");
        fail("The test case is a prototype.");
    }

    /**
     * Test of saveToDB method, of class AddressBook.
     */
    //@Test
    public void testSaveToDB() {
        System.out.println("saveToDB");
        fail("The test case is a prototype.");
    }

    /**
     * Test of dataToDB method, of class AddressBook.
     */
    //@Test
    public void testDataToDB() {
        System.out.println("dataToDB");
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadFromDB method, of class AddressBook.
     */
    //@Test
    public void testLoadFromDB() {
        System.out.println("loadFromDB");
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadConfig method, of class AddressBook.
     */
    //@Test
    public void testLoadConfig() {
        System.out.println("loadConfig");
        fail("The test case is a prototype.");
    }

    /**
     * Test of saveConfig method, of class AddressBook.
     */
    //@Test
    public void testSaveConfig() {
        System.out.println("saveConfig");
        fail("The test case is a prototype.");
    }

    /**
     * Test of initDB method, of class AddressBook.
     */
    //@Test
    public void testInitDB() {
        System.out.println("initDB");
        fail("The test case is a prototype.");
    }
    
}
