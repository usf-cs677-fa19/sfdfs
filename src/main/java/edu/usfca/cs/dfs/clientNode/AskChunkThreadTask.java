package edu.usfca.cs.dfs.clientNode;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.data.NodeId;
import edu.usfca.cs.dfs.init.ClientParams;
import edu.usfca.cs.dfs.net.MessageSender;
import io.netty.channel.ChannelFuture;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class AskChunkThreadTask implements Callable {

    Map<String, StorageMessages.StorageMessageWrapper> retrieveChunkMap;

    public AskChunkThreadTask(Map<String, StorageMessages.StorageMessageWrapper> retrieveChunkMap) {
        this.retrieveChunkMap = retrieveChunkMap;
    }

    @Override
    public ChannelFuture call() throws Exception {

        for(Map.Entry<String, StorageMessages.StorageMessageWrapper> entry : this.retrieveChunkMap.entrySet()) {
            String[] connectingInfo = NodeId.getIPAndPort(entry.getKey());
            ChannelFuture f = new MessageSender().send(
                    true,
                    ClientParams.getNodeType(),
                    connectingInfo[0],
                    Integer.parseInt(connectingInfo[1]),
                    entry.getValue());

            f.get(200, TimeUnit.MILLISECONDS);
            if(!f.isCancelled()) {
                return f;
            }
        }
        return null;
    }




}
