package edu.usfca.cs.dfs.clientNode;

import com.google.protobuf.ByteString;
import edu.usfca.cs.dfs.Client;
import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.data.NodeId;
import edu.usfca.cs.dfs.fileUtil.Fileify;
import edu.usfca.cs.dfs.net.InboundHandler;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ClientInboundHandler extends InboundHandler {

    @Override
    public void channelRead0(
            ChannelHandlerContext ctx,
            StorageMessages.StorageMessageWrapper msg) {
        //copy = msg. todo create a copy and close context here
        System.out.println("IN CLIENT INBOUND HANDLER");
        if(msg.hasChunkMetaMsg()) { // msg returned from controller with storage nodes list
            System.out.println("\nChunkMetaMsg received in CLIENT INBOUND HANDLER");
            int size = msg.getChunkMetaMsg().getStorageNodeIdsCount();
            if (size > 0) {
                System.out.println(msg.getChunkMetaMsg().getFileName()+"-"+msg.getChunkMetaMsg().getChunkId());
                for(int i = 0; i < size; i++) {
                    System.out.println(msg.getChunkMetaMsg().getStorageNodeIds(i));
                }

            } else {
                System.out.println(msg.getChunkMetaMsg().getFileName()+"-"+"NO STORAGE LIST");
            }

            this.recvChunkMetaMsg(msg);
        }

        ctx.close();  //todo from here to up
    }

    private void recvChunkMetaMsg(StorageMessages.StorageMessageWrapper chunkMetaMsg){
        //todo anurag
        //check file name, start read position , and chunk size
        // read that much in the buffer
        StorageMessages.ChunkMeta cmMsg = chunkMetaMsg.getChunkMetaMsg();

        ByteBuffer buffer = null;
        try {
            buffer = Fileify.readToBuffer(cmMsg); // filling buffer full
        } catch (IOException e) {
            e.printStackTrace();
        }

        // a. connecting info and prepare a storeChunk msg
        String[] connectingId = NodeId.getIPAndPort(cmMsg.getStorageNodeIds(0));
        StorageMessages.StorageMessageWrapper storeChunkMsg = this.prepareStoreChunkMsg(cmMsg, buffer);
        // b. send to primary storage node
        try {
            new Client().runClient(false, "client", connectingId[0], Integer.parseInt(connectingId[1]), storeChunkMsg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private StorageMessages.StorageMessageWrapper prepareStoreChunkMsg(StorageMessages.ChunkMeta cmMsg, ByteBuffer buffer) {
        StorageMessages.StoreChunk storeChunkMsg
                = StorageMessages.StoreChunk.newBuilder()
                .setFileName(cmMsg.getFileName())
                .setChunkId(cmMsg.getChunkId())
                .setChunkSize(cmMsg.getChunkSize())
                .setTotalChunks(cmMsg.getTotalChunks())
                .setStorageNodeIds(0, cmMsg.getStorageNodeIds(0))
                .setStorageNodeIds(1, cmMsg.getStorageNodeIds(1))
                .setStorageNodeIds(2, cmMsg.getStorageNodeIds(2))
                .setData(ByteString.copyFrom(buffer))
                .build();

        StorageMessages.StorageMessageWrapper msgWrapper =
                StorageMessages.StorageMessageWrapper.newBuilder()
                        .setStoreChunkMsg(storeChunkMsg)
                        .build();

        return msgWrapper;
    }


}
