package server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.tasks.MediaScanner;

@RestController
public class LibraryController {

    @RequestMapping("/library/refresh")
    public String refresh() throws Exception {
        var scanner = new MediaScanner();
        scanner.scan();

        return "ok. in queue.";
    }
}
