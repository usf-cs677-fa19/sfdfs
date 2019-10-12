package edu.usfca.cs.dfs.clientNode;

import edu.usfca.cs.dfs.Client;
import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.data.FileChunkId;
import edu.usfca.cs.dfs.data.NodeId;
import edu.usfca.cs.dfs.fileUtil.Fileify;
import edu.usfca.cs.dfs.init.ClientParams;
import edu.usfca.cs.dfs.net.InboundHandler;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.ByteBuffer;

public class  ClientInboundHandler extends InboundHandler {

    @Override
    public void channelRead0(
            ChannelHandlerContext ctx,
            StorageMessages.StorageMessageWrapper msg) {

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

    private void recvChunkMetaMsg( StorageMessages.StorageMessageWrapper msg){
        //check file name, start read position , and chunk size
        StorageMessages.ChunkMeta cmMsg = msg.getChunkMetaMsg();
        // read that much in the buffer
        ByteBuffer buffer = null;
        try {
            buffer = Fileify.readToBuffer(cmMsg, ClientParams.getGeneralChunkSize()); // filling buffer full
        } catch (IOException e) {
            e.printStackTrace();
        }
        // a. connecting info and prepare a storeChunk msg
        String[] connectingInfo = NodeId.getIPAndPort(cmMsg.getStorageNodeIds(0));
        StorageMessages.StorageMessageWrapper storeChunkMsg =
                ClientStorageMessagesHelper.prepareStoreChunkMsg(cmMsg, buffer);
        System.out.println("storage nodes assigned to "
                + FileChunkId.getFileChunkId(cmMsg.getFileName(), cmMsg.getChunkId())
                +" are : "
                + storeChunkMsg.getStoreChunkMsg().getStorageNodeIdsList());
        // b. send to primary storage node
        try {
            new Client().runClient(false, ClientParams.getNodeType(), connectingInfo[0], Integer.parseInt(connectingInfo[1]), storeChunkMsg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}
