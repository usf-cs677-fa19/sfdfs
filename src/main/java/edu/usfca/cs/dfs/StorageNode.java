package edu.usfca.cs.dfs;

import edu.usfca.cs.dfs.init.ConfigSystemParam;
import edu.usfca.cs.dfs.init.Init;
import edu.usfca.cs.dfs.nodes.NodeServer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StorageNode {
public static Logger logger = Logger.getLogger(StorageNode.class.getName());

    public static void main(String[] args) throws IOException {
        if(Init.isCorrectArgs(args)) { //if configSystem.json specified
            new ConfigSystemParam(args[0]);
            System.out.println(ConfigSystemParam.getString());

            new NodeServer().run();
        } else {
            logger.log(Level.INFO,"give config json as param");
        }
    }


}
