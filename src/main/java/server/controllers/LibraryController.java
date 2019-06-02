package server.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import server.tasks.MediaScanner;

import java.util.ArrayList;

@RestController
@RequestMapping("/library")
@Api(value="All methods related to library.")
public class LibraryController {

    @RequestMapping(value = "/refresh", method= RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Check to see if any new media files are in the library.", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully initiated media scan."),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
    })
    public String refresh() throws Exception {
        var scanner = new MediaScanner();
        scanner.scan();

        return "ok. in queue.";
    }
}
