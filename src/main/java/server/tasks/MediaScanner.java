package server.tasks;

import org.apache.commons.io.FileUtils;
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
    public void scan() {
        Collection files = FileUtils.listFiles(
                new File("/opt/media"),
                new String[] { "wav", "mp3", "ogg", "wma", "aif", "aiff", "aifc", "aac", "flac", "alac" },
                true
        );

        System.out.println(String.format("Found %d media items.", files.size()));

        files.forEach(file -> {
            System.out.println(String.format("Processing file %s now...", file.toString()));
            Metadata metadata = null;
            try {
                InputStream input = new FileInputStream(new File(file.toString()));
                ContentHandler handler = new DefaultHandler();
                metadata = new Metadata();
                Parser parser = new Mp3Parser();
                ParseContext parseCtx = new ParseContext();
                parser.parse(input, handler, metadata, parseCtx);
                input.close();
            } catch(Exception ex) {
                ex.printStackTrace();
            }

            if(metadata != null) {
                // Retrieve the necessary info from metadata
                // Names - title, xmpDM:artist etc. - mentioned below may differ based
                System.out.println("----------------------------------------------");
                System.out.println("Title: " + metadata.get("title"));
                System.out.println("Artists: " + metadata.get("xmpDM:artist"));
                System.out.println("Composer : "+metadata.get("xmpDM:composer"));
                System.out.println("Genre : "+metadata.get("xmpDM:genre"));
                System.out.println("Album : "+metadata.get("xmpDM:album"));
            }
        });
    }
}
