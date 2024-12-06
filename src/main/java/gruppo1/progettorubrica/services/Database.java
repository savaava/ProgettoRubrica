package gruppo1.progettorubrica.services;

import gruppo1.progettorubrica.models.Contact;
import gruppo1.progettorubrica.models.Tag;

import java.util.Collection;

/**
 * @brief Classe Database
 * 
 * Questa classe si occupa di gestire il database utilizzando il package di MongoDB.
 */
public class Database {
    /**
     * @brief Costruttore della classe Database
     * @param[in] uri URI del database
     */
    public Database(String uri) {

    }

    /**
     * @brief Aggiunge un contatto al database
     * @param[in] contact Contatto da aggiungere
     */
    public void addContact(Contact contact) {

    }

    /**
     * @brief Aggiorna un contatto nel database
     * @param[in] contact Contatto da aggiornare
     */
    public void updateContact(Contact contact) {

    }

    /**
     * @brief Rimuove un contatto dal database
     * @param[in] contact Contatto da rimuovere
     */
    public void removeContact(Contact contact) {

    }

    /**
     * @brief Restituisce tutti i contatti presenti nel database
     * @return Collezione di Contact con i contatti del database
     */
    public Collection<Contact> getContacts() {
        return null;
    }

    /**
     * @brief Restituisce tutti i tag presenti nel database
     * @return Collezione di String con i tag del database
     */
    public Collection<Tag> getTags() {
        return null;
    }

    /**
     * @brief Inserisce una collezione di contatti nel database
     * @param[in] contacts Collezione di contatti da inserire
     */
    public void insertManyContacts(Collection<Contact> contacts) {

    }

    /**
     * @brief Inserisce una collezione di tag nel database
     * @param[in] tags Collezione di tag da inserire
     */
    public void insertManyTags(Collection<Tag> tags) {

    }
}
