package edu.usfca.cs.dfs.clientNode;

import edu.usfca.cs.dfs.Client;
import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.init.ClientParams;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GetThreadTask implements Runnable {

    private String filePath;

    public GetThreadTask(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        System.out.println("Getting file : "+filePath);
        StorageMessages.StorageMessageWrapper messageWrapper = ClientStorageMessagesHelper.prepareRetrieveFileMsg(filePath);
        try {
            this.runClient(messageWrapper);
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void runClient(StorageMessages.StorageMessageWrapper msgWrapper)
            throws IOException, ExecutionException, InterruptedException {

        new Client().runClient(true,
                ClientParams.getNodeType(),
                ClientParams.getConnectingAddress(),
                ClientParams.getConnectingPort(),
                msgWrapper);
    }
}
