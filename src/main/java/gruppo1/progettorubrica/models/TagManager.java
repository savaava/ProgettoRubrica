package gruppo1.progettorubrica.models;

import javafx.collections.ObservableList;

/**
 * @brief Interfaccia per la gestione dei tag.
 * @see Tag
 */
public interface TagManager {
    /**
     * @brief Aggiunge un tag.
     * @param[in] tag Tag da aggiungere.
     * @pre nessuna (il parametro tag può essere null).
     * @post Aggiorna la lista dei tags dell'AddressBook inserendovi il nuovo tag
     */
    void addTag(Tag tag);

    /**
     * @brief Rimuove un tag.
     * @param[in] tag Tag da rimuovere.
     * @pre nessuna (il parametro tag può essere null).
     * @post Aggiorna la lista dei tags dell'AddressBook rimuovendo il tag
     */
    void removeTag(Tag tag);

    /**
     * @brief Trova il tag corrispondente ad un certo id
     * @param[in] id
     * @pre nessuna (id può essere minore e uguale di 0).
     * @return Il tag associato al parametro in ingresso id, null se non lo trova.
     */
    Tag getTag(int id);

    /**
     * @pre nessuna (ci possono essere 0 o più tags).
     * @post Il client ottiene la lista completa dei tags dell'AddressBook
     * @return L'insieme di tag presenti nell'AddressBook.
     */
    ObservableList<Tag> getAllTags();
}
