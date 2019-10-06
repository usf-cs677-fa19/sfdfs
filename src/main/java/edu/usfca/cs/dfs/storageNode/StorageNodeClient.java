package edu.usfca.cs.dfs.storageNode;

import edu.usfca.cs.dfs.NodeClient;
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
import java.util.Timer;


public class StorageNodeClient implements NodeClient {

    private String nodeType;
    private String address;
    private int port;

    StorageClientHelper helper;

    public StorageNodeClient(String nodeType, String address, int port) {
        this.nodeType = nodeType;
        this.address = address;
        this.port = port;

        this.helper = new StorageClientHelper();
        this.keepSendingHeartBeat();


    }

    public void keepSendingHeartBeat() {
        Timer timer = new Timer();
        timer.schedule(
                new HeartBeatSender(this,"localhost",7777,helper.buildHeartBeat(this.address,this.port)),
                0,
                5000);
    }

    public void startClient(String connectingAddress, int connectingPort, StorageMessages.StorageMessageWrapper msgWrapper)
            throws IOException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        MessagePipeline pipeline = new MessagePipeline(this.nodeType); // nodeType can be storage or controller

        Bootstrap bootstrap = new Bootstrap()
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(pipeline);

        ChannelFuture cf = bootstrap.connect(connectingAddress, connectingPort);// connecting info
        cf.syncUninterruptibly();

        // ByteString data = ByteString.copyFromUtf8("Hello World!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

//        StorageMessages.HeartBeat heartBeat = StorageMessages.HeartBeat.newBuilder() // building heartbeat
//                .setIpAddress(address)
//                .setPort(String.valueOf(port))
//                .setSpaceRemainingMB("1024")  // new File("\").getSpace
//                .build();
//        /*StorageMessages.StoreChunk storeChunkMsg
//                = StorageMessages.StoreChunk.newBuilder()
//                .setFileName("my_file.txt")
//                .setChunkId(3)
//                .setData(data)
//                .build();*/
//
//
//        // new StorageNodeClientHelper().startSendingHeartBeat(cf); // start sending heartbeat
//        StorageMessages.StorageMessageWrapper msgWrapper =
//                StorageMessages.StorageMessageWrapper.newBuilder()
//                        .setHeartBeat(heartBeat)
//                        .build();

        Channel chan = cf.channel();
        ChannelFuture write = chan.write(msgWrapper);
        chan.flush();
        write.syncUninterruptibly();

        /* Don't quit until we've disconnected: */
        System.out.println("Shutting down client");
        workerGroup.shutdownGracefully();
    }
}


