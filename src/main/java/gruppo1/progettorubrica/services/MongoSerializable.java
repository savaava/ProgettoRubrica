package gruppo1.progettorubrica.services;

import org.bson.Document;

public interface MongoSerializable<T> {
    Document toDocument();
    T fromDocument(Document document);
}