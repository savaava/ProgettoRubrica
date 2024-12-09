package gruppo1.progettorubrica.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void constructor1() {
        Tag tag = new Tag("tag");
        assertNotNull(tag);
        assertTrue(tag.getId() > 0);
    }

    @Test
    public void constructor2() {
        Tag tag = new Tag("tag", 10);
        assertNotNull(tag);
        assertEquals(10, tag.getId());
    }

    @Test
    public void getId() {
        Tag tag = new Tag("tag", 10);
        assertEquals(10, tag.getId());
    }

    @Test
    public void getDescription() {
        Tag tag = new Tag("tag");
        assertEquals("tag", tag.getDescription());
    }

    @Test
    public void setDescription() {
        Tag tag = new Tag("tag");
        tag.setDescription("new tag");
        assertEquals("new tag", tag.getDescription());
    }

    @Test
    public void setIndex() {
        Tag.setIndex(11);
        Tag tag = new Tag("tag");
        assertEquals(11, tag.getId());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("tag");
        Tag tag2 = new Tag("tag");
        assertEquals(tag1, tag2);

        Tag tag3 = new Tag("tag", 10);
        Tag tag4 = new Tag("taga", 10);
        assertEquals(tag3, tag4);
    }
}