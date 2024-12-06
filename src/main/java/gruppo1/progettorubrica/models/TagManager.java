package gruppo1.progettorubrica.models;

import javafx.collections.ObservableList;

public interface TagManager {
    /**
     * @brief Aggiunge un tag.
     * @param[in] tag Tag da aggiungere.
     */
    void addTag(String tag);

    /**
     * @brief Rimuove un tag.
     * @param[in] tag Tag da rimuovere.
     * @return Il tag rimosso.
     */
    String removeTag(String tag);

    /**
     * @return L'insieme di tag inseriti.
     */
    ObservableList<String> getTags();
}
