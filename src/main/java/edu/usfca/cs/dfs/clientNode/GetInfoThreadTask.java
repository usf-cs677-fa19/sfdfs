package edu.usfca.cs.dfs.clientNode;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.clientNode.ClientStorageMessagesHelper;
import edu.usfca.cs.dfs.init.ClientParams;
import edu.usfca.cs.dfs.net.MessageSender;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GetInfoThreadTask implements Runnable {

    private String ipAddress;
    private int port;

    public GetInfoThreadTask(String ipAddress,int port){
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("Getting node info : "+ipAddress+":"+port);
        StorageMessages.StorageMessageWrapper messageWrapper = ClientStorageMessagesHelper.prepareGetStorageNodeInfo();
        this.runClient(messageWrapper);

    }

    public void runClient(StorageMessages.StorageMessageWrapper msgWrapper) {
        try {
            ChannelFuture f = new MessageSender().send(true,
                    ClientParams.getNodeType(),
                    ipAddress,
                    port,
                    msgWrapper

            );
//            if(f.await(200)) {
//                System.out.println("Exiting from info");
//            } else {
//
//            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
