package server.controllers;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.tasks.MediaScanner;

@RestController
public class MediaController {

    @RequestMapping("/media")
    public String index() {
        return "hello world";
    }

    @RequestMapping("/media/refresh")
    public String refresh() throws Exception {
        MediaScanner scanner = new MediaScanner();
        scanner.scan();

        return "ok";
    }
}
