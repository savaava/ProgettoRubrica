
package gruppo1.progettorubrica.services;

import gruppo1.progettorubrica.models.Contact;
import gruppo1.progettorubrica.services.Converter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
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
        assertArrayEquals(new String[]{"l.rossi@gmail.com ","rossil@outlook.com","lucarossi@alice.it"}, contact1.getEmails());
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
}