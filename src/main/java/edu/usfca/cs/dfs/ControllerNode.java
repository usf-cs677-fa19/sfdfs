package edu.usfca.cs.dfs;

import edu.usfca.cs.dfs.init.ConfigSystemParam;
import edu.usfca.cs.dfs.init.Init;
import edu.usfca.cs.dfs.nodes.NodeServer;

import java.io.IOException;

public class ControllerNode {


    public static void main(String[] args) throws IOException {
        if(Init.isCorrectArgs(args)) { //if configSystem.json specified

            //read readConfigFile to ConfigSystemParams
            new ConfigSystemParam(args[0]);
            System.out.println(ConfigSystemParam.getString());

            new NodeServer().run();


        } else {
            System.out.println("give config json as param");
        }
    }


}
