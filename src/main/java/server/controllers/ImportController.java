package server.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import server.services.ImportService;

import java.util.ArrayList;

@RestController
@RequestMapping("/import")
@Api(value="All methods related to song object.")
public class ImportController {
    @RequestMapping(value = "/youtube/playlist/{id}", method= RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Import tracks from YouTube playlist by providing its id.", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully initiated import"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
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

    @RequestMapping(value = "/youtube/video/{id}", method= RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Import YouTube video by providing its id.", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully initiated import"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
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

    @RequestMapping(value = "/spotify/playlist/{id}", method= RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Import Spotify playlist metadata by providing its id while using YouTube as source to download media from.", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully initiated import"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
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
