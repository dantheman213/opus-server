package server.services;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import server.lib.Database;
import server.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private MongoCollection<UserModel> userCollection;

    public UserService() {
        userCollection = Database.database.getCollection("users", UserModel.class);
    }

    public List<UserModel> getAllUsers() {
        var results = new ArrayList<UserModel>();
        for(var user : userCollection.find()) {
            results.add(user);
        }

        return results;
    }

    public UserModel getUserById(String id) {
        return userCollection.find(new Document("_id", new ObjectId(id))).first();
    }
}
