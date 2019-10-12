package edu.usfca.cs.dfs;

import edu.usfca.cs.dfs.init.ConfigSystemParam;
import edu.usfca.cs.dfs.init.ConfigSystemParams;
import edu.usfca.cs.dfs.init.Init;

import java.io.IOException;
import java.util.Map;

public class StartSystem {


    public static void main(String[] args) throws IOException {
        if(Init.isCorrectArgs(args)) { //if configSystem.json specified

            //read readConfigFile to ConfigSystemParams
            ConfigSystemParams configParams = new ConfigSystemParams(args[0]);
            System.out.println(configParams.toString());

            //  ConfigSystemParams :
            for(ConfigSystemParam nodeParams :configParams.getParams()) { // for each Node Params
                    new NodeServer(nodeParams).run();
            }

        }
    }


}
