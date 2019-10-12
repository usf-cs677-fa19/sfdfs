package edu.usfca.cs.dfs.init;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Utility class includes method to check program parameters and reading config in json
 */
public class Init {

    /**
     * isCorrectArgs checks if the program params are correct
     * @param args
     * @return
     */
    public static Boolean isCorrectArgs(String[] args) {
        return ((args.length == 1) && (args[0].contains(".json")));
//        return ((args.length == 2) &&
//                ((args[0].equalsIgnoreCase("controller")) || (args[0].equalsIgnoreCase("storage"))) &&
//                (Init.isInt(args[1]))
//        );
    }

    private static boolean isInt(String strNum) {
        try {
            int i = Integer.parseInt(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Reads the config file in Map
     * @param filename
     */
    public static Map<String, String> readConfigFileIntoMap(String filename) {
//        return new Gson().fromJson(Fileify.readUsingBufferedReader(filename), Map.class); Todo:decide on this or below
        return new Gson().fromJson(Init.readUsingFileChannel(filename,4096), Map.class);

    }

//    /**
//     * Reads the config file in Map
//     * @param filename
//     */
//    public static Object[] readConfigFileIntoMap(String filename, Class c) {
////        return new Gson().fromJson(Fileify.readUsingBufferedReader(filename), Map.class); Todo:decide on this or below
//        return new Gson().fromJson(Init.readUsingFileChannel(filename,4096), c);
//
//    }


    /**
     * readUsingFileChannel using RandomAccessFile and ByteArrayOutputStream
     * @param filename
     * @return file content in string
     */
    public static String readUsingFileChannel(String filename, int bufferSize) {
        try(
                RandomAccessFile reader = new RandomAccessFile(filename, "r");
                FileChannel fc = reader.getChannel();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            //int bufferSize = 4096;
            if (bufferSize > fc.size()) {
                bufferSize = (int)fc.size();
            }
            ByteBuffer buff = ByteBuffer.allocate(bufferSize);
            while (fc.read(buff) > 0) {
                out.write(buff.array(),0,buff.position());
                buff.clear();
            }

            String fileContent = new String(out.toByteArray(), StandardCharsets.UTF_8);
            return fileContent;

        } catch (FileNotFoundException e) {
            System.out.println("Exception in readUsingFileChannel - FileNotFoundException : "+e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Exception in readUsingFileChannel - IOException : "+e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

}