package gruppo1.progettorubrica;

import gruppo1.progettorubrica.models.Tag;
import gruppo1.progettorubrica.services.Database;

public class DatabaseProvaSava {
    public static void main(String[] args) {
        Database db = new Database("mongodb+srv://rubricaContatti:tqHPmDYFftuxXE3g@mongisacluster.o8crvzq.mongodb.net/?retryWrites=true&w=majority&appName=MongisaCluster");
        
        db.deleteAllTags();
    }
}
