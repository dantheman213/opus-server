package server.services;

import com.mongodb.client.MongoCollection;
import com.mpatric.mp3agic.Mp3File;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.springframework.util.StringUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;
import server.lib.Database;
import server.models.SongModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Date;

public class LibraryService {
    private MongoCollection<SongModel> songCollection;

    public LibraryService() {
        songCollection = Database.database.getCollection("songs", SongModel.class);
    }

    public void addItemToLibrary(String filePath) throws Exception {
        var song = new SongModel();
        song.filePath = filePath;
        String fileName = Paths.get(song.filePath).getFileName().toString();

        System.out.println(String.format("Processing file %s now...", fileName));

        InputStream input = new FileInputStream(new File(song.filePath));
        ContentHandler handler = new DefaultHandler();

        String mediaExtension = FilenameUtils.getExtension(song.filePath);
        if (mediaExtension.equals("mp3")) {
            var file = new Mp3File(song.filePath);
            if (file.hasId3v2Tag()) {
                var tags = file.getId3v2Tag();

                song.artist = tags.getArtist();
                song.album = tags.getAlbum();
                song.composer = tags.getComposer();
                song.genre = tags.getGenreDescription();
                song.duration = file.getLengthInSeconds();
                song.bitrate = Integer.toString(file.getBitrate());
                song.releaseDate = tags.getYear();
                song.trackNumber = tags.getTrack();
                song.title = tags.getTitle();
                if (StringUtils.isEmpty(song.title)) {
                    song.title = FilenameUtils.removeExtension(fileName);
                }
            } else {
                System.out.println(String.format("ERROR: Could not get metadata from %s...!", fileName));
            }
        } else {
            System.out.println(String.format("Audio extension %s is not currently supported. However, plans are being made to support it. SKIPPING '%s' FOR NOW...", mediaExtension, fileName));
        }
        // TODO: Support all audio formats listed above.

        input.close();
        song.updatedAt = new Date();

        System.out.print(String.format("Artist: %s\nAlbum: %s\nComposer: %s\nGenre: %s\nTitle: %s\nYear: %s\nBitrate: %s\n",
                song.artist,
                song.album,
                song.composer,
                song.genre,
                song.title,
                song.releaseDate,
                song.bitrate));

        var resultDocuments = songCollection.find(new Document("filePath", song.filePath));
        if(resultDocuments.first() == null) {
            songCollection.insertOne(song);
            System.out.println(String.format("Song %s has been added to database!", song.title));
        } else {
            var existingSong = resultDocuments.first();
            existingSong.updatedAt = new Date();

            // TODO: Check for metadata updates and if so update database with new values.

            songCollection.replaceOne(new Document("filePath", song.filePath), existingSong);
            System.out.println(String.format("Song %s has been skipped!", song.title));
        }
    }
}
