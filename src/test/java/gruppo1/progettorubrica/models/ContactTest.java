/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppo1.progettorubrica.models;

import java.awt.Image;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class ContactTest {
    private Contact c;
    
    
    @Before
    public void setUp() {
        c=new Contact("pippo", "paperino");
    }
    

    /**
     * UTC 2.1
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        
        assertEquals("pippo", c.getName());
    }

    /**
     * UTC 2.2
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        
        c.setName("pluto");
        assertEquals("pluto", c.getName());
    }

    /**
     * UTC 2.3
     */
    @Test
    public void testGetSurname() {
        System.out.println("getSurname");
        
        assertEquals("paperino", c.getSurname());
        
    }

    /**
     * UTC 2.4
     */
    @Test
    public void testSetSurname() {
        System.out.println("setSurname");
        
        c.setSurname("pluto");
        assertEquals("pluto", c.getSurname());
    }

    /**
     * UTC 2.5
     */
    @Test
    public void testGetNumbers1() {
        System.out.println("getNumbers");
        
        String numbers1[]=c.getNumbers();
        assertNull(numbers1[0]);
        assertNull(numbers1[1]);
        assertNull(numbers1[2]);
    }
    
    /**
     * UTC 2.6
     */
    @Test
    public void testGetNumbers2() {
        System.out.println("getNumbers");
        
        String numbers[]=new String[3];
        numbers[0]="3926533458";
        numbers[1]="3482356875";
        numbers[2]="3275647395";
        c.setNumbers(numbers);
        
        String numbers2[]=c.getNumbers();
        assertEquals(numbers, numbers2);
    }

    /**
     * UTC 2.7
     */
    @Test
    public void testSetNumbers() {
        System.out.println("setNumbers");
        
        String numbers[]=new String[3];
        numbers[0]="3924567896";
        numbers[1]="3601245678";
        numbers[2]="3334567899";
        
        c.setNumbers(numbers);
        
        String numbers2[]=c.getNumbers();
        assertEquals(numbers, numbers2);
    }

    /**
     * UTC 2.8
     */
    @Test
    public void testGetEmails() {
        System.out.println("getEmails");
        String emails[]=c.getEmails();
        assertNull(emails[0]);
        assertNull(emails[1]);
        assertNull(emails[2]);
    }

    /**
     * UTC 2.9
     */
    @Test
    public void testSetEmails() {
        System.out.println("setEmails");
        
        String emails[]=new String[3];
        emails[0]="pluto@gmail.com";
        emails[1]="paperino@gmail.com";
        emails[2]="pippo@gmail.com";
        
        c.setEmails(emails);
        
        String emails1[]=c.getEmails();
        assertEquals(emails, emails1);
    }

    /**
     * UTC 2.10
     */
    @Test
    public void testGetProfilePicture() {
        System.out.println("getProfilePicture");
        
    }

    /**
     * UTC 2.11
     */
    @Test
    public void testSetProfilePicture() {
        System.out.println("setProfilePicture");
    }

    /**
     * UTC 2.12
     */
    @Test
    public void testGetAllTagIndexes1() {
        System.out.println("getAllTagIndexes");
        
        Set<Integer> tags=c.getAllTagIndexes();
        assertTrue(tags.isEmpty());
    }
    
    /**
     * UTC 2.13
     */
    @Test
    public void testGetAllTagIndexes2() {
        System.out.println("getAllTagIndexes");
        
        c.addTagIndex(1);
        c.addTagIndex(2);
        Set<Integer> tags2=c.getAllTagIndexes();
        assertEquals(c.getAllTagIndexes(), tags2);
    }

    /**
     * UTC 2.14
     */
    @Test
    public void testRemoveTagIndex1() {
        System.out.println("removeTagIndex");
        
        c.addTagIndex(2);
        c.addTagIndex(4);
        Set<Integer> tags2=c.getAllTagIndexes();
        
        assertTrue(c.removeTagIndex(2));
        assertFalse(c.removeTagIndex(5)); //provo a rimuovere un tag non presente
        assertFalse(tags2.contains(2));
    }
    

    /**
     * UTC 2.15
     */
    @Test
    public void testAddTagIndex() {
        System.out.println("addTagIndex");
        
        c.addTagIndex(1);
        c.addTagIndex(2);
        c.addTagIndex(3);
        
        Set<Integer> tags=c.getAllTagIndexes();
        assertTrue(tags.contains(1));
        assertTrue(tags.contains(2));
        assertTrue(tags.contains(3));
        
    }

    /**
     * UTC 2.16
     */
    @Test
    public void testEquals() {
        
    }
    
    
    /*
    EQUALS
    GET e SET DI PROFILOPICTURE
    */
    
    
    
}
