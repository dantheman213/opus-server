package server.services;

import org.springframework.scheduling.annotation.Async;
import server.lib.Utility;
import server.tasks.MediaScanner;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    @Async
    public void importSpotifyPlaylist(String id) throws Exception {
        URL url = new URL("http://spotify-playlist-to-json:3000/playlist/" + id);
        var connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(800);
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream (
                connection.getOutputStream());
        wr.close();
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        var response = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();

        var result = response.toString();
        connection.disconnect();

        System.out.print(result);
    }
}
