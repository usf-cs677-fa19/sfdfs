package edu.usfca.cs.dfs.fileUtil;


import java.io.IOException;
import java.util.Scanner;

public class Authenticator {

    /**
     *
     * @param filenames
     */
    public static void sha1sumOfFiles(String[] filenames) {
        StringBuilder result = new StringBuilder();
        try {
            Process process1 = new ProcessBuilder("pwd").start();
            process1.waitFor();
            Scanner scanner1 = new Scanner(process1.getInputStream());
            while(scanner1.hasNextLine()) {
                String one = scanner1.nextLine()+"\n";
                //System.out.println("Running sha1sum\n" + one);
                result.append(one);
            }
            process1.destroy();

            for (String filename:filenames) {

                Process process = new ProcessBuilder("sha1sum", filename).start();
                process.waitFor();

                Scanner scanner = new Scanner(process.getInputStream());

                while(scanner.hasNextLine()) {
                    String one = scanner.nextLine()+"\n";
                    //System.out.println("Running sha1sum\n" + one);
                    result.append(one);

                }
                process.destroy();
            }

            System.out.println(scanner1.toString()+"\nRunning sha1sum\n" + result.toString());

        } catch (IOException | InterruptedException ioe) {
            System.out.println("Exception in sha1sumOfFile :"+ ioe.getMessage());
            ioe.printStackTrace();
        }
    }
}
