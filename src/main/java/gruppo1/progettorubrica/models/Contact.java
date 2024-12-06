package gruppo1.progettorubrica.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @brief La classe Contact modella un'entità contatto.
 * 1 o più istanze di Contact possono far parte della classe AddressBook: 
 * @see AddressBook
 */
public class Contact implements Serializable {
    private String name; ///< il nome in formato stringa dell'entità contatto
    private String surname; ///< il cognome in formato stringa dell'entità contatto
    private String[] numbers; ///< gli eventuali numeri di telefono in formato stringa dell'entità contatto
    private String[] emails; ///< le eventuali emails in formato stringa dell'entità contatto
    private byte[] profilePicture; ///< l'immagine in formato vettore di byte associata all'entità contatto
    private Set<String> tags; ///< gli eventuali, contenuti nella collezione, in formato stringa associati all'entità contatto

    /**
     * @brief Costruttore per creare un'istanza Contact con solo nome e cognome valorizzati. 
     * Gli altri attributi non vengono valorizzati
     * @param[in] name
     * @param[in] surname
     */
    public Contact(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.numbers = new String[3];
        this.emails = new String[3];
        this.profilePicture = null;
        this.tags = new HashSet<>();
    }

    /**
     * 
     * @return il nome dell'istanza Contact
     */
    public String getName() {
        return name;
    }

    /**
     * @brief imposta il parametro in ingresso come il nome dell'istanza Contact
     * @param[in] name 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return il cognome dell'istanza Contact
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @brief imposta il parametro in ingresso come il cognome dell'istanza Contact
     * @param[in] surname 
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * 
     * @return la stringa di numeri di telefono dell'istanza Contact
     */
    public String[] getNumbers() {
        return numbers;
    }

    /**
     * @brief imposta il vettore in ingresso come i numeri di telefono dell'istanza Contact
     * @param[in] numbers 
     */
    public void setNumbers(String[] numbers) {
        this.numbers = numbers;
    }

    /**
     * 
     * @return le stringhe di emails dell'istanza Contact
     */
    public String[] getEmails() {
        return emails;
    }

    /**
     * @brief imposta il vettore in ingresso come le emails dell'istanza Contact
     * @param[in] emails 
     */
    public void setEmails(String[] emails) {
        this.emails = emails;
    }

    /**
     * 
     * @return il vettore di byte che rappresentano l'immagine dell'istanza Contact
     */
    public byte[] getProfilePicture() {
        return profilePicture;
    }

    /**
     * @brief imposta il vettore di byte come l'immagine dell'istanza del contatto
     * @param[in] profilePicture 
     */
    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * 
     * @return la collezione di tag associati al contatto 
     */
    public Set<String> getTags() {
        return tags;
    }

    /**
     * @brief imposta la collezione di tag in ingresso come l'insieme di tag dell'istanza Contact
     * @param[in] tags 
     */
    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
    
    /**
     * @brief controlla se l'istanza Contact su cui si chiama tale metodo è uguale
     * al parametro in ingresso o, secondo i criteri di uguaglianza scelti
     * @param[in] o
     * @return true se l'istanza Contact è uguale ad o,
     *         false se l'istanza Contact è diversa da o
     */
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
