package edu.usfca.cs.dfs;

import edu.usfca.cs.dfs.clientNode.GetThreadTask;
import edu.usfca.cs.dfs.clientNode.StoreThreadTask;

import edu.usfca.cs.dfs.controllerNode.ControllerDS;
import edu.usfca.cs.dfs.fileUtil.Fileify;
import edu.usfca.cs.dfs.init.ClientParams;
import edu.usfca.cs.dfs.init.Init;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientNode {

    private static ClientNode clientNode = null;

    private ExecutorService executor;
    private String basePath;

    private ClientNode(){
    }

    public static ClientNode getInstance(){
        if(clientNode == null){
            clientNode =  new ClientNode();
        }
        return clientNode;
    }

    private ClientNode(String fileName) {
        ClientParams.buildClientParams(fileName);
        basePath = System.getProperty("user.home") + "/sfdfs_client" + ClientParams.getNodeId();
        Fileify.createDirectory(basePath);
        executor = Executors.newFixedThreadPool(ClientParams.getNoOfThreads());
    }

    public static ClientNode setInstance(String fileName){
        if(clientNode == null){
            clientNode =  new ClientNode(fileName);
        }
        return clientNode;
    }

    public String getBasePath() {
        return basePath;
    }

    public void store(String filePath) {
        executor.submit(new StoreThreadTask(filePath));
    }

    public void get(String filePath) {
        executor.submit(new GetThreadTask(filePath));
    }


    public static void main(String[] args) {
        if(Init.isCorrectArgs(args)) {
            ClientNode c = ClientNode.setInstance(args[0]);
            for (; ; ) {
                Scanner scanner = new Scanner(System.in);
                if (scanner.hasNextLine()) {
                    String inCmd = scanner.nextLine();
                    String[] inCmdParams = inCmd.split(" ");
                    if(inCmdParams.length == 2) {
                        if(inCmdParams[0].equalsIgnoreCase("store")) { // storing a file
                            c.store(inCmdParams[1]);
                        }
                        else if(inCmdParams[0].equalsIgnoreCase("get")) { // retrieving a file
                            c.get(inCmdParams[1]);
                        }
                    }
                }
            }
        }
    }
}
