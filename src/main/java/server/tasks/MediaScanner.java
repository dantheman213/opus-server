package server.tasks;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.parser.audio.AudioParser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.scheduling.annotation.Async;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

public class MediaScanner {
    @Async
    public void scan() throws Exception {
        Collection files = FileUtils.listFiles(
                new File("/opt/media"),
                new String[] { "wav", "mp3", "ogg", "wma", "aif", "aiff", "aifc", "aac", "flac", "alac" },
                true
        );

        System.out.println(String.format("Found %d media items.", files.size()));

        for (var file : files) {
            String artist = "",
                    album = "",
                    composer = "",
                    genre = "",
                    title = "",
                    year = "",
                    bitrate = "";
            String filePath = file.toString();
            Metadata metadata = new Metadata();

            System.out.println(String.format("Processing file %s now...", filePath));

            InputStream input = new FileInputStream(new File(filePath));
            ContentHandler handler = new DefaultHandler();

            String mediaExtension = FilenameUtils.getExtension(filePath);
            if (mediaExtension.equals("mp3")) {
                Parser parser = new Mp3Parser();
                ParseContext parseCtx = new ParseContext();
                parser.parse(input, handler, metadata, parseCtx);

                if (metadata != null) {
                    artist = metadata.get("xmpDM:artist");
                    if (StringUtils.isEmpty(artist)) {
                        artist = metadata.get("xmpDM:albumArtist");
                    }
                    album = metadata.get("xmpDM:album");
                    composer = metadata.get("xmpDM:composer");
                    genre = metadata.get("xmpDM:genre");
                    title = metadata.get("title");
                }
            }

            input.close();

            System.out.print(String.format("Artist: %s\nAlbum: %s\nComposer: %s\nGenre: %s\nTitle: %s\nYear: %s\nBitrate: %s",
                    artist,
                    album,
                    composer,
                    genre,
                    title,
                    year,
                    bitrate));
        }
    }
}
