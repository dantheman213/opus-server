package server.lib;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Database {
    public static MongoClient client;
    public static MongoDatabase database;

    public Database() {
        try {
            System.out.println("Attempting to connect to database...");
            CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));

            client = new MongoClient( "mongo", MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
            database = client.getDatabase("opus_db");
            System.out.println("Connected to database successfully!");
        } catch(Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
