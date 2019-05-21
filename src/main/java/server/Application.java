package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import server.lib.Database;
import server.tasks.MediaScanner;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class Application {

    public static void main(String[] args) {
        var database = new Database();
        var scanner = new MediaScanner();
        scanner.scan();

        SpringApplication.run(Application.class, args);
    }
}
