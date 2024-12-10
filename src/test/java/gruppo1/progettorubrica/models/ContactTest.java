/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppo1.progettorubrica.models;

import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author solon
 */
public class ContactTest {
    
    public ContactTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getName method, of class Contact.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Contact instance = null;
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setName method, of class Contact.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        String name = "";
        Contact instance = null;
        instance.setName(name);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSurname method, of class Contact.
     */
    @Test
    public void testGetSurname() {
        System.out.println("getSurname");
        Contact instance = null;
        String expResult = "";
        String result = instance.getSurname();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSurname method, of class Contact.
     */
    @Test
    public void testSetSurname() {
        System.out.println("setSurname");
        String surname = "";
        Contact instance = null;
        instance.setSurname(surname);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNumbers method, of class Contact.
     */
    @Test
    public void testGetNumbers() {
        System.out.println("getNumbers");
        Contact instance = null;
        String[] expResult = null;
        String[] result = instance.getNumbers();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setNumbers method, of class Contact.
     */
    @Test
    public void testSetNumbers() {
        System.out.println("setNumbers");
        String[] numbers = null;
        Contact instance = null;
        instance.setNumbers(numbers);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEmails method, of class Contact.
     */
    @Test
    public void testGetEmails() {
        System.out.println("getEmails");
        Contact instance = null;
        String[] expResult = null;
        String[] result = instance.getEmails();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEmails method, of class Contact.
     */
    @Test
    public void testSetEmails() {
        System.out.println("setEmails");
        String[] emails = null;
        Contact instance = null;
        instance.setEmails(emails);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProfilePicture method, of class Contact.
     */
    @Test
    public void testGetProfilePicture() {
        System.out.println("getProfilePicture");
        Contact instance = null;
        byte[] expResult = null;
        byte[] result = instance.getProfilePicture();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setProfilePicture method, of class Contact.
     */
    @Test
    public void testSetProfilePicture() {
        System.out.println("setProfilePicture");
        byte[] profilePicture = null;
        Contact instance = null;
        instance.setProfilePicture(profilePicture);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllTagIndexes method, of class Contact.
     */
    @Test
    public void testGetAllTagIndexes() {
        System.out.println("getAllTagIndexes");
        Contact instance = null;
        Set<Integer> expResult = null;
        Set<Integer> result = instance.getAllTagIndexes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeTagIndex method, of class Contact.
     */
    @Test
    public void testRemoveTagIndex() {
        System.out.println("removeTagIndex");
        Integer tagIndex = null;
        Contact instance = null;
        boolean expResult = false;
        boolean result = instance.removeTagIndex(tagIndex);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addTagIndex method, of class Contact.
     */
    @Test
    public void testAddTagIndex() {
        System.out.println("addTagIndex");
        Integer tagIndex = null;
        Contact instance = null;
        instance.addTagIndex(tagIndex);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class Contact.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object o = null;
        Contact instance = null;
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
