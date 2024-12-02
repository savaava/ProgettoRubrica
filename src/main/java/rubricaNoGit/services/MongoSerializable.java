package rubricaNoGit.services;

import org.bson.Document;

public interface MongoSerializable<T> {
    Document toDocument();
    T fromDocument(Document document);
}
