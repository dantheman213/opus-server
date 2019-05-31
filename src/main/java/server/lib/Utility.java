package server.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    public static int randomNumber(int min, int max) {
        return (int)Math.random() * ((max - min) + 1) + min;
    }
}
