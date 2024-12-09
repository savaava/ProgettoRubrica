package gruppo1.progettorubrica.services;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import gruppo1.progettorubrica.models.Contact;
import gruppo1.progettorubrica.models.Tag;
import org.bson.Document;

import java.util.*;

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
        MongoClient mongoClient = MongoClients.create(url);
        this.mongoDb = mongoClient.getDatabase("addressBook");
    }

    /**
     * @brief Verifica la validità dell'URL del database
     * @param[in] url URL del database
     * @return true se l'URL è valido, false altrimenti
     */
    public static boolean verifyDBUrl(String url) {
        if(url == null || url.isEmpty()) return false;

        try(MongoClient mongoClient = MongoClients.create(url)) {
            MongoDatabase db = mongoClient.getDatabase("addressBook");
            MongoCollection<Document> contactsColl = db.getCollection("contacts");

            //Inserimento elemento di test
            contactsColl.insertOne(Document.parse("{\"test\":\"test\"}"));

            //Rimozione elemento di test
            contactsColl.deleteOne(Filters.eq("test", "test"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @brief Aggiunge/aggiorna un contatto al database
     * @param[in] c Contatto da aggiungere
     */
    public void insertContact(Contact c) {
        mongoDb.getCollection("contacts").insertOne(this.contactToDocument(c));
    }

    /**
     * @brief Rimuove un contatto dal database
     * @param[in] c Contatto da rimuovere
     */
    public void removeContact(Contact c) {
        mongoDb.getCollection("contacts").deleteOne(this.contactToDocument(c));
    }

    /**
     * @brief Aggiunge/aggiorna un tag al database
     */
    public void insertTag(Tag tag) {
        mongoDb.getCollection("tags").insertOne(this.tagToDocument(tag));
    }

    /**
     * @brief Rimuove un tag dal database
     */
    public void removeTag(Tag tag) {
        mongoDb.getCollection("tags").deleteOne(this.tagToDocument(tag));
    }

    /**
     * @brief Restituisce tutti i contatti presenti nel database
     * @return Collezione di Contact con i contatti del database
     */
    public Collection<Contact> getAllContacts() {
        FindIterable<Document> contactsDocument = mongoDb.getCollection("contacts").find();
        List<Contact> contacts = new ArrayList<>();

        if(contactsDocument.first() == null) return Collections.emptyList();

        //Iteriamo i documenti per aggiungerli alla lista una volta convertiti in contatti
        contactsDocument.forEach(doc -> {
            contacts.add(this.documentToContact(doc));
        });

        return contacts;
    }

    /**
     * @brief Restituisce tutti i tag presenti nel database
     * @return Collezione di String con i tag del database
     */
    public Collection<Tag> getAllTags() {
        FindIterable<Document> tagsDocument = mongoDb.getCollection("tags").find();
        List<Tag> tags = new ArrayList<>();

        if(tagsDocument.first() == null) return Collections.emptyList();

        //Iteriamo i documenti per aggiungerli alla lista una volta convertiti in tag
        tagsDocument.forEach(doc -> {
            tags.add(this.documentToTag(doc));
        });

        return tags;
    }

    /**
     * @brief Inserisce una collezione di contatti nel database
     * @param[in] contacts Collezione di contatti da inserire
     */
    public void insertManyContacts(Collection<Contact> contacts) {
        List<Document> documents = new ArrayList<>();

        contacts.forEach(c -> {
            documents.add(this.contactToDocument(c));
        });

        mongoDb.getCollection("contacts").insertMany(documents);
    }

    /**
     * @brief Inserisce una collezione di tag nel database
     * @param[in] tags Collezione di tag da inserire
     */
    public void insertManyTags(Collection<Tag> tags) {
        List<Document> documents = new ArrayList<>();

        tags.forEach(t -> {
            documents.add(this.tagToDocument(t));
        });

        mongoDb.getCollection("tags").insertMany(documents);
    }

    /**
     * @brief Rimuove tutti i contatti dal database
     * 
     * Questo metodo elimina tutti i contatti presenti nel database.
     * @pre Nessuna
     * @post Nessun contatto presente nel database
     */
    public void deleteAllContacts() {
        mongoDb.getCollection("contacts").drop();
    }

    /**
     * @brief Rimuove tutti i tag dal database
     * 
     * Questo metodo elimina tutti i tag presenti nel database.
     * @pre Nessuna
     * @post Nessun tag presente nel database
     */
    public void deleteAllTags() {
        mongoDb.getCollection("tags").drop();
    }

    //METODI DI CONVERSIONE

    /**
     * @brief Converte il contatto in un documento
     * @param[in] c Contatto da convertire
     * @return Document con i dati del contatto
     */
    public Document contactToDocument(Contact c) {
        Document doc = new Document();

        doc.put("name",c.getName());
        doc.put("surname", c.getSurname());
        doc.put("numbers", Arrays.asList(c.getNumbers()));
        doc.put("emails", Arrays.asList(c.getEmails()));
        if(c.getProfilePicture() != null) {
            doc.put("image", Base64.getEncoder().encodeToString(c.getProfilePicture()));
        } else {
            doc.put("image", "");
        }
        doc.put("tagIndexes", new ArrayList<>(c.getAllTagIndexes()));

        return doc;
    }

    /**
     * @brief Converte il documento in un contatto
     * @param[in] d Document da convertire
     * @return Contatto con i dati del documento
     */
    public Contact documentToContact(Document d) {
        Contact c = new Contact(d.getString("name"),d.getString("surname"));

        c.setNumbers(d.getList("numbers", String.class).toArray(new String[0]));
        c.setEmails(d.getList("emails", String.class).toArray(new String[0]));
        c.setProfilePicture(Base64.getDecoder().decode(d.getString("image")));

        for(int tagIndex : d.getList("tagIndexes", Integer.class).toArray(new Integer[0])) {
            c.addTagIndex(tagIndex);
        }

        return c;
    }

    /**
     * @brief Converte il tag in un documento
     * @param[in] tag Tag da convertire
     * @return Document con i dati del tag
     */
    public Document tagToDocument(Tag tag) {
        Document doc = new Document();

        doc.put("id",tag.getId());
        doc.put("description", tag.getDescription());

        return doc;
    }

    /**
     * @brief Converte il documento in un tag
     * @param[in] d Document da convertire
     * @return Tag con i dati del documento
     */
    public Tag documentToTag(Document d) {
        return new Tag(d.getString("description"),d.getInteger("id"));
    }
}
