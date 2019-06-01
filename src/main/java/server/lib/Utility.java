package server.lib;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

public class Utility {
    public static void launchProcess(String cmd) {
        String[] commands = { "bash", "-c", cmd };

        try {
            var pb = new ProcessBuilder(commands);
            pb.inheritIO();
            var p = pb.start();

            new Thread(new Runnable() {
                public void run() {
                    BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line = null;

                    try {
                        while ((line = input.readLine()) != null)
                            System.out.println(line);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(new Runnable() {
                public void run() {
                    BufferedReader input = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                    String line = null;

                    try {
                        while ((line = input.readLine()) != null)
                            System.out.println(line);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            p.waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Collection getAllMediaFilesFromLibrary() {
        Collection files = FileUtils.listFiles(
                new File("/opt/media"),
                new String[] { "wav", "mp3", "ogg", "wma", "aif", "aiff", "aifc", "aac", "flac", "alac" },
                true
        );

        return files;
    }

    public static int randomNumber(int min, int max) {
        return (int)Math.random() * ((max - min) + 1) + min;
    }
}
