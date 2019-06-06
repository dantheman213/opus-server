package server.lib;

import org.apache.commons.io.FileUtils;
import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
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

    public static Pair<String, String> generatePasswordHashAndSalt(String password) throws Exception {
        var salt = generateSalt();
        var calculatedHash = calculateHash(password, new String(salt));
        return new Pair<String, String>(new String(salt), new String(calculatedHash));
    }

    public static boolean validatePassword(String password, String hash, String salt) throws Exception {
        // calculate hash (and use salt)
        var calculatedHash = calculateHash(password, salt);
        return calculatedHash.equals(hash);
    }

    private static String generateSalt() throws Exception {
        var random = new SecureRandom();
        var salt = new byte[16];
        random.nextBytes(salt);
        return new String(salt);
    }

    private static String calculateHash(String password, String salt) throws Exception {
        var md = MessageDigest.getInstance("SHA-512");
        md.update(salt.getBytes(StandardCharsets.UTF_8));
        var bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
        var sb = new StringBuilder();

        for(int i=0; i< bytes.length ;i++){
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
