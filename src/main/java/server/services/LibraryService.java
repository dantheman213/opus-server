package server.services;

import com.mongodb.client.MongoCollection;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.bson.Document;
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
        Metadata metadata = new Metadata();

        System.out.println(String.format("Processing file %s now...", fileName));

        InputStream input = new FileInputStream(new File(song.filePath));
        ContentHandler handler = new DefaultHandler();

        String mediaExtension = FilenameUtils.getExtension(song.filePath);
        if (mediaExtension.equals("mp3")) {
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);

            if (metadata != null) {
                song.artist = metadata.get("xmpDM:artist");
                if (StringUtils.isEmpty(song.artist)) {
                    song.artist = metadata.get("xmpDM:albumArtist");
                }
                if (StringUtils.isEmpty(song.artist)) {
                    song.artist = metadata.get("xmpDM:albumArtist");
                }
                if (StringUtils.isEmpty(song.artist)) {
                    song.artist = metadata.get("Author");
                }
                if (StringUtils.isEmpty(song.artist)) {
                    song.artist = metadata.get("creator");
                }
                if (StringUtils.isEmpty(song.artist)) {
                    song.artist = metadata.get("dc:creator");
                }
                song.album = metadata.get("xmpDM:album");
                song.composer = metadata.get("xmpDM:composer");
                song.genre = metadata.get("xmpDM:genre");
                song.duration = Float.parseFloat(metadata.get("xmpDM:duration"));
                song.releaseDate = metadata.get("xmpDM:releaseDate");
                song.trackNumber = metadata.get("xmpDM:trackNumber");
                song.title = metadata.get("title");
                if  (StringUtils.isEmpty(song.title)) {
                    song.title = metadata.get("dc:title");
                }
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
