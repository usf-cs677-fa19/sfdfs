package edu.usfca.cs.dfs.net;

import edu.usfca.cs.dfs.StorageMessages;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

@ChannelHandler.Sharable
public class InboundHandler
extends SimpleChannelInboundHandler<StorageMessages.StorageMessageWrapper> {

    public Logger logger =  Logger.getLogger(InboundHandler.class.getName());
    public InboundHandler() { }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        /* A connection has been established */
        InetSocketAddress addr
            = (InetSocketAddress) ctx.channel().remoteAddress();
        logger.log(Level.INFO,"Connection established: " + addr);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        /* A channel has been disconnected */
        InetSocketAddress addr
            = (InetSocketAddress) ctx.channel().remoteAddress();
        logger.log(Level.INFO,"Connection lost: " + addr);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx)
    throws Exception {
        /* Writable status of the channel changed */
    }

    @Override
    public void channelRead0(
            ChannelHandlerContext ctx,
            StorageMessages.StorageMessageWrapper msg) {

//        if(msg.hasHeartBeat() ) {
//            System.out.println("heartbeat from");
//        } else {
        logger.log(Level.INFO,"IN CLIENT INBOUND HANDLER");
        if(msg.hasRetrieveFileMsg()) {
            logger.log(Level.INFO,"ChunkMetaMsg receved in CLIENT INBOUND HANDLER");
        } else {
            StorageMessages.StoreChunk storeChunkMsg
                    = msg.getStoreChunkMsg();
            logger.log(Level.INFO,"Storing file name: "
                    + storeChunkMsg.getFileName());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
}
