package edu.usfca.cs.dfs.clientNode;

import com.google.protobuf.ByteString;
import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.net.InboundHandler;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ClientInboundHandler extends InboundHandler {

    @Override
    public void channelRead0(
            ChannelHandlerContext ctx,
            StorageMessages.StorageMessageWrapper msg) {

        System.out.println("IN CLIENT INBOUND HANDLER");
        if(msg.hasChunkMetaMsg()) { // msg returned from controller with storage nodes list

            //this.recvChunkMetaMsg(msg);


            System.out.println("\nChunkMetaMsg received in CLIENT INBOUND HANDLER");
            int size = msg.getChunkMetaMsg().getStorageNodeIdsCount();
            if (size > 0) {
                System.out.println(msg.getChunkMetaMsg().getFileName()+"-"+msg.getChunkMetaMsg().getChunkId());
                for(int i = 0; i < size; i++) {
                    System.out.println(msg.getChunkMetaMsg().getStorageNodeIds(i));
                }
            }

        }


        ctx.close();

    }

    private void recvChunkMetaMsg(StorageMessages.StorageMessageWrapper chunkMetaMsg)  {
        //todo anurag
        //check file name, start read position , and chunk size
        // read that much in the buffer
        StorageMessages.ChunkMeta cmMsg = chunkMetaMsg.getChunkMetaMsg();

        ByteBuffer buffer = null;
        try {
            buffer = Fileify.getFilledBuffer(cmMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // a. prepare a storeChunk msg
        StorageMessages.StorageMessageWrapper storeChunkMsg = this.prepareStoreChunkMsg(cmMsg.getFileName(), cmMsg.getChunkId(), buffer);

        // b. update chunkMeta message by filling checksum field
        // c. send to primary storage node

    }

    private StorageMessages.StorageMessageWrapper prepareStoreChunkMsg(String fileName, int chunkId, ByteBuffer buffer) {
        StorageMessages.StoreChunk storeChunkMsg
                = StorageMessages.StoreChunk.newBuilder()
                .setFileName(fileName)
                .setChunkId(chunkId)
                .setData(ByteString.copyFrom(buffer))
                .build();

        StorageMessages.StorageMessageWrapper msgWrapper =
                StorageMessages.StorageMessageWrapper.newBuilder()
                        .setStoreChunkMsg(storeChunkMsg)
                        .build();

        return msgWrapper;
    }


}
