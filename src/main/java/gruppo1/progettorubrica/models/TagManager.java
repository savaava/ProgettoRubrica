package gruppo1.progettorubrica.models;

import javafx.collections.ObservableList;

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

    Tag getTag(int id);

    /**
     * @return L'insieme di tag inseriti.
     */
    ObservableList<Tag> getAllTags();
}
