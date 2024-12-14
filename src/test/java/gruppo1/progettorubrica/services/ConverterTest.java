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
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import org.junit.*;
import static org.junit.Assert.*;
import java.nio.charset.StandardCharsets;


public class ConverterTest {

    private File csvFile;

    private File vcfFile;

    @Before
    public void setUp() throws IOException {

    }

    ///UTC 3.1
    @Test
    public void testParseCSV1() throws IOException {
        this.writeCsv();
        Collection<Contact> contacts = Converter.parseCSV(csvFile);
        assertNotNull(contacts);
        assertEquals(2, contacts.size());
    }

    ///UTC 3.2
    @Test
    public void testParseCSV2() throws IOException {
        this.writeCsv();
        Collection<Contact> contacts = Converter.parseCSV(csvFile);
        Contact[] contactArray = contacts.toArray(new Contact[0]);

        Contact contact1 = contactArray[0];
        Contact contact2 = contactArray[1];
        assertEquals("Luca", contact1.getName());
        assertEquals("Rossi", contact1.getSurname());
        assertArrayEquals(new String[]{"1234567890", "0987654321", "1122334455"}, contact1.getNumbers());
        assertArrayEquals(new String[]{"l.rossi@gmail.com","rossil@outlook.com","lucarossi@alice.it"}, contact1.getEmails());
        assertArrayEquals(Converter.stringToByteArray("SGVsbG8gd29ybGQ="), contact1.getProfilePicture());
        assertEquals("Mario", contact2.getName());
        assertEquals("Grigi", contact2.getSurname());
        assertArrayEquals(new String[]{"2233445566", "6655443322", "7788990011"}, contact2.getNumbers());
        assertArrayEquals(new String[]{"m.grigi@gmail.com","grigimar@outlook.com",""}, contact2.getEmails());
        assertArrayEquals(Converter.stringToByteArray("SGVsbG8gd29ybGQ="), contact2.getProfilePicture());
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
        this.writeVcf();
        Collection<Contact> contacts = Converter.parseVCard(vcfFile);
        assertEquals(2, contacts.size());
    }

