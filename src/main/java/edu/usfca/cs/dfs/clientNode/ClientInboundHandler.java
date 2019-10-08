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
        if(msg.hasRetrieveFileMsg()) {
            System.out.println("ChunkMetaMsg receved in CLIENT INBOUND HANDLER");
        }

    }





}
