package gruppo1.progettorubrica.models;

import javafx.collections.ObservableList;

/**
 * @brief Interfaccia per la gestione dei contatti.
 */
public interface ContactManager {

    /**
     * @brief Ottiene la lista osservabile di tutti i contatti.
     * @return Lista osservabile
     */
    ObservableList<Contact> getAllContacts();

    /**
     * @brief Aggiunge un contatto alla rubrica telefonica.
     *
     * @param[in] c Contatto da aggiungere.
     */
    void addContact(Contact c);

    /**
     * @brief Rimuove la prima occorrenza del contatto.
     * @param[in] c Contatto da rimuovere.
     * @return Contatto rimosso.
     */
    void removeContact(Contact c);
}
