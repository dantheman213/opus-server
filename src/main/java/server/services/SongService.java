package server.services;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import server.lib.Database;
import server.models.SongModel;

import java.util.ArrayList;
import java.util.List;

public class SongService {
    private MongoCollection<SongModel> songCollection;

    public SongService() {
        songCollection = Database.database.getCollection("songs", SongModel.class);
    }

    public List<SongModel> getAllSongs() {
        var results = new ArrayList<SongModel>();
        for(var song : songCollection.find()) {
            results.add(song);
        }

        return results;
    }

    public SongModel getSongById(String id) {
        return songCollection.find(new Document("_id", new ObjectId(id))).first();
    }
}
