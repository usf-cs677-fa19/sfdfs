package edu.usfca.cs.dfs;

import edu.usfca.cs.dfs.init.ConfigSystemParam;
import edu.usfca.cs.dfs.init.Init;
import edu.usfca.cs.dfs.nodes.NodeServer;

import java.io.IOException;

public class StorageNode {


    public static void main(String[] args) throws IOException {
        if(Init.isCorrectArgs(args)) { //if configSystem.json specified

            //read readConfigFile to ConfigSystemParams
//            ConfigSystemParam configParams = new ConfigSystemParam(args[0]);
//            System.out.println(configParams.toString());
            new ConfigSystemParam(args[0]);
            System.out.println(ConfigSystemParam.getString());

            //  ConfigSystemParams :
//            for(ConfigSystemParam nodeParams :configParams.getParams()) { // for each Node Params
            new NodeServer().run();
//            }

        }
    }


}
