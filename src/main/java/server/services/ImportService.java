package server.services;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.scheduling.annotation.Async;
import server.lib.Utility;
import server.tasks.MediaScanner;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
        String url = "http://spotify-playlist-to-json:3000/playlist/" + id;

        var client = HttpClientBuilder.create().setConnectionTimeToLive(300, TimeUnit.SECONDS).build();
        var request = new HttpGet(url);
        var response = client.execute(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            String responseString = new BasicResponseHandler().handleResponse(response);
            request.releaseConnection();
            System.out.println(responseString);
        } else {
            request.releaseConnection();
            throw new Exception("Spotify playlist integration failed.");
        }
    }
}
