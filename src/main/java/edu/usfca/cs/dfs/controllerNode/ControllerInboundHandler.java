package edu.usfca.cs.dfs.controllerNode;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.data.ChunkMeta;
import edu.usfca.cs.dfs.net.InboundHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Arrays;

public class ControllerInboundHandler extends InboundHandler {

    @Override
    public void channelRead0(
            ChannelHandlerContext ctx,
            StorageMessages.StorageMessageWrapper msg) {

        if(msg.hasHeartBeat()) {  // if message is a heartbeat
            this.recvHeartBeat(msg);

        }else if(msg.hasChunkMetaMsg()){
            ///
            System.out.println("Recved chunkMetaMsg from client");

            StorageMessages.ChunkMeta receivedChunkMetaMsg = msg.getChunkMetaMsg();

            ChunkMeta cm = new ChunkMeta()
                    .setFilename(receivedChunkMetaMsg.getFileName())
                    .setChunkId(receivedChunkMetaMsg.getChunkId())
                    .setChunkSize(receivedChunkMetaMsg.getChunkSize())
                    .setTotalChunks(receivedChunkMetaMsg.getTotalChunks());

            String[] storageNodesAssigned = this.getStorageNodesForChunkMeta(cm);
            cm.setStorageNodeIds(storageNodesAssigned);

            StorageMessages.StorageMessageWrapper msgWrapper = this.buildChunkMeta(cm);

            Channel chan = ctx.channel();
            ChannelFuture future = chan.write(msgWrapper);
            chan.flush();
//            //future.addListener(ChannelFutureListener.CLOSE);
//
            System.out.println("Sent RetrieveFileMsg back to  client");
            //


            //this.getStorageNodesForChunkMeta(msg); todo manali
        }else {
            StorageMessages.StoreChunk storeChunkMsg
                    = msg.getStoreChunkMsg();
            System.out.println("Storing file name: "
                    + storeChunkMsg.getFileName());
        }
    }

    private StorageMessages.StorageMessageWrapper buildChunkMeta(ChunkMeta cm) {
        StorageMessages.ChunkMeta chunkMetaMsg
                = StorageMessages.ChunkMeta.newBuilder()
                .setFileName(cm.getFilename())
                .setChunkId(cm.getChunkId())
                .setChunkSize(cm.getChunkSize())
                .setTotalChunks(cm.getTotalChunks())
                .addAllStorageNodeIds(Arrays.asList(cm.getStorageNodeIds()))
                .build();

            StorageMessages.StorageMessageWrapper msgWrapper =
                    StorageMessages.StorageMessageWrapper.newBuilder()
                            .setChunkMetaMsg(chunkMetaMsg)
                            .build();

            return msgWrapper;

    }

    private String[] getStorageNodesForChunkMeta(ChunkMeta cm) { //todo manali
        String[] arr =  new String[3];
        arr[0] = "localhost:8000";
        arr[1] = "localhost:8001";
        arr[2] = "localhost:8002";
        return arr;
    }


    private void recvHeartBeat(StorageMessages.StorageMessageWrapper msg) {
        ControllerNodeHelper.getControllerNodeHelper().recvHeartBeat(msg);
      //  new ControllerNodeHelper().recvHeartBeat(msg);
//        System.out.println("heartbeat from: "+msg.getHeartBeat().getIpAddress()+":"+msg.getHeartBeat().getPort());
//
//        controllerDS.updateStorageNodeRegister(new StorageNodeDetail(
//                msg.getHeartBeat().getIpAddress(),
//                msg.getHeartBeat().getPort(),
//                msg.getHeartBeat().getSpaceRemainingMB(),
//                Instant.now()
//        ));
//
//        System.out.println("StorageNodeDetailList size: "+controllerDS.getStorageNodeRegister().size());
    }

    private void storeChunkMetadata(StorageMessages.StorageMessageWrapper msg){

        ControllerNodeHelper.getControllerNodeHelper().storeChunkMetadata(
                new ChunkMeta(msg.getChunkMetaMsg().getFileName(),
                        msg.getChunkMetaMsg().getTotalChunks(),
                        msg.getChunkMetaMsg().getChunkId(),
                        msg.getChunkMetaMsg().getChunkSize())
        );
    }
}
