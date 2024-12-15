package gruppo1.progettorubrica.models;

import java.io.IOException;
import java.util.Collection;
import javafx.collections.ObservableList;

/**
 * @brief Interfaccia per la gestione dei tag.
 * @see Tag
 */
public interface TagManager {
    /**
     * @pre nessuna (ci possono essere 0 o più tags).
     * @post Il client ottiene la lista completa dei tags dell'AddressBook
     * @return L'insieme di tag presenti nell'AddressBook.
     */
    ObservableList<Tag> getAllTags();
    
    /**
     * @brief Aggiunge un tag.
     * @param[in] tag Tag da aggiungere.
     * @pre nessuna (il parametro tag può essere null).
     * @post Aggiorna la lista dei tags dell'AddressBook inserendovi il nuovo tag
     */
    void addTag(Tag tag) throws IOException;

    /**
     * @brief Aggiunge 1 o più tags dalla collezione alla rubrica telefonica.
     * @param[in] c Collezione di tags da aggiungere.
     * @pre nessuna (il parametro c può essere null).
     * @post Aggiorna la lista dei tags dell'AddressBook inserendovi gli eventuali tags da c.
     */
    void addManyTags(Collection<Tag> c) throws IOException;
    
    /**
     * @brief Rimuove un tag.
     * @param[in] tag Tag da rimuovere.
     * @pre nessuna (il parametro tag può essere null).
     * @post Aggiorna la lista dei tags dell'AddressBook rimuovendo il tag
     */
    void removeTag(Tag tag) throws IOException;

    /**
     * @brief Trova il tag corrispondente ad un certo id
     * @param[in] id
     * @pre nessuna (id può essere minore e uguale di 0).
     * @return Il tag associato al parametro in ingresso id, null se non lo trova.
     */
    Tag getTag(int id);
    
    /**
     * @brief Trova il tag corrispondente ad una certa stringa
     * @param[in] descrizione 
     * @pre nessuna
     * @post il client ottiene l'eventuale tag che contiene la stringa descrizione in ingresso
     * @return il tag associato a descrizione, null se non esiste.
     */
    Tag getTag(String description);
}
