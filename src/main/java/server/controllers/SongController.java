package server.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.models.SongModel;
import server.services.SongService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class SongController {
    @RequestMapping("/songs")
    public List<SongModel> getAllSongs() throws Exception {
        var service = new SongService();
        return service.getAllSongs();
    }

    @RequestMapping("/songs/play/{id}")
    public void playSongById(@PathVariable String id, HttpServletResponse response) throws Exception {
        var service = new SongService();
        var data = service.getSongBinary(id);
        // TODO detect media type based on file extension
        response.setContentType("audio/mpeg");

        try{
            response.getOutputStream().write(data);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
