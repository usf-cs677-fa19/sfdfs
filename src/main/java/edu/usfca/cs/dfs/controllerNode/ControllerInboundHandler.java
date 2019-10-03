package edu.usfca.cs.dfs.controllerNode;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.net.InboundHandler;
import io.netty.channel.ChannelHandlerContext;

public class ControllerInboundHandler extends InboundHandler {

    @Override
    public void channelRead0(
            ChannelHandlerContext ctx,
            StorageMessages.StorageMessageWrapper msg) {

        if(msg.hasHeartBeat() ) {
            System.out.println("heartbeat from");
        } else {
            StorageMessages.StoreChunk storeChunkMsg
                    = msg.getStoreChunkMsg();
            System.out.println("Storing file name: "
                    + storeChunkMsg.getFileName());
        }
    }



}
