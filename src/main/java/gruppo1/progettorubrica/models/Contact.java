package gruppo1.progettorubrica.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @brief Modella un'entità contatto.
 * 
 * 1 o più istanze di Contact possono far parte della classe AddressBook.
 * @see AddressBook
 */
public class Contact implements Serializable{
    private String id; ///< l'identificativo univoco dell'entità contatto
    private String name; ///< il nome in formato stringa dell'entità contatto
    private String surname; ///< il cognome in formato stringa dell'entità contatto
    private String[] numbers; ///< gli eventuali numeri di telefono in formato stringa dell'entità contatto
    private String[] emails; ///< le eventuali emails in formato stringa dell'entità contatto
    private Byte[] profilePicture; ///< l'immagine in formato vettore di byte associata all'entità contatto
    private Set<Integer> tagIndexes; ///< gli eventuali tag, contenuti nella collezione, in formato stringa associati all'entità contatto

    /**
     * @brief Costruttore per creare un'istanza Contact con solo nome e cognome valorizzati. 
     * Gli altri attributi non vengono valorizzati
     * @pre Fornire 2 stringhe corrispondenti al nome e cognome.
     * @post Creazione di un contatto.
     * @param[in] name
     * @param[in] surname
     */
    public Contact(String name, String surname) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.surname = surname;
        this.numbers = new String[3];
        this.emails = new String[3];
        this.profilePicture = null;
        this.tagIndexes = new HashSet<>();
    }

    /**
     * @pre Avere un'istanza della classe Contact
     * @post Ottenere l'identificativo associato all'istanza di Contact
     * @return l'identificativo dell'istanza Contact
     */
    public String getId() {
        return id;
    }
    
    /**
     * @pre Avere un'istanza della classe Contact
     * @post Impostare l'identificativo dell'istanza di Contact
     * @param[in] id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @pre Avere un'istanza della classe Contact
     * @post Ottenere il nome associato all'istanza di Contact
     * @return il nome dell'istanza Contact
     */
    public String getName() {
        return name;
    }

    /**
     * @brief Imposta il parametro in ingresso come il nome dell'istanza Contact
     * @pre Avere un'istanza della classe Contact. Fornire una stringa in ingresso
     * @post Valorizzare il campo name dell'istanza di Contact.
     * @param[in] name 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @pre Avere un'istanza della classe Contact
     * @post Ottenere il cognome associato all'istanza di Contact
     * @return il cognome dell'istanza Contact
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @brief Imposta il parametro in ingresso come il cognome dell'istanza Contact
     * @pre Avere un'istanza della classe Contact. Fornire una stringa in ingresso
     * @post Valorizzare il campo surname dell'istanza di Contact.
     * @param[in] surname 
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @pre Avere un'istanza della classe Contact
     * @post Ottenere l'array di numeri di telefono associato all'istanza di Contact
     * @return la stringa di numeri di telefono dell'istanza Contact
     */
    public String[] getNumbers() {
        return numbers;
    }

    /**
     * @brief Imposta il vettore in ingresso come numeri di telefono dell'istanza Contact
     * @pre Avere un'istanza della classe Contact. Fornire un array di stringhe in ingresso
     * @post Valorizzare il campo numbers dell'istanza di Contact.
     * @param[in] numbers 
     */
    public void setNumbers(String[] numbers) {
        this.numbers = numbers;
    }

    /**
     * @pre Avere un'istanza della classe Contact
     * @post Ottenere l'array di emails associato all'istanza di Contact 
     * @return Le stringhe di emails dell'istanza Contact
     */
    public String[] getEmails() {
        return emails;
    }

    /**
     * @brief Imposta il vettore in ingresso come le emails dell'istanza Contact
     * @pre Avere un'istanza della classe Contact. Fornire un array di stringhe in ingresso
     * @post Valorizzare il campo emails dell'istanza di Contact.
     * @param[in] emails 
     */
    public void setEmails(String[] emails) {
        this.emails = emails;
    }

    /**
     * @pre Avere un'istanza della classe Contact
     * @post Ottenere l'array di byte che rappresenta l'immagine profilo dell'istanza di Contact
     * @return Il vettore di byte che rappresentano l'immagine dell'istanza Contact
     */
    public Byte[] getProfilePicture() {
        return profilePicture;
    }

    /**
     * @brief imposta il vettore di byte come l'immagine dell'istanza del contatto
     * @pre Avere un'istanza della classe Contact. Fornire un array di byte in ingresso
     * @post Imposta il vettore in ingresso come profilePicture dell'istanza di Contact 
     * @param[in] profilePicture 
     */
    public void setProfilePicture(Byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * @pre Avere un'istanza della classe Contact
     * @post Ottenere la collezione di indici assegnati ai tag dell'istanza di Contact
     * @return La collezione di indici dei tag associati al contatto 
     */
    public Set<Integer> getAllTagIndexes() {
        return tagIndexes;
    }

    /**
     * @brief rimuove l'indice del tag passato come parametro
     * @pre Avere un'istanza di Contact e aver inserito almeno 1 tag. Passare in ingresso l'indice del tag da rimuovere.
     * @post Aver rimosso il tag se presente
     * @param[in] Indice del tag da rimuovere
     * @return true se il tag è presente e quindi viene rimosso, altrimenti false
     */
    public boolean removeTagIndex(Integer tagIndex) {
        return tagIndexes.remove(tagIndex);
    }

    /**
     * @brief Aggiunge l'indice del tag passato come parametro
     * @pre Avere un'istanza di Contact. Passare in ingresso un intero.
     * @post Aggiungere l'intero nella collezione tagIndexes
     * @param[in] Indice del tag da aggiungere
     */
    public void addTagIndex(Integer tagIndex) {
        tagIndexes.add(tagIndex);
    }
    
    /**
     * @brief controlla se l'istanza Contact su cui si chiama tale metodo è uguale
     * al parametro in ingresso o, secondo i criteri di uguaglianza scelti
     * @pre Avere un'istanza di Contact. Passare in ingresso un oggetto di Object.
     * @post Controllare se l'istanza corrente di Contact è uguale all'oggetto passato in ingresso.
     * @param[in] o
     * @return true se l'istanza Contact è uguale ad o,
     *         false se l'istanza Contact è diversa da o
     */
    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;

        Contact contact = (Contact) o;

        return this.id.equals(contact.id);
    }
    
    @Override
    public String toString(){
        StringBuffer strb=new StringBuffer();
        strb.append("ID: ").append(this.id).append('\n');
        strb.append("Name: ").append(this.name).append('\n');
        strb.append("Surname: ").append(this.surname).append('\n');
        
        strb.append("Emails: ");
        for(String e: this.emails){
            if(e!=null)
               strb.append(e).append(" ");
        }
        strb.append('\n');
        
        strb.append("Numbers: ");
        for(String n: this.numbers){
            if(n!=null)
               strb.append(n).append(" ");
        }
        strb.append('\n');
        
        if(profilePicture != null)
            strb.append("Image: Available (dim: ").append(profilePicture.length).append("B)\n");
        else
            strb.append("Image: Not Available\n");
        
        strb.append("TagIndexes: ");
        for(Integer i: this.tagIndexes){
            if(i!=null)
                strb.append(i).append(" ");
        }
        strb.append('\n');
        
        return strb.toString();
    }
}
