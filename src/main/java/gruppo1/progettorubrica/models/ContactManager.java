package gruppo1.progettorubrica.models;

import java.io.IOException;
import java.util.Collection;
import javafx.collections.ObservableList;

/**
 * @brief Interfaccia per la gestione dei contatti.
 */
public interface ContactManager {

    /**
     * @brief Ottiene la lista osservabile di tutti i contatti.
     * @pre nessuna (ci possono essere 0 o più contatti).
     * @post Il client ottiene la lista completa degli utenti dell'AddressBook
     * @return Lista osservabile dei contatti.
     */
    ObservableList<Contact> getAllContacts();

    /**
     * @brief Aggiunge un contatto alla rubrica telefonica.
     * @param[in] c Contatto da aggiungere.
     * @pre nessuna (il parametro c può essere null).
     * @post Aggiorna la lista dei contatti dell'AddressBook inserendovi l'eventuale nuovo contatto c
     */
    void addContact(Contact c) throws IOException;
    
    /**
     * @brief Aggiunge 1 o più contatti dalla collezione alla rubrica telefonica.
     * @param[in] c Collezione di contatti da aggiungere.
     * @pre nessuna (il parametro c può essere null).
     * @post Aggiorna la lista dei contatti dell'AddressBook inserendovi gli eventuali contatti da c.
     */
    void addManyContacts(Collection<Contact> c) throws IOException;

    /**
     * @brief Rimuove la prima occorrenza del contatto.
     * @param[in] c Contatto da rimuovere.
     * @pre nessuna (il parametro c può essere null o non presente nella lista dei contatti).
     * @post Aggiorna la lista dei contatti dell'AddressBook rimuovendo il contatto c, se presente.
     */
    void removeContact(Contact c) throws IOException;
    
    /**
     * @brief Restituisce la sottocollezione dei contatti a cui è associato il tag in ingresso
     * @pre Il tag esiste, quindi è stato inserito dall'utente nell'elenco dei tag
     * @post Il client ottiene i contatti interessati.
     * @param[in] tag
     * @return la collezione dei contatti, oppure una collezione vuota se non vi sono contatti associati a tag.
     */
    Collection<Contact> getContactsFromTag(Tag tag);
}
