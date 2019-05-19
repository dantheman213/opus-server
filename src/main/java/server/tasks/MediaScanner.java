package server.tasks;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.bson.Document;
import org.springframework.scheduling.annotation.Async;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;
import server.lib.Database;
import server.models.SongModel;

public class MediaScanner {
    private static boolean isRunning = false;

    @Async
    public void scan() {
        if (isRunning)
            return;

        isRunning = true;

        try {
            Collection files = FileUtils.listFiles(
                    new File("/opt/media"),
                    new String[] { "wav", "mp3", "ogg", "wma", "aif", "aiff", "aifc", "aac", "flac", "alac" },
                    true
            );

            System.out.println(String.format("Found %d media items.", files.size()));

            var songCollection = Database.database.getCollection("songs");
            Gson gson = new Gson();

            for (var file : files) {
                var song = new SongModel();
                song.filePath = file.toString();
                Metadata metadata = new Metadata();

                System.out.println(String.format("Processing file %s now...", song.filePath));

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
                        song.album = metadata.get("xmpDM:album");
                        song.composer = metadata.get("xmpDM:composer");
                        song.genre = metadata.get("xmpDM:genre");
                        song.title = metadata.get("title");
                        if (StringUtils.isEmpty(song.title)) {
                            song.title = "Untitled Song";
                        }
                    }
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
                        song.year,
                        song.bitrate));

                var resultDocuments = songCollection.find(new Document("filePath", song.filePath));
                if(resultDocuments.first() == null) {
                    Document newSongDocument = Document.parse(gson.toJson(song));
                    songCollection.insertOne(newSongDocument);
                    System.out.println(String.format("Song %s has been added to database!", song.title));
                } else {
                    var existingSong = resultDocuments.first();
                    existingSong.put("updatedAt", new Date());

                    // TODO: Check for metadata updates and if so update database with new values.

                    songCollection.replaceOne(new Document("filePath", song.filePath), existingSong);
                    System.out.println(String.format("Song %s has been skipped!", song.title));
                }
            }

            // TODO: Use updatedAt field to see which items no longer exist and remove them from database.
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        isRunning = false;
    }
}
