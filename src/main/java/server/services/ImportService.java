package server.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.util.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.uima.resource.metadata.Import;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import server.lib.Utility;
import server.tasks.MediaScanner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
            if (httpResponse != null) {
                System.out.println(httpResponse.getStatusLine().getStatusCode());
            }

            ex.printStackTrace();
            return;
        }

        var playlist = new JSONObject(result);
        var tracks = playlist.getJSONArray("tracks");

        for (int i = 0; i < tracks.length(); i++) {
            var track = tracks.getJSONObject(i);
            String searchQuery = String.format("%s %s", track.getJSONArray("artists").getJSONObject(0).getString("name"), track.getJSONObject("song").getString("title"));
            System.out.println("Search Query: " + searchQuery);

            new Thread(new Runnable() {
                public void run() {
                    var service = new ImportService();
                    service.importYoutubeVideoBySearch(searchQuery);
                }
            }).start();
            Thread.sleep(Utility.randomNumber(2000, 3500));
        }
    }
}
