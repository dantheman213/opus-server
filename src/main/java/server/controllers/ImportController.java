package server.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.services.ImportService;

@RestController
public class ImportController {
    @RequestMapping("/import/youtube/playlist/{id}")
    public String importYouTubePlaylist(@PathVariable String id) throws Exception {
        // TODO: Check if songs already exist
        new Thread(new Runnable() {
            public void run() {
                try {
                    var service = new ImportService();
                    service.importYoutubePlaylist(id);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

        return "ok. in queue.";
    }

    @RequestMapping("/import/youtube/video/{id}")
    public String importYouTubeVideo(@PathVariable String id) throws Exception {
        // TODO: Check if songs already exist
        new Thread(new Runnable() {
            public void run() {
                var service = new ImportService();
                service.importYoutubeVideoById(id);
            }
        }).start();

        return "ok. in queue.";
    }

    @RequestMapping("/import/spotify/playlist/{id}")
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
