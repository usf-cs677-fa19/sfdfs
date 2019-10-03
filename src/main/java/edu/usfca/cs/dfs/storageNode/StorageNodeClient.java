package edu.usfca.cs.dfs.storageNode;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.net.MessagePipeline;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;

public class StorageNodeClient {

    public StorageNodeClient() {

    }

    public void startClient()
            throws IOException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        MessagePipeline pipeline = new MessagePipeline("storage");

        Bootstrap bootstrap = new Bootstrap()
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(pipeline);

        ChannelFuture cf = bootstrap.connect("localhost", 7777);// 7777 is controller
        cf.syncUninterruptibly();

       // ByteString data = ByteString.copyFromUtf8("Hello World!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        StorageMessages.HeartBeat heartBeat = StorageMessages.HeartBeat.newBuilder()
                .setIpAddress("localhost")
                .setPort("8000")
                .setSpaceRemainingMB("1024")  // new File("\").getSpace
                .build();
        /*StorageMessages.StoreChunk storeChunkMsg
                = StorageMessages.StoreChunk.newBuilder()
                .setFileName("my_file.txt")
                .setChunkId(3)
                .setData(data)
                .build();*/


       // new StorageNodeClientHelper().startSendingHeartBeat(cf); // start sending heartbeat
        StorageMessages.StorageMessageWrapper msgWrapper =
                StorageMessages.StorageMessageWrapper.newBuilder()
                        .setHeartBeat(heartBeat)
                        .build();

        Channel chan = cf.channel();
        ChannelFuture write = chan.write(msgWrapper);
        chan.flush();
        write.syncUninterruptibly();

        /* Don't quit until we've disconnected: */
        System.out.println("Shutting down");
        workerGroup.shutdownGracefully();
    }
}


