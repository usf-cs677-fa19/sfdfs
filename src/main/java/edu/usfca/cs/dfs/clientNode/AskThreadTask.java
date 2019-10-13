package edu.usfca.cs.dfs.clientNode;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.init.ClientParams;
import edu.usfca.cs.dfs.net.Client;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class AskThreadTask implements Runnable {

//    private Map<String,String> askMap;

    public AskThreadTask(Map<String,String> askMap) {
//        this.askMap = askMap;
    }

    @Override
    public void run() {
//        System.out.println("Getting Map : "+this.askMap);
//        //StorageMessages.StorageMessageWrapper messageWrapper = ClientStorageMessagesHelper.prepareRetrieveFileMsg(this.askMap);
//        try {
//            this.runClient(this.askMap);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }


    public void runClient(Map<String,String> askMap)
            throws InterruptedException {

//        new Client().runClient(true,
//                ClientParams.getNodeType(),
//                ClientParams.getConnectingAddress(),
//                ClientParams.getConnectingPort(),
//                msgWrapper);
    }


}
