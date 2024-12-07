package gruppo1.progettorubrica.services;

import com.mongodb.client.MongoDatabase;
import gruppo1.progettorubrica.models.Contact;
import gruppo1.progettorubrica.models.Tag;
import org.bson.Document;

import java.util.Collection;

/**
 * @brief Modella un database per il salvataggio dei dati.
 * 
 * Questa classe si occupa di gestire il database utilizzando il package di MongoDB.
 */
public class Database {
    private MongoDatabase mongoDb; ///< Oggetto database di tipo MongoDatabase

    /**
     * @brief Costruttore della classe Database
     * @param[in] uri URL del database
     */
    public Database(String url) {

    }

    /**
     * @brief Verifica la validità dell'URL del database
     * @param[in] url URL del database
     * @return true se l'URL è valido, false altrimenti
     */
    public static boolean verifyDBUrl(String url) {
        return false;
    }

    /**
     * @brief Aggiunge/aggiorna un contatto al database
     * @param[in] c Contatto da aggiungere
     */
    public void upsertContact(Contact c) {

    }

    /**
     * @brief Rimuove un contatto dal database
     * @param[in] c Contatto da rimuovere
     */
    public void removeContact(Contact c) {

    }

    /**
     * @brief Aggiunge/aggiorna un tag al database
     */
    public void upsertTag(Tag tag) {

    }

    /**
     * @brief Rimuove un tag dal database
     */
    public void removeTag(Tag tag) {

    }

    /**
     * @brief Restituisce tutti i contatti presenti nel database
     * @return Collezione di Contact con i contatti del database
     */
    public Collection<Contact> getAllContacts() {
        return null;
    }

    /**
     * @brief Restituisce tutti i tag presenti nel database
     * @return Collezione di String con i tag del database
     */
    public Collection<Tag> getAllTags() {
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

    //METODI DI CONVERSIONE

    /**
     * @brief Converte il contatto in un documento
     * @param[in] c Contatto da convertire
     * @return Document con i dati del contatto
     */
    public Document contactToDocument(Contact c) {
        return null;
    }

    /**
     * @brief Converte il documento in un contatto
     * @param[in] d Document da convertire
     * @return Contatto con i dati del documento
     */
    public Contact documentToContact(Document d) {
        return null;
    }

    /**
     * @brief Converte il tag in un documento
     * @param[in] tag Tag da convertire
     * @return Document con i dati del tag
     */
    public Document tagToDocument(Tag tag) {
        return null;
    }

    /**
     * @brief Converte il documento in un tag
     * @param[in] d Document da convertire
     * @return Tag con i dati del documento
     */
    public Tag documentToTag(Document d) {
        return null;
    }
}
