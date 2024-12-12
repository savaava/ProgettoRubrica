
package gruppo1.progettorubrica.services;

import gruppo1.progettorubrica.models.Contact;
import gruppo1.progettorubrica.services.Converter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;


public class ConverterTest {

    private File csvFile;
    
    private File vcfFile;

    @Before
    public void setUp() throws IOException {
        csvFile = File.createTempFile("contacts", ".csv");
        vcfFile = File.createTempFile("contacts", ".vcf");
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("Name,Surname,TEL1,TEL2,TEL3,EMAIL1,EMAIL2,EMAIL3,PHOTO\n");
            writer.write("Luca,Rossi,1234567890,0987654321,1122334455,l.rossi@gmail.com,rossil@outlook.com,lucarossi@alice.it,SGVsbG8gd29ybGQ=\n");
            writer.write("Mario,Grigi,2233445566,6655443322,7788990011,m.grigi@gmail.com,,grigimar@outlook.com,SGVsbG8gd29ybGQ=\n");
        }
    }

    @Test
    public void testParseCSV() throws IOException {
        Collection<Contact> contacts = Converter.parseCSV(csvFile);
        assertNotNull(contacts);
        assertEquals(2, contacts.size());

        Contact[] contactArray = contacts.toArray(new Contact[0]);

        Contact contact1 = contactArray[0];
        assertEquals("Luca", contact1.getName());
        assertEquals("Rossi", contact1.getSurname());
        assertArrayEquals(new String[]{"1234567890", "0987654321", "1122334455"}, contact1.getNumbers());
        assertArrayEquals(new String[]{"l.rossi@gmail.com","rossil@outlook.com","lucarossi@alice.it"}, contact1.getEmails());
        assertArrayEquals("Hello world".getBytes(), contact1.getProfilePicture());

        Contact contact2 = contactArray[1];
        assertEquals("Mario", contact2.getName());
        assertEquals("Grigi", contact2.getSurname());
        assertArrayEquals(new String[]{"2233445566", "6655443322", "7788990011"}, contact2.getNumbers());
        assertArrayEquals(new String[]{"m.grigi@gmail.com","","grigimar@outlook.com"}, contact2.getEmails());
        assertArrayEquals("Hello world".getBytes(), contact2.getProfilePicture());
    }

    @Test
    public void testParseCSVEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("empty", ".csv");
        Collection<Contact> contacts = Converter.parseCSV(emptyFile);
        assertNotNull(contacts);
        assertTrue(contacts.isEmpty());
    }

    @Test
    public void testParseCSVInvalidFormat() throws IOException {
        File invalidFile = File.createTempFile("invalid", ".csv");
        try (FileWriter writer = new FileWriter(invalidFile)) {
            writer.write("Invalid,Content\n");
        }
        Collection<Contact> contacts = Converter.parseCSV(invalidFile);
        assertNotNull(contacts);
        assertTrue(contacts.isEmpty());
    }
    
    @Test
    public void testParseVCard_ValidVCard() throws IOException {
        File tempFile = File.createTempFile("test", ".vcf");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("BEGIN:VCARD\n");
            writer.write("VERSION:3.0\n");
            writer.write("N:Rossi;Luca;\n");
            writer.write("FN:Luca Rossi\n");
            writer.write("TEL;TYPE=CELL:1234567890\n");
            writer.write("TEL;TYPE=CELL:0987654321\n");
            writer.write("EMAIL;TYPE=HOME:l.rossi@gmail.com\n");
            writer.write("EMAIL;TYPE=HOME:rossil@outlook.com\n");
            writer.write("PHOTO:9jIV2379LJ84fsbOHFhgsh\n");
            writer.write("END:VCARD\n");
        }

        Collection<Contact> contacts = Converter.parseVCard(tempFile);
        assertEquals(1, contacts.size());

        Contact contact = contacts.iterator().next();
        assertEquals("Luca", contact.getName());
        assertEquals("Rossi", contact.getSurname());
        assertEquals("1234567890", contact.getNumbers()[0]);
        assertEquals("0987654321", contact.getNumbers()[1]);
        assertEquals("l.rossi@gmail.com", contact.getEmails()[0]);
        assertEquals("rossil@outlook.com", contact.getEmails()[1]);
        byte[] expectedProfilePicture = Base64.getDecoder().decode("9jIV2379LJ84fsbOHFhgsh");
        assertArrayEquals(expectedProfilePicture, contact.getProfilePicture());

        tempFile.delete();
    }

    @Test
    public void testParseVCard_EmptyFile() throws IOException {
        File tempFile = File.createTempFile("test", ".vcf");

        Collection<Contact> contacts = Converter.parseVCard(tempFile);
        assertTrue(contacts.isEmpty());

        tempFile.delete();
    }

    @Test
    public void testParseVCard_InvalidVCard() throws IOException {
        File tempFile = File.createTempFile("test", ".vcf");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("INVALID:VCARD\n");
        }

        Collection<Contact> contacts = Converter.parseVCard(tempFile);
        assertTrue(contacts.isEmpty());

        tempFile.delete();
    }
    
    @Test
    public void testOnExportCSV() throws IOException {
        // Create a temporary file
        File tempFile = File.createTempFile("contacts", ".csv");
        tempFile.deleteOnExit();

        List<Contact> contacts = new ArrayList<>();
        contacts = this.createContacts(); 

        // Export contacts to CSV
        Converter.onExportCSV(contacts, tempFile);

        // Read the file content
        List<String> lines = Files.readAllLines(tempFile.toPath());
        // Verify the content
        assertTrue(lines.get(0).contains("Name,Surname,TEL1,TEL2,TEL3,EMAIL1,EMAIL2,EMAIL3,PHOTO,"));
        assertTrue(lines.get(1).contains("Luca,Rossi,1234567890,0987654321,1122334455,l.rossi@gmail.com,rossil@outlook.com,lucarossi@alice.it," + Base64.getEncoder().encodeToString("profilePicture1".getBytes()) + ","));
        assertEquals("Mario,Grigi,1029384756,6655443322,7788990011,m.grigi@gmail.com,grigimar@outlook.com,," + Base64.getEncoder().encodeToString("profilePicture2".getBytes()) + ",", lines.get(2));
        //assertTrue(lines.get(2).contains("Mario,Grigi,1029384756,6655443322,7788990011,m.grigi@gmail.com,grigimar@outlook.com,," + Base64.getEncoder().encodeToString("profilePicture2".getBytes()) + ","));
    }
    
    @Test
    public void testOnExportVCard() throws IOException {
        // Create a temporary file to write the VCard data
        File tempFile = Files.createTempFile("contacts", ".vcf").toFile();
        tempFile.deleteOnExit();

        // Create a list of contacts
        List<Contact> contacts = new ArrayList<>();
        contacts = this.createContacts();

        // Export the contacts to the VCard file
        Converter.onExportVCard(contacts, tempFile);

        // Read the file content
        String content = new String(Files.readAllBytes(tempFile.toPath()));

        // Verify the content
        assertTrue(content.contains("BEGIN:VCARD"));
        assertTrue(content.contains("VERSION:3.0"));
        assertTrue(content.contains("FN:Luca Rossi"));
        assertTrue(content.contains("N: Rossi;Luca;"));
        assertTrue(content.contains("TEL; TYPE:1234567890"));
        assertTrue(content.contains("TEL; TYPE:0987654321"));
        assertTrue(content.contains("TEL; TYPE:1122334455"));
        assertTrue(content.contains("EMAIL; TYPE:l.rossi@gmail.com"));
        assertTrue(content.contains("EMAIL; TYPE:rossil@outlook.com"));
        assertTrue(content.contains("EMAIL; TYPE:lucarossi@alice.it"));
        assertTrue(content.contains(Base64.getEncoder().encodeToString("profilePicture1".getBytes())));
        assertTrue(content.contains("FN:Mario Grigi"));
        assertTrue(content.contains("N: Grigi;Mario;"));
        assertTrue(content.contains("TEL; TYPE:1029384756"));
        assertTrue(content.contains("TEL; TYPE:6655443322"));
        assertTrue(content.contains("TEL; TYPE:7788990011"));
        assertTrue(content.contains("EMAIL; TYPE:m.grigi@gmail.com"));
        assertTrue(content.contains("EMAIL; TYPE:grigimar@outlook.com"));
        assertTrue(content.contains(Base64.getEncoder().encodeToString("profilePicture2".getBytes())));
        assertTrue(content.contains("END:VCARD"));
    }
    
    
    private List<Contact> createContacts(){
        List<Contact> contacts = new ArrayList<>();
        Contact contact1 = new Contact("Luca", "Rossi");
        contact1.setNumbers(new String[]{"1234567890", "0987654321", "1122334455"});
        contact1.setEmails(new String[]{"l.rossi@gmail.com","rossil@outlook.com","lucarossi@alice.it"});
        contact1.setProfilePicture("profilePicture1".getBytes());
        contacts.add(contact1);

        Contact contact2 = new Contact("Mario", "Grigi");
        contact2.setNumbers(new String[]{"1029384756", "6655443322", "7788990011"});
        contact2.setEmails(new String[]{"m.grigi@gmail.com","grigimar@outlook.com",""});
        contact2.setProfilePicture("profilePicture2".getBytes());
        contacts.add(contact2);
        
        return contacts;
    }
}