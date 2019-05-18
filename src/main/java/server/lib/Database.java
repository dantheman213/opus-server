package server.lib;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class Database {
    public static MongoClient client;
    public static MongoDatabase database;

    public Database() {
        try {
            System.out.println("Attempting to connect to database...");
            client = new MongoClient( "mongo" , 27017 );
            database = client.getDatabase("opus_db");
            System.out.println("Connected to database successfully!");
        } catch(Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
