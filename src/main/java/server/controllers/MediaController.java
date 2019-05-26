package server.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.models.SongModel;
import server.services.ImportService;
import server.services.SongService;
import server.tasks.MediaScanner;

import javax.servlet.http.HttpServletResponse;

@RestController
public class MediaController {
    @RequestMapping("/media/songs")
    public List<SongModel> getAllSongs() throws Exception {
        var service = new SongService();
        return service.getAllSongs();
    }

    @RequestMapping("/media/songs/play/{id}")
    public void playSongById(@PathVariable String id, HttpServletResponse response) throws Exception {
        var service = new SongService();
        var song = service.getSongById(id);
        File file = new File(song.filePath);
        FileInputStream fileStream;
        byte[] buffer = null;

        try {
            fileStream = new FileInputStream(file);
            buffer = new byte[fileStream.available()];
            fileStream.read(buffer);
            fileStream.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            // do nothing
        }

        // TODO detect media type based on file extension
        response.setContentType("audio/mpeg");

        try{
            response.getOutputStream().write(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping("/media/refresh")
    public String refresh() throws Exception {
        MediaScanner scanner = new MediaScanner();
        scanner.scan();

        return "ok";
    }

    @RequestMapping("/media/import/youtube/playlist/{id}")
    public String importYouTubePlaylist(@PathVariable String id) throws Exception {
        // TODO: Check if songs already exist
        new Thread(new Runnable() {
            public void run() {
                var service = new ImportService();
                service.importYoutubePlaylist(id);
            }
        }).start();

        return "ok. in queue.";
    }

    @RequestMapping("/media/import/youtube/video/{id}")
    public String importYouTubeVideo(@PathVariable String id) throws Exception {
        // TODO: Check if songs already exist
        new Thread(new Runnable() {
            public void run() {
                var service = new ImportService();
                service.importYoutubeVideo(id);
            }
        }).start();

        return "ok. in queue.";
    }

    @RequestMapping("/media/import/spotify/playlist/{id}")
    public String importSpotifyPlaylist(@PathVariable String id) throws Exception {
        // TODO: Check if songs already exist
        new Thread(new Runnable() {
            public void run() {
                try {
                    var service = new ImportService();
                    service.importSpotifyPlaylist(id);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

        return "ok. in queue.";
    }
}
