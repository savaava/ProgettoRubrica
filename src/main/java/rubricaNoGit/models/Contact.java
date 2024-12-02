package rubricaNoGit.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Contact implements Serializable {
    private String name;
    private String surname;
    private String[] numbers;
    private String[] emails;
    private byte[] profilePicture;
    private Set<String> tags;

    public Contact(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.numbers = new String[3];
        this.emails = new String[3];
        this.profilePicture = null;
        this.tags = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String[] getNumbers() {
        return numbers;
    }

    public void setNumbers(String[] numbers) {
        this.numbers = numbers;
    }

    public String[] getEmails() {
        return emails;
    }

    public void setEmails(String[] emails) {
        this.emails = emails;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        //CONTINUARE

        return false;
    }
}
