package gruppo1.progettorubrica.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    ///UTC 5.1
    @Test
    public void testConstructor1() {
        Tag tag = new Tag("tag");
        assertNotNull(tag);
        assertTrue(tag.getId() > 0);
    }

    ///UTC 5.2
    @Test
    public void testConstructor2() {
        Tag tag = new Tag("tag", 10);
        assertNotNull(tag);
        assertEquals(10, tag.getId());
    }

    ///UTC 5.3
    @Test
    public void testGetId() {
        Tag tag = new Tag("tag", 10);
        assertEquals(10, tag.getId());
    }

    ///UTC 5.4
    @Test
    public void testGetDescription() {
        Tag tag = new Tag("tag");
        assertEquals("tag", tag.getDescription());
    }

    ///UTC 5.5
    @Test
    public void testSetDescription() {
        Tag tag = new Tag("tag");
        tag.setDescription("new tag");
        assertEquals("new tag", tag.getDescription());
    }

    ///UTC 5.6
    @Test
    public void testSetIndex() {
        Tag.setIndex(11);
        Tag tag = new Tag("tag");
        assertEquals(11, tag.getId());
    }

    ///UTC 5.7
    @Test
    public void testEquals() {
        Tag tag1 = new Tag("tag");
        Tag tag2 = new Tag("tag");
        Tag tag3 = new Tag("tag", 10);
        Tag tag4 = new Tag("taga", 10);
        assertEquals(tag1, tag2);
        assertEquals(tag3, tag4);
    }
}