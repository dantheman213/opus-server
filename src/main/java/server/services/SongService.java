package server.services;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import server.lib.Database;
import server.models.SongModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    public byte[] getSongBinary(String id) throws Exception {
        var song = this.getSongById(id);
        File file = new File(song.filePath);
        FileInputStream fileStream;
        byte[] buffer = null;

        try {
            fileStream = new FileInputStream(file);
            buffer = new byte[fileStream.available()];
            fileStream.read(buffer);
            fileStream.close();

            return buffer;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
