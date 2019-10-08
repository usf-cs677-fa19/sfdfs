package edu.usfca.cs.dfs.controllerNode;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.data.ChunkMeta;
import edu.usfca.cs.dfs.net.InboundHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

public class ControllerInboundHandler extends InboundHandler {

    @Override
    public void channelRead0(
            ChannelHandlerContext ctx,
            StorageMessages.StorageMessageWrapper msg) {

        if(msg.hasHeartBeat() ) {  // if message is a heartbeat
            this.recvHeartBeat(msg);

        }else if(msg.hasChunkMetaMsg()){
            /// todo doing
            System.out.println("Recved chunkMetaMsg from client");

            StorageMessages.RetrieveFile retrieveFileMsg
                    = StorageMessages.RetrieveFile.newBuilder()
                    .setFileName(msg.getChunkMetaMsg().getFileName())
                    .build();

            StorageMessages.StorageMessageWrapper msgWrapper =
                    StorageMessages.StorageMessageWrapper.newBuilder()
                            .setRetrieveFileMsg(retrieveFileMsg)
                            .build();

            System.out.println("msgWrapper.hasRetrieveFileMsg(): " + msgWrapper.hasRetrieveFileMsg());


            Channel chan = ctx.channel();
            ChannelFuture future = chan.write(msgWrapper);
            chan.flush();
            //future.addListener(ChannelFutureListener.CLOSE);

            System.out.println("Sent RetrieveFileMsg back to  client");
            // todo doing




//            ctx.channel().writeAndFlush("\n\n RESPONSE FROM CONTROLLER TO CLIENT for Recved chunkMetaMsg !!!!! !!!!! !!!! \n ");
            //this.storeChunkMetadata(msg);
        }else {
            StorageMessages.StoreChunk storeChunkMsg
                    = msg.getStoreChunkMsg();
            System.out.println("Storing file name: "
                    + storeChunkMsg.getFileName());
        }
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
