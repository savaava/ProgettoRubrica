/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppo1.progettorubrica.models;

import javafx.collections.ObservableList;


public interface ContactManager {
    
    /**
     * @return La lista di contatti.
     */
    ObservableList<Contact> getContacts();
    
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
    Contact removeContact(Contact c);
    
}
