package gruppo1.progettorubrica.models;

import java.io.Serializable;

/**
 * @brief Modella un'entità tag
 * 
 * Permette di aggiungere 1 o più tag nella rubrica telefonica
 * @see AddressBook
 */
public class Tag implements Serializable{
    private final int id; ///< Identificativo univoco dell'entità tag
    private String description; ///< Descrizione dell'entità tag in formato stringa
    private static int index = 1; ///< Indice utilizzato per calcolare l'identificativo univoco dell'entità tag

    /**
     * @brief Costruttore per creare un'istanza della classe tag
     * @pre description != null
     * @post description != null
     * @param[in] description
     */
    public Tag(String description) {
        this.description = description;
        this.id = index++;
    }

    /**
     * @brief Costruttore per creare un'istanza della classe tag
     * @pre id > 0, description != null
     * @post id > 0, description != null
     * @param[in] description
     * @param[in] id
     */
    public Tag(String description, int id) {
        this.description = description;
        this.id = id;
    }

    /**
     * @pre Nessuna
     * @post Nessuna
     * @return L'identificativo univoco associato ad un oggetto di Tag
     */
    public int getId() {
        return id;
    }

    /**
     * @pre Nessuna
     * @post Nessuna
     * @return La descrizione associata ad un oggetto di Tag
     */
    public String getDescription() {
        return description;
    }

    /**
     * @brief Assegna il parametro in ingresso all'attributo descrizione dell'istanza di Tag
     * @param[in] description
     * @pre description != null
     * @post description != null
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @brief Assegna il parametro in ingresso all'attributo statico index dell'istanza di Tag
     * @param[in] index 
     * @pre index > 0
     * @post index > 0
     */
    public static void setIndex(int index) {
        Tag.index = index;
    }

    /**
     * @brief Controlla se l'istanza corrente e l'oggetto in ingresso sono uguali
     * @param[in] o
     * @pre Nessuna
     * @post Nessuna
     * @return True se sono uguali, altrimenti false
     */
    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(this == o) return true;
        if(this.getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return this.id == tag.id || this.description.equals(tag.description);
    }
    
}