    ///UTC 3.6
    @Test
    public void testParseVCard_ValidVCard2() throws IOException {
        this.writeVcf();
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
        Byte[] expectedProfilePicture1 = Converter.stringToByteArray("SGVsbG8gd29ybGQ=");
        assertArrayEquals(expectedProfilePicture1, contact1.getProfilePicture());
        assertEquals("Mario", contact2.getName());
        assertEquals("Grigi", contact2.getSurname());
        assertEquals("2233445566", contact2.getNumbers()[0]);
        assertEquals("6655443322", contact2.getNumbers()[1]);
        assertEquals("7788990011", contact2.getNumbers()[2]);
        assertEquals("m.grigi@gmail.com", contact2.getEmails()[0]);
        assertEquals("grigimar@outlook.com", contact2.getEmails()[1]);
        Byte[] expectedProfilePicture2 = Converter.stringToByteArray("SGVsbG8gd29ybGQ=");
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
    public void testOnExportCSV1() throws IOException {
        File tempFile = File.createTempFile("contacts", ".csv");
        tempFile.deleteOnExit();
        List<Contact> contacts = new ArrayList<>();
        contacts = this.createContacts();
        Converter.onExportCSV(contacts, tempFile);
        assertNotNull(tempFile);
    }

    ///UTC 3.10
    @Test
    public void testOnExportCSV2() throws IOException {
        File tempFile = File.createTempFile("contacts", ".csv");
        tempFile.deleteOnExit();
        List<Contact> contacts = this.createContacts();
        Converter.onExportCSV(contacts, tempFile);
        List<String> lines = Files.readAllLines(tempFile.toPath());
        assertTrue(lines.get(0).contains("First Name,Middle Name,Last Name,Phonetic First Name,Phonetic Middle Name,Phonetic Last Name,Name Prefix,Name Suffix,Nickname,File As,Organization Name,Organization Title,Organization Department,Birthday,Notes,Photo,Labels,E-mail 1 - Label,E-mail 1 - Value,E-mail 2 - Label,E-mail 2 - Value,E-mail 3 - Label,E-mail 3 - Value,Phone 1 - Label,Phone 1 - Value,Phone 2 - Label,Phone 2 - Value,Phone 3 - Label,Phone 3 - Value,Custom Field 1 - Label,Custom Field 1 - Value"));
        assertTrue(lines.get(1).contains("Luca,,Rossi,,,,,,,,,,,,,profilePicture1,,,l.rossi@gmail.com,,rossil@outlook.com,,lucarossi@alice.it,,1234567890,,0987654321,,1122334455,,,"));
        assertTrue(lines.get(2).contains("Mario,,Grigi,,,,,,,,,,,,,profilePicture2,,,m.grigi@gmail.com,,grigimar@outlook.com,,,,2233445566,,6655443322,,7788990011,,,"));
    }
    ///UTC 3.11
    @Test
    public void testOnExportVCard1() throws IOException {
        File tempFile = Files.createTempFile("contacts", ".vcf").toFile();
        tempFile.deleteOnExit();

        // Create a list of contacts
        List<Contact> contacts = this.createContacts();

        // Export the contacts to the VCard file
        Converter.onExportVCard(contacts, tempFile);

        assertNotNull(tempFile);
    }

    ///UTC 3.12
    @Test
    public void testOnExportVCard2() throws IOException {
        File tempFile = Files.createTempFile("contacts", ".vcf").toFile();
        tempFile.deleteOnExit();
        List<Contact> contacts = this.createContacts();
        Converter.onExportVCard(contacts, tempFile);
        try(Scanner s = new Scanner(new BufferedReader(new FileReader(tempFile)))){
            s.useDelimiter("[\n]");

            while(s.hasNext()){   //uso replaceAll() per eliminare gli spazi superflui
                assertEquals("BEGIN:VCARD", s.next().replaceAll("\\s+", ""));  //Primo contatto
                assertEquals("VERSION:3.0", s.next().replaceAll("\\s+", ""));
                assertEquals("FN:LucaRossi", s.next().replaceAll("\\s+", ""));
                assertEquals("N:Rossi;Luca;", s.next().replaceAll("\\s+", ""));
                assertEquals("TEL;TYPE:1234567890", s.next().replaceAll("\\s+", ""));
                assertEquals("TEL;TYPE:0987654321", s.next().replaceAll("\\s+", ""));
                assertEquals("TEL;TYPE:1122334455", s.next().replaceAll("\\s+", ""));
                assertEquals("EMAIL;TYPE:l.rossi@gmail.com", s.next().replaceAll("\\s+", ""));
                assertEquals("EMAIL;TYPE:rossil@outlook.com", s.next().replaceAll("\\s+", ""));
                assertEquals("EMAIL;TYPE:lucarossi@alice.it", s.next().replaceAll("\\s+", ""));
                assertEquals(("PHOTO:" + "profilePicture1"), s.next().replaceAll("\\s+", ""));
                assertEquals("END:VCARD", s.next().replaceAll("\\s+", ""));
                assertEquals("BEGIN:VCARD", s.next().replaceAll("\\s+", ""));  //Secondo contatto
                assertEquals("VERSION:3.0", s.next().replaceAll("\\s+", ""));
                assertEquals("FN:MarioGrigi", s.next().replaceAll("\\s+", ""));
                assertEquals("N:Grigi;Mario;", s.next().replaceAll("\\s+", ""));
                assertEquals("TEL;TYPE:2233445566", s.next().replaceAll("\\s+", ""));
                assertEquals("TEL;TYPE:6655443322", s.next().replaceAll("\\s+", ""));
                assertEquals("TEL;TYPE:7788990011", s.next().replaceAll("\\s+", ""));
                assertEquals("EMAIL;TYPE:m.grigi@gmail.com", s.next().replaceAll("\\s+", ""));
                assertEquals("EMAIL;TYPE:grigimar@outlook.com", s.next().replaceAll("\\s+", ""));
                assertEquals(("PHOTO:profilePicture2"), s.next().replaceAll("\\s+", ""));
                assertEquals("END:VCARD", s.next().replaceAll("\\s+", "").replaceAll("\\s+", ""));
            }
        }
    }


    private void writeCsv() throws IOException{
        csvFile = File.createTempFile("contacts", ".csv");
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("First Name,1Middle Name,2Last Name,3Phonetic First Name,4Phonetic Middle Name,5Phonetic Last Name,6Name Prefix,7Name Suffix,8Nickname,9File As,10Organization Name,11Organization Title,12Organization Department,13Birthday,14Notes,15Photo,16Labels,17E-mail 1 - Label,18E-mail 1 - Value,19E-mail 2 - Label,20E-mail 2 - Value,21E-mail 3 - Label,22E-mail 3 - Value,23Phone 1 - Label,24Phone 1 - Value,25Phone 2 - Label,26Phone 2 - Value,27Phone 3 - Label,28Phone 3 - Value,29Custom Field 1 - Label,30Custom Field 1 - Value\n");
            writer.write("Luca,,Rossi,,,,,,,,,,,,,SGVsbG8gd29ybGQ=,,,l.rossi@gmail.com,,rossil@outlook.com,,lucarossi@alice.it,,1234567890,,0987654321,,1122334455,,,\n");
            writer.write("Mario,,Grigi,,,,,,,,,,,,,SGVsbG8gd29ybGQ=,,,m.grigi@gmail.com,,grigimar@outlook.com,,,,2233445566,,6655443322,,7788990011,,,\n");
        }
    }

    private void writeVcf() throws IOException{
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
        contact1.setProfilePicture(Converter.stringToByteArray("profilePicture1"));
        contacts.add(contact1);

        Contact contact2 = new Contact("Mario", "Grigi");
        contact2.setNumbers(new String[]{"2233445566", "6655443322", "7788990011"});
        contact2.setEmails(new String[]{"m.grigi@gmail.com","grigimar@outlook.com",""});
        contact2.setProfilePicture(Converter.stringToByteArray("profilePicture2"));
        contacts.add(contact2);

        return contacts;
    }
}