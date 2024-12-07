package gruppo1.progettorubrica.models;

import javafx.collections.ObservableList;

/**
 * @brief Interfaccia per la gestione dei tag.
 */
public interface TagManager {
    /**
     * @brief Aggiunge un tag.
     * @param[in] tag Tag da aggiungere.
     */
    void addTag(Tag tag);

    /**
     * @brief Rimuove un tag.
     * @param[in] tag Tag da rimuovere.
     * @return Il tag rimosso.
     */
    Tag removeTag(Tag tag);

    /**
     * 
     * @param[in] id
     * @return Il tag associato al parametro in ingresso id.
     */
    Tag getTag(int id);

    /**
     * @return L'insieme di tag inseriti.
     */
    ObservableList<Tag> getAllTags();
}
