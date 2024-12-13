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
        assertNull(c.getProfilePicture());
        
    }

    /**
     * UTC 2.11
     */
    @Test
    public void testSetProfilePicture() {
        System.out.println("setProfilePicture");
        Byte profilePicture[]={3,4,5};
        c.setProfilePicture(profilePicture);
        Byte profilePicture1[]=c.getProfilePicture();
        assertEquals(profilePicture, profilePicture1);
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
     * Confronto Contact c con un altro contatto c1 i quali contengono gli stessi valori e inolte i contenuti degli array sono gli stessi ma in ordine diverso.
     */
    @Test
    public void testEquals1() {
        System.out.println("equals1");
        String s="paperino";
        
        Contact c1=new Contact("pippo", "paperino");
        String numbers1[]=new String[3];
        numbers1[0]="3926533458";
        numbers1[1]="3482356875";
        c1.setNumbers(numbers1);
        String numbers2[]=new String[3];
        numbers2[0]="3482356875";
        numbers2[1]="3926533458";
        c.setNumbers(numbers2);
        
        String emails1[]=new String[3];
        emails1[0]="pluto@gmail.com";
        emails1[1]="paperino@gmail.com";
        emails1[2]="pippo@gmail.com";
        c.setEmails(emails1);
        String emails2[]=new String[3];
        emails2[0]="pippo@gmail.com";
        emails2[1]="paperino@gmail.com";
        emails2[2]="pluto@gmail.com";
        c1.setEmails(emails2);
        
        c.addTagIndex(2);
        c1.addTagIndex(2);
        
        Byte image1[]={1,2,3};
        Byte image2[]={1,2,3};
        c.setProfilePicture(image1);
        c1.setProfilePicture(image2);
        
        assertFalse(c.equals(null)); //confronto Contact c con un riferimento null
        assertTrue(c.equals(c)); //confronto Contact c con se stesso
        assertFalse(c.equals(s)); //confronto Contact c con un'istanza di una classe diversa da Contact
        assertTrue(c.equals(c1)); //confronto Contact c e c1 i quali contengono stessi valori
    }
    
    /**
     * UTC 2.17
     * Confronto c con un altro contatto c1 i quali contengono nome e cognome diversi.
     */
    @Test
    public void testEquals2() {
        System.out.println("equals2");
        
        Contact c1=new Contact("mario", "rossi");
        
        assertFalse(c.equals(c1));
    }
    
    /**
     * UTC 2.17
     * Confronto c con un altro contatto c1 i quali contengono stesso nome e cognome ma numeri di cellulare diversi.
     */
    @Test
    public void testEquals3() {
        System.out.println("equals3");
        
        Contact c1=new Contact("pippo", "paperino");
        String numbers1[]=new String[3];
        numbers1[0]="3926533458";
        numbers1[1]="3482356875";
        numbers1[2]="3872476909";
        c1.setNumbers(numbers1);
        String numbers2[]=new String[3];
        numbers2[0]="3984562456";
        numbers2[1]="3545476789";
        numbers2[2]="3435679832";
        c.setNumbers(numbers2);
        
        assertFalse(c.equals(c1));
    }
    
    
    
    
    
}
