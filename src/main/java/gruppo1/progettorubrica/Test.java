package gruppo1.progettorubrica;

import gruppo1.progettorubrica.models.Contact;
import gruppo1.progettorubrica.services.Database;

import java.util.List;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        String url = "mongodb+srv://rubricaContatti:tqHPmDYFftuxXE3g@mongisacluster.o8crvzq.mongodb.net/?retryWrites=true&w=majority&appName=MongisaCluster";
        Database database = new Database(url);

        database.deleteAllContacts();
        Contact c = new Contact("Alessandro","Monte");
        String[] s = {"3667296040"};
        c.setNumbers(s);
        c.addTagIndex(1);
        database.insertContact(c);
    }
}
