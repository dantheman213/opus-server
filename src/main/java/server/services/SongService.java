package server.services;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import server.lib.Database;
import server.lib.GsonBootstrap;
import server.models.SongModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SongService {
    Gson gson;
    MongoCollection<Document> songCollection;

    public SongService() {
        gson = GsonBootstrap.create();
        songCollection = Database.database.getCollection("songs");
    }

    public List<SongModel> getAllSongs() {
        var results = new ArrayList<SongModel>();
        for(var song : songCollection.find()) {
            var result = gson.fromJson(song.toJson(), SongModel.class);
            results.add(result);
        }

        return results;
    }
}
