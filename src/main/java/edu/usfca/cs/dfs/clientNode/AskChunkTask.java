package edu.usfca.cs.dfs.clientNode;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.data.NodeId;
import edu.usfca.cs.dfs.init.ConfigSystemParam;
import edu.usfca.cs.dfs.net.MessageSender;

public class AskChunkTask implements Runnable {

    StorageMessages.Chunk chunkMsg;
    String nodeType;


    public AskChunkTask(String nodeType, StorageMessages.Chunk chunkMsg) {
        this.chunkMsg = chunkMsg;
        this.nodeType = nodeType;
    }

    @Override
    public void run() {
        StorageMessages.StorageMessageWrapper retrieveChunkWrapper =
                ClientStorageMessagesHelper.prepareRetrieveChunk(chunkMsg.getFileChunkId(), chunkMsg.getStorageNodeIdsList());
        if(chunkMsg.getStorageNodeIdsList().size() > 0) {
            System.out.println("Sending new retrieve message to : "+ chunkMsg.getStorageNodeIdsList().get(0));
            String[] connectingInfo = NodeId.getIPAndPort(chunkMsg.getStorageNodeIds(0));
            try {
                new MessageSender().send(true, nodeType, connectingInfo[0], Integer.parseInt(connectingInfo[1]), retrieveChunkWrapper);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
