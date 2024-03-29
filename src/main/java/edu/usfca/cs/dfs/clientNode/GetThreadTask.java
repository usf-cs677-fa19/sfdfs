package edu.usfca.cs.dfs.clientNode;

import edu.usfca.cs.dfs.net.MessageSender;
import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.init.ClientParams;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void runClient(StorageMessages.StorageMessageWrapper msgWrapper)
            throws InterruptedException {

        ChannelFuture f = new MessageSender().send(true,
                ClientParams.getNodeType(),
                ClientParams.getConnectingAddress(),
                ClientParams.getConnectingPort(),
                msgWrapper);
    }
}
