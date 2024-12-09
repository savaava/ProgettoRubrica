package gruppo1.progettorubrica;

import gruppo1.progettorubrica.models.Contact;
import gruppo1.progettorubrica.services.Database;

import java.util.List;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        String url = "mongodb+srv://rubricaContatti:tqHPmDYFftuxXE3g@mongisacluster.o8crvzq.mongodb.net/?retryWrites=true&w=majority&appName=MongisaCluster";

        Database database = new Database(url);
        Contact c = new Contact("Alessandro","Monte");
        String[] s = {"3667296040"};
        c.setNumbers(s);
        c.addTagIndex(1);
        database.insertContact(c);
        List<Contact> l = (List<Contact>) (database.getAllContacts());
        System.out.println(l.get(0).toString());
        //database.removeContact(c);
        List<Contact> l1 = (List<Contact>) (database.getAllContacts());
        System.out.println(l1.isEmpty());
    }
}
