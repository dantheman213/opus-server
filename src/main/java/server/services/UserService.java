package server.services;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import server.lib.Database;
import server.lib.Utility;
import server.models.domain.UserDomainModel;
import server.models.request.UserRegisterRequestModel;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private MongoCollection<UserDomainModel> userCollection;

    public UserService() {
        userCollection = Database.database.getCollection("users", UserDomainModel.class);
    }

    public List<UserDomainModel> getAllUsers() {
        var results = new ArrayList<UserDomainModel>();
        for(var user : userCollection.find()) {
            results.add(user);
        }

        return results;
    }

    public UserDomainModel getUserById(String id) {
        return userCollection.find(new Document("_id", new ObjectId(id))).first();
    }

    public boolean registerUser(UserRegisterRequestModel model) throws Exception {
        var result = Utility.generatePasswordHashAndSalt(model.password);
        var user = new UserDomainModel();
        user.isAdmin = model.isAdmin;
        user.passwordHash = result.getValue1();
        user.passwordSalt = result.getValue0();
        user.username = model.username;

        userCollection.insertOne(user);
        System.out.println(String.format("User %s has been added to the server!", user.username));

        return true;
    }
}
