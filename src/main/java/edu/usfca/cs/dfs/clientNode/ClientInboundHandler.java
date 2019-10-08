package edu.usfca.cs.dfs.clientNode;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.net.InboundHandler;
import io.netty.channel.ChannelHandlerContext;

public class ClientInboundHandler extends InboundHandler {

    @Override
    public void channelRead0(
            ChannelHandlerContext ctx,
            StorageMessages.StorageMessageWrapper msg) {

        System.out.println("IN CLIENT INBOUND HANDLER");
        if(msg.hasChunkMetaMsg()) { // msg returned from controller with storage nodes list
            this.recvChunkMetaMsg(msg);
            System.out.println("\nChunkMetaMsg receved in CLIENT INBOUND HANDLER");
            System.out.println(msg.getChunkMetaMsg().getFileName()+
                    "-"+msg.getChunkMetaMsg().getChunkId()+
                    " => "+msg.getChunkMetaMsg().getStorageNodeIds(0)+
                    ", "+msg.getChunkMetaMsg().getStorageNodeIds(1)+
                    ", "+msg.getChunkMetaMsg().getStorageNodeIds(2));
        }
        else if(msg.hasRetrieveFileMsg()) {
            System.out.println("RetrieveFileMsg receved in CLIENT INBOUND HANDLER");

        }

    }

    private void recvChunkMetaMsg(StorageMessages.StorageMessageWrapper msg) {
        //todo anurag
        //check file name, start read position , and chunk size
        // read that much in the buffer
        // a. prepare a storeChunk msg
        // b. update chunkMeta message by filling checksum field
        // c. send to primary storage node

    }


}
