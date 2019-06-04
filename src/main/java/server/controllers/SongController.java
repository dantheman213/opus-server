package server.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import server.models.domain.SongDomainModel;
import server.services.SongService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/songs")
@Api(value="All methods related to song object.")
public class SongController {
    @RequestMapping(value = "/", method= RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get a list of all songs in library.", response = ArrayList.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public List<SongDomainModel> getAllSongs() throws Exception {
        var service = new SongService();
        return service.getAllSongs();
    }

    @RequestMapping(value = "/play/{id}", method= RequestMethod.GET)
    @ApiOperation(value = "Play song by providing song id.", response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved song"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public void playSongById(HttpServletResponse response, @PathVariable String id) throws Exception {
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
