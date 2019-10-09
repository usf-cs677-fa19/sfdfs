package edu.usfca.cs.dfs;

import edu.usfca.cs.dfs.init.ConfigSystemParam;
import edu.usfca.cs.dfs.init.ConfigSystemParams;
import edu.usfca.cs.dfs.init.Init;

import java.io.IOException;

public class StartSystem {

    public static void main(String[] args) throws IOException {

        if(Init.isCorrectArgs(args)) { //if configSystem.json specified
            ConfigSystemParams configSystemParams = new ConfigSystemParams(args[0]);
            System.out.println(configSystemParams.toString());

            for(ConfigSystemParam nodeParam :configSystemParams.getParams()) { //for each ConfigSystemParam
                String nodeType = nodeParam.getNodeType();
                String address = nodeParam.getAddress();
                int port = nodeParam.getPort();

                if (!nodeType.equals("") && !address.equals("") && port!=0) { //if ConfigSystemParam pass check
                    //new NodeServer().run(nodeParam);
                    new NodeServer(nodeParam).run();
                }
            }
        }
    }
}
