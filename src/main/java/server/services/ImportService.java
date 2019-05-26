package server.services;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.scheduling.annotation.Async;
import server.lib.Utility;
import server.models.SpotifyMusicItemModel;
import server.tasks.MediaScanner;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    public void importYoutubeVideoById(String id) {
        var now = new Date().getTime();
        Utility.launchProcess("youtube-dl -f bestvideo+bestaudio --extract-audio --audio-format mp3 --add-metadata --output '/opt/staging/" + now + "/%(title)s.%(ext)s' https://www.youtube.com/watch?v=" + id);
        Utility.launchProcess("mv /opt/staging/" + now + "/*.mp3 /opt/media/");
        Utility.launchProcess("rm -rf /opt/staging/" + now);

        var scanner = new MediaScanner();
        scanner.scan();
    }

    @Async
    public void importYoutubeVideoBySearch(String search) {
        var now = new Date().getTime();
        Utility.launchProcess("youtube-dl -f bestvideo+bestaudio --extract-audio --audio-format mp3 --add-metadata --output '/opt/staging/" + now + "/%(title)s.%(ext)s' 'ytsearch1:" + search + "'");
        Utility.launchProcess("mv /opt/staging/" + now + "/*.mp3 /opt/media/");
        Utility.launchProcess("rm -rf /opt/staging/" + now);

        var scanner = new MediaScanner();
        scanner.scan();
    }

    @Async
    public void importSpotifyPlaylist(String id) throws Exception {
        String url = "http://spotify-playlist-to-json:3000/playlist/" + id;

        BasicCookieStore cookieStore = null;
        HttpResponse httpResponse = null;
        var httpClient = HttpClients.createDefault();
        var request = new HttpGet(url);
        String result;
        try {
            httpResponse = httpClient.execute(request);

            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()))) {
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }

            result = stringBuilder.toString();
            httpResponse.getEntity().getContent().close();
        } catch (Exception ex) {
            System.out.println(httpResponse.getStatusLine().getStatusCode());
            ex.printStackTrace();
            return;
        }

        Gson gson = new Gson();
        SpotifyMusicItemModel[] playlist = gson.fromJson(result, SpotifyMusicItemModel[].class);
        for (var song : playlist) {
            String searchQuery = String.format("%s %s song", song.artist, song.title);
            System.out.println("Search Query: " + searchQuery);
            this.importYoutubeVideoBySearch(searchQuery);
        }
    }
}
