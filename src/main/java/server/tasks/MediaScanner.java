package server.tasks;

import org.springframework.scheduling.annotation.Async;
import server.lib.Utility;
import server.services.LibraryService;

public class MediaScanner {
    private static boolean isRunning = false;

    @Async
    public void scan() {
        if (isRunning)
            return;

        isRunning = true;
        try {
            var files = Utility.getAllMediaFilesFromLibrary();
            System.out.println(String.format("Found %d media items.", files.size()));

            var service = new LibraryService();
            for (var file : files) {
                service.addItemToLibrary(file.toString());
            }

            // TODO: Use updatedAt field to see which items no longer exist and remove them from database.
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        isRunning = false;
    }
}
