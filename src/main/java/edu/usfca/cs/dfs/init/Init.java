package edu.usfca.cs.dfs.init;

import com.google.gson.Gson;
        import edu.usfca.cs.dfs.fileUtil.Fileify;

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
    }

    /**
     * Reads the config file in Map
     * @param filename
     */
    public static Map<String, String> readConfigFileIntoMap(String filename) {
//        return new Gson().fromJson(Fileify.readUsingBufferedReader(filename), Map.class); Todo:decide on this or below
        return new Gson().fromJson(Fileify.readUsingFileChannel(filename,4096), Map.class);

    }

}