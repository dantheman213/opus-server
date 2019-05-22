package server.services;

import org.springframework.scheduling.annotation.Async;
import server.lib.Utility;
import server.tasks.MediaScanner;

import java.util.Date;

public class ImportService {
    @Async
    public void importYoutubePlaylist(String id) {
        var now = new Date().getTime();
        Utility.launchProcess("youtube-dl -f bestvideo+bestaudio --extract-audio --audio-format mp3 --add-metadata --output '/opt/staging/" + now + "/%(title)s.%(ext)s' --playlist-random https://www.youtube.com/playlist?list=" + id);
        Utility.launchProcess("mv /opt/staging/" + now + "/*.mp3 /opt/media/.");
        Utility.launchProcess("rm -rf /opt/staging/" + now);

        var scanner = new MediaScanner();
        scanner.scan();
    }

    @Async
    public void importYoutubeVideo(String id) {
        var now = new Date().getTime();
        Utility.launchProcess("youtube-dl -f bestvideo+bestaudio --extract-audio --audio-format mp3 --add-metadata --output '/opt/staging/" + now + "/%(title)s.%(ext)s' https://www.youtube.com/watch?v=" + id);
        Utility.launchProcess("mv /opt/staging/" + now + "/*.mp3 /opt/media/");
        Utility.launchProcess("rm -rf /opt/staging/" + now);

        var scanner = new MediaScanner();
        scanner.scan();
    }
}
