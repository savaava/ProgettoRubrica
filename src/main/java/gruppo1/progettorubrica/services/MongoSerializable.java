package gruppo1.progettorubrica.services;

import org.bson.Document;

public interface MongoSerializable<T> {
    /**
     * @brief Converte l'oggetto in un documento
     * @return Document con i dati dell'oggetto
     */
    Document toDocument();

    /**
     * @brief Converte un documento in un oggetto
     * @param[in] document Document da convertire
     * @return Oggetto con i dati del documento
     */
    T fromDocument(Document document);
}