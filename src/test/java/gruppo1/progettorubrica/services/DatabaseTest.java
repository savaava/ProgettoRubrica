package gruppo1.progettorubrica.services;

import gruppo1.progettorubrica.models.Contact;
import gruppo1.progettorubrica.models.Tag;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.bson.Document;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class DatabaseTest {

    private Database database;
    private static final String url = "mongodb+srv://rubricaContatti:tqHPmDYFftuxXE3g@mongisacluster.o8crvzq.mongodb.net/?retryWrites=true&w=majority&appName=MongisaCluster";

    @Test
    public void testConstructor_validUrl() {
        database = new Database(url);
        assertNotNull(database);
    }

    @Test
    public void testVerifyDBUrl_validUrl() {
        String validUrl = url;
        assertTrue(Database.verifyDBUrl(validUrl));
    }

    @Test
    public void testVerifyDBUrl_invalidUrl() {
        String invalidUrl = "aaaaaaaaaaaa";
        assertFalse(Database.verifyDBUrl(invalidUrl));
    }

    @Test
    public void testVerifyDBUrl_nullUrl() {
        String nullUrl = null;
        assertFalse(Database.verifyDBUrl(nullUrl));
    }

    @Test
    public void testVerifyDBUrl_emptyUrl() {
        String emptyUrl = "";
        assertFalse(Database.verifyDBUrl(emptyUrl));
    }

    @Test
    public void testInsertContact() throws IOException {
        database = new Database(url);

        String name = "Mario";
        String surname = "Rossi";
        String[] numbers = {"3229384403","4578906546","5467965434"};
        String[] emails = {"mariorossi@gmail.com","rossimario@gmail.com","mrossi@gmail.com"};
        Integer[] tags = {32,3,43};

        Contact c = new Contact(name,surname);
        c.setNumbers(numbers);
        c.setEmails(emails);
        c.setProfilePicture(Converter.imageViewToByteArray(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/FotoProfilo1.jpg"))))));
        for(int tag : tags) {
            c.addTagIndex(tag);
        }

        database.insertContact(c);
        Collection<Contact> result = database.getAllContacts();

        database.removeContact(c);
        assertTrue(result.contains(c));
    }

    @Test
    public void testRemoveContact() {
        database = new Database(url);

        String name = "Mario";
        String surname = "Rossi";

        Contact c = new Contact(name,surname);

        database.insertContact(c);
        database.removeContact(c);
        Collection<Contact> contactList = database.getAllContacts();
        assertFalse(contactList.contains(c));
    }

    @Test
    public void testInsertTag() {
        database = new Database(url);

        String description = "FamigliaTest";

        Tag t = new Tag(description);

        database.insertTag(t);
        Collection<Tag> result = database.getAllTags();

        database.removeTag(t);
        assertTrue(result.contains(t));
    }

    @Test
    public void testRemoveTag() {
        database = new Database(url);

        String description = "FamigliaTest";

        Tag t = new Tag(description);

        database.insertTag(t);
        database.removeTag(t);
        Collection<Tag> result = database.getAllTags();

        assertFalse(result.contains(t));
    }

    @Test
    public void testGetAllContacts() {
        database = new Database(url);
        //Salvo lo stato iniziale del database
        Collection<Contact> savedContacts = database.getAllContacts();
        database.deleteAllContacts();

        Contact c1 = new Contact("Mario","Rossi");
        Contact c2 = new Contact("Luca","Verdi");
        Contact c3 = new Contact("Paolo","Bianchi");

        database.insertContact(c1);
        database.insertContact(c2);
        database.insertContact(c3);

        Collection<Contact> result = database.getAllContacts();

        //Ripristino lo stato iniziale del database
        database.deleteAllContacts();

        if(!savedContacts.isEmpty())
            database.insertManyContacts(savedContacts);

        assertTrue(result.contains(c1));
        assertTrue(result.contains(c2));
        assertTrue(result.contains(c3));
    }

    @Test
    public void testGetAllTags() {
        database = new Database(url);
        //Salvo lo stato iniziale del database
        Collection<Tag> savedTags = database.getAllTags();
        database.deleteAllTags();

        Tag t1 = new Tag("Famiglia");
        Tag t2 = new Tag("Amici");
        Tag t3 = new Tag("Lavoro");

        database.insertTag(t1);
        database.insertTag(t2);
        database.insertTag(t3);

        Collection<Tag> result = database.getAllTags();

        //Ripristino lo stato iniziale del database
        database.deleteAllTags();
        if(!savedTags.isEmpty())
            database.insertManyTags(savedTags);

        assertTrue(result.contains(t1));
        assertTrue(result.contains(t2));
        assertTrue(result.contains(t3));
    }

    @Test
    public void testInsertManyContacts() {
        database = new Database(url);

        //Salvo lo stato iniziale del database
        Collection<Contact> savedContacts = database.getAllContacts();
        database.deleteAllContacts();

        List<Contact> contacts = new ArrayList<>();

        Contact c1 = new Contact("Mario","Rossi");
        Contact c2 = new Contact("Luca","Verdi");
        Contact c3 = new Contact("Paolo","Bianchi");

        contacts.add(c1);
        contacts.add(c2);
        contacts.add(c3);

        database.insertManyContacts(contacts);
        Collection<Contact> result = database.getAllContacts();

        //Ripristino lo stato iniziale del database
        database.removeContact(c1);
        database.removeContact(c2);
        database.removeContact(c3);

        if(!savedContacts.isEmpty())
            database.insertManyContacts(savedContacts);

        assertTrue(result.contains(c1));
        assertTrue(result.contains(c2));
        assertTrue(result.contains(c3));
    }

    @Test
    public void testInsertManyTags() {
        database = new Database(url);

        //Salvo lo stato iniziale del database
        Collection<Tag> savedTags = database.getAllTags();
        database.deleteAllTags();

        List<Tag> tags = new ArrayList<>();

        Tag t1 = new Tag("Famiglia");
        Tag t2 = new Tag("Amici");
        Tag t3 = new Tag("Lavoro");

        tags.add(t1);
        tags.add(t2);
        tags.add(t3);

        database.insertManyTags(tags);
        Collection<Tag> result = database.getAllTags();

        //Ripristino lo stato iniziale del database
        database.removeTag(t1);
        database.removeTag(t2);
        database.removeTag(t3);

        if(!savedTags.isEmpty())
            database.insertManyTags(savedTags);

        assertTrue(result.contains(t1));
        assertTrue(result.contains(t2));
        assertTrue(result.contains(t3));
    }

    @Test
    public void testDeleteAllContacts() {
        database = new Database(url);

        //Salvo lo stato iniziale del database
        Collection<Contact> savedContacts = database.getAllContacts();
        database.deleteAllContacts();

        Contact c1 = new Contact("Mario","Rossi");
        Contact c2 = new Contact("Luca","Verdi");
        Contact c3 = new Contact("Paolo","Bianchi");

        database.insertContact(c1);
        database.insertContact(c2);
        database.insertContact(c3);

        database.deleteAllContacts();
        Collection<Contact> result = database.getAllContacts();

        //Ripristino lo stato iniziale del database
        if(!savedContacts.isEmpty())
            database.insertManyContacts(savedContacts);

        assertFalse(result.contains(c1));
        assertFalse(result.contains(c2));
        assertFalse(result.contains(c3));
    }

    @Test
    public void testDeleteAllTags() {
        database = new Database(url);

        //Salvo lo stato iniziale del database
        Collection<Tag> savedTags = database.getAllTags();
        database.deleteAllTags();

        Tag t1 = new Tag("Famiglia");
        Tag t2 = new Tag("Amici");
        Tag t3 = new Tag("Lavoro");

        database.insertTag(t1);
        database.insertTag(t2);
        database.insertTag(t3);

        database.deleteAllTags();
        Collection<Tag> result = database.getAllTags();

        //Ripristino lo stato iniziale del database
        if(!savedTags.isEmpty())
            database.insertManyTags(savedTags);

        assertFalse(result.contains(t1));
        assertFalse(result.contains(t2));
        assertFalse(result.contains(t3));
    }

    @Test
    public void testContactToDocument() throws IOException {
        database = new Database(url);

        String name = "Mario";
        String surname = "Rossi";
        String[] numbers = {"3229384403","4578906546","5467965434"};
        String[] emails = {"mariorossi@gmail.com"};
        Byte[] profilePicture = Converter.imageViewToByteArray(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/FotoProfilo1.jpg")))));
        Integer[] tags = {32,3,43};

        Contact c = new Contact(name,surname);
        c.setNumbers(numbers);
        c.setEmails(emails);
        c.setProfilePicture(profilePicture);
        for(int tag : tags) {
            c.addTagIndex(tag);
        }

        Document doc = database.contactToDocument(c);

        assertEquals(name, doc.get("name"));
        assertEquals(surname, doc.get("surname"));
        assertArrayEquals(doc.getList("numbers", String.class).toArray(new String[0]), numbers);
        assertArrayEquals(doc.getList("emails", String.class).toArray(new String[0]), emails);
        assertArrayEquals(Converter.stringToByteArray(doc.getString("image")), profilePicture);
        assertArrayEquals(doc.getList("tagIndexes", Integer.class).toArray(new Integer[0]), tags);
    }

    @Test
    public void testDocumentToContact() throws IOException {
        database = new Database(url);
        Document doc = new Document();

        String name = "Mario";
        String surname = "Rossi";
        String[] numbers = {"3229384403","4578906546","5467965434"};
        String[] emails = {"mrossi@gmail.com"};
        String profilePicture = Converter.byteArrayToString(Converter.imageViewToByteArray(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/FotoProfilo1.jpg"))))));
        Integer[] tags = {32};

        doc.append("name", name);
        doc.append("surname", surname);
        doc.append("numbers", Arrays.asList(numbers));
        doc.append("emails", Arrays.asList(emails));
        doc.append("image", profilePicture);
        doc.append("tagIndexes", Arrays.asList(tags));

        Contact c = database.documentToContact(doc);

        assertEquals(name, c.getName());
        assertEquals(surname, c.getSurname());

        assertArrayEquals(c.getNumbers(), numbers);
        assertArrayEquals(c.getEmails(), emails);
        assertEquals(profilePicture, Converter.byteArrayToString(c.getProfilePicture()));
        assertTrue(c.getAllTagIndexes().containsAll(Arrays.asList(tags)));
    }

    @Test
    public void testTagToDocument() {
        database = new Database(url);

        String description = "Famiglia";

        Tag t = new Tag(description);
        int id = t.getId();

        Document doc = database.tagToDocument(t);

        assertEquals(description, doc.get("description"));
        assertEquals(id, doc.get("_id"));
    }

    @Test
    public void testDocumentToTag() {
        database = new Database(url);
        Document doc = new Document();

        String description = "Famiglia";
        int id = 1;

        doc.append("description", description);
        doc.append("_id", id);

        Tag t = database.documentToTag(doc);

        assertEquals(description, t.getDescription());
        assertEquals(id, t.getId());
    }
}
