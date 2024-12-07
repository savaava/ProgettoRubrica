package gruppo1.progettorubrica.models;

import java.io.Serializable;
import org.bson.Document;

/**
 * @brief Tale classe modella un'entità tag.
 * 
 * Permette di aggiungere 1 o più tag nella rubrica telefonica.
 * @see AddressBook
 */
public class Tag implements Serializable{
    private final int id; ///< Identificativo univoco dell'entità tag.
    private String descrizione; ///< Descrizione dell'entità tag in formato stringa.
    private static int index = 1; ///< Indice utilizzato per calcolare l'identificativo univoco dell'entità tag.

    /**
     * @brief Costruttore per creare un'istanza della classe tag.
     * 
     * @param[in] descrizione 
     */
    public Tag(String descrizione) {
        this.descrizione = descrizione;
        this.id = index++;
    }

    /**
     * 
     * @return L'identificativo univoco associato ad un oggetto di Tag.
     */
    public int getId() {
        return id;
    }

    /**
     * 
     * @return La descrizione associata ad un oggetto di Tag.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @brief Assegna il parametro in ingresso all'attributo descrizione dell'istanza di Tag.
     * @param[in] descrizione 
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @brief Assegna il parametro in ingresso all'attributo statico index dell'istanza di Tag.
     * @param[in] index 
     */
    public static void setIndex(int index) {
        Tag.index = index;
    }

    /**
     * @brief Controlla se l'istanza corrente e l'oggetto in ingresso sono uguali.
     * @param[in] o Oggetto da confrontare.
     * @return True se sono uguali, altrimenti false.
     */
    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(this == o) return true;
        if(this.getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return this.id == tag.id || this.descrizione.equals(tag.descrizione);
    }
    
}
