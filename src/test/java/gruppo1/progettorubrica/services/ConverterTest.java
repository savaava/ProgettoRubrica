/*
package gruppo1.progettorubrica.services;

import gruppo1.progettorubrica.models.Contact;
import gruppo1.progettorubrica.services.Converter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import org.junit.*;
import static org.junit.Assert.*;


public class ConverterTest {

    private File csvFile;
    
    private File vcfFile;

    @Before
    public void setUp() throws IOException {
        
    }

    ///UTC 3.1
    @Test
    public void testParseCSV1() throws IOException {
        this.writeCsv(csvFile);
        Collection<Contact> contacts = Converter.parseCSV(csvFile);
        assertNotNull(contacts);
        assertEquals(2, contacts.size());
    }
    
    ///UTC 3.2
    @Test
    public void testParseCSV2() throws IOException {
        this.writeCsv(csvFile);
        Collection<Contact> contacts = Converter.parseCSV(csvFile);
        Contact[] contactArray = contacts.toArray(new Contact[0]);

        Contact contact1 = contactArray[0];
        Contact contact2 = contactArray[1];
        assertEquals("Luca", contact1.getName());
        assertEquals("Rossi", contact1.getSurname());
        assertArrayEquals(new String[]{"1234567890", "0987654321", "1122334455"}, contact1.getNumbers());
        assertArrayEquals(new String[]{"l.rossi@gmail.com","rossil@outlook.com","lucarossi@alice.it"}, contact1.getEmails());
        assertArrayEquals("Hello world".getBytes(), contact1.getProfilePicture());
        assertEquals("Mario", contact2.getName());
        assertEquals("Grigi", contact2.getSurname());
        assertArrayEquals(new String[]{"2233445566", "6655443322", "7788990011"}, contact2.getNumbers());
        assertArrayEquals(new String[]{"m.grigi@gmail.com","grigimar@outlook.com",""}, contact2.getEmails());
        assertArrayEquals("Hello world".getBytes(), contact2.getProfilePicture());
    }

    ///UTC 3.3
    @Test
    public void testParseCSVEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("empty", ".csv");
        Collection<Contact> contacts = Converter.parseCSV(emptyFile);
        assertNotNull(contacts);
        assertTrue(contacts.isEmpty());
    }

    ///UTC 3.4
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
    
    ///UTC 3.5
    @Test
    public void testParseVCard_ValidVCard1() throws IOException {
        this.writeVcf(vcfFile);
        Collection<Contact> contacts = Converter.parseVCard(vcfFile);
        assertEquals(2, contacts.size());
    }
    
    ///UTC 3.6
    @Test
    public void testParseVCard_ValidVCard2() throws IOException {
        this.writeVcf(vcfFile);
        Collection<Contact> contacts = Converter.parseVCard(vcfFile);
        Contact[] contactArray = contacts.toArray(new Contact[0]);
        Contact contact1 = contactArray[0];
        Contact contact2 = contactArray[1];
        
        assertEquals("Luca", contact1.getName());
        assertEquals("Rossi", contact1.getSurname());
        assertEquals("1234567890", contact1.getNumbers()[0]);
        assertEquals("0987654321", contact1.getNumbers()[1]);
        assertEquals("1122334455", contact1.getNumbers()[2]);
        assertEquals("l.rossi@gmail.com", contact1.getEmails()[0]);
        assertEquals("rossil@outlook.com", contact1.getEmails()[1]);
        assertEquals("lucarossi@alice.it", contact1.getEmails()[2]);
        byte[] expectedProfilePicture1 = Base64.getDecoder().decode("SGVsbG8gd29ybGQ=");
        assertArrayEquals(expectedProfilePicture1, contact1.getProfilePicture());
        assertEquals("Mario", contact2.getName());
        assertEquals("Grigi", contact2.getSurname());
        assertEquals("2233445566", contact2.getNumbers()[0]);
        assertEquals("6655443322", contact2.getNumbers()[1]);
        assertEquals("7788990011", contact2.getNumbers()[2]);
        assertEquals("m.grigi@gmail.com", contact2.getEmails()[0]);
        assertEquals("grigimar@outlook.com", contact2.getEmails()[1]);
        byte[] expectedProfilePicture2 = Base64.getDecoder().decode("SGVsbG8gd29ybGQ=");
        assertArrayEquals(expectedProfilePicture2, contact2.getProfilePicture());
}

    ///UTC 3.7
    @Test
    public void testParseVCard_EmptyFile() throws IOException {
        File tempFile = File.createTempFile("test", ".vcf");
        tempFile.deleteOnExit();
        Collection<Contact> contacts = Converter.parseVCard(tempFile);
        assertTrue(contacts.isEmpty());

    }

    ///UTC 3.8
    @Test
    public void testParseVCard_InvalidVCard() throws IOException {
        File tempFile = File.createTempFile("test", ".vcf");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("INVALID:VCARD\n");
        }
        tempFile.deleteOnExit();
        Collection<Contact> contacts = Converter.parseVCard(tempFile);
        assertTrue(contacts.isEmpty());

    }
    
    ///UTC 3.9
    @Test
    public void testOnExportCSV() throws IOException {
        File tempFile = File.createTempFile("contacts", ".csv");
        tempFile.deleteOnExit();
        List<Contact> contacts = new ArrayList<>();
        contacts = this.createContacts(); 
        Converter.onExportCSV(contacts, tempFile);
        List<String> lines = Files.readAllLines(tempFile.toPath());
        
        assertTrue(lines.get(0).contains("Name,Surname,TEL1,TEL2,TEL3,EMAIL1,EMAIL2,EMAIL3,PHOTO,"));
        assertTrue(lines.get(1).contains("Luca,Rossi,1234567890,0987654321,1122334455,l.rossi@gmail.com,rossil@outlook.com,lucarossi@alice.it," + Base64.getEncoder().encodeToString("profilePicture1".getBytes()) + ","));
        assertTrue(lines.get(2).contains("Mario,Grigi,1029384756,6655443322,7788990011,m.grigi@gmail.com,grigimar@outlook.com,," + Base64.getEncoder().encodeToString("profilePicture2".getBytes()) + ","));
    }
    
    ///UTC 3.10
    @Test
    public void testOnExportVCard() throws IOException {
        File tempFile = Files.createTempFile("contacts", ".vcf").toFile();
        tempFile.deleteOnExit();

    // Create a list of contacts
        List<Contact> contacts = this.createContacts();

    // Export the contacts to the VCard file
        Converter.onExportVCard(contacts, tempFile);
        
        try(Scanner s = new Scanner(new BufferedReader(new FileReader(tempFile)))){
            assertNotNull(tempFile);
            while(s.hasNext()){
                assertEquals("BEGIN:VCARD", s.next());
                
            }
        }

        // Ensure both contacts were found and verified
        assertTrue(foundLucaRossi);
        assertTrue(foundMarioGrigi);

        scanner.close();
    }

    
    private void writeCsv(File file) throws IOException{
        csvFile = File.createTempFile("contacts", ".csv");
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("Name,Surname,TEL1,TEL2,TEL3,EMAIL1,EMAIL2,EMAIL3,PHOTO\n");
            writer.write("Luca,Rossi,1234567890,0987654321,1122334455,l.rossi@gmail.com,rossil@outlook.com,lucarossi@alice.it,SGVsbG8gd29ybGQ=\n");
            writer.write("Mario,Grigi,2233445566,6655443322,7788990011,m.grigi@gmail.com,grigimar@outlook.com,,SGVsbG8gd29ybGQ=\n");
        }  
    }
    
    private void writeVcf(File file) throws IOException{
        vcfFile = File.createTempFile("contacts", ".vcf");
        try (FileWriter writer = new FileWriter(vcfFile)) {
            writer.write("BEGIN:VCARD\n");
            writer.write("VERSION:3.0\n");
            writer.write("FN:Luca Rossi\n");
            writer.write("N:Rossi;Luca;\n");
            writer.write("TEL;TYPE=CELL:1234567890\n");
            writer.write("TEL;TYPE=CELL:0987654321\n");
            writer.write("TEL;TYPE=CELL:1122334455\n");
            writer.write("EMAIL;TYPE=HOME:l.rossi@gmail.com\n");
            writer.write("EMAIL;TYPE=HOME:rossil@outlook.com\n");
            writer.write("EMAIL;TYPE=HOME:lucarossi@alice.it\n");
            writer.write("PHOTO:SGVsbG8gd29ybGQ=\n");
            writer.write("END:VCARD\n");
            writer.write("BEGIN:VCARD\n");
            writer.write("VERSION:3.0\n");
            writer.write("FN:Mario Grigi\n");
            writer.write("N:Grigi;Mario;\n");
            writer.write("TEL;TYPE=CELL:2233445566\n");
            writer.write("TEL;TYPE=CELL:6655443322\n");
            writer.write("TEL;TYPE=CELL:7788990011\n");
            writer.write("EMAIL;TYPE=HOME:m.grigi@gmail.com\n");
            writer.write("EMAIL;TYPE=HOME:grigimar@outlook.com\n");
            writer.write("PHOTO:SGVsbG8gd29ybGQ=\n");
            writer.write("END:VCARD\n");
        }  
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
 */