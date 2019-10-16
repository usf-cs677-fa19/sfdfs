package edu.usfca.cs.dfs.clientNode;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.data.NodeId;
import edu.usfca.cs.dfs.init.ClientParams;
import edu.usfca.cs.dfs.net.MessageSender;
import io.netty.channel.ChannelFuture;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AskChunkThreadTask implements Callable {

    Map<String, StorageMessages.StorageMessageWrapper> retrieveChunkMap;

    public AskChunkThreadTask(Map<String, StorageMessages.StorageMessageWrapper> retrieveChunkMap) {
        this.retrieveChunkMap = retrieveChunkMap;
    }

    @Override
    public ChannelFuture call() {

        for(Map.Entry<String, StorageMessages.StorageMessageWrapper> entry : this.retrieveChunkMap.entrySet()) {
            String[] connectingInfo = NodeId.getIPAndPort(entry.getKey());
            ChannelFuture f = null;
            try {
                f = new MessageSender().send(
                        true,
                        ClientParams.getNodeType(),
                        connectingInfo[0],
                        Integer.parseInt(connectingInfo[1]),
                        entry.getValue());

                f.get(200, TimeUnit.MILLISECONDS);
                break;
            }  catch (TimeoutException e) {
                System.out.println("TIMEOUT, continuing to next if any");
                continue;
            }catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

//            if(!f.isCancelled()) {
////                System.out.println("AskChunkThreadTask : !f.isCancelled() : ");
//                return f;
//            }
        }
        return null;
    }




}
