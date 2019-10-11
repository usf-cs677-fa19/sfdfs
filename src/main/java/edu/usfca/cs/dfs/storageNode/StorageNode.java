package edu.usfca.cs.dfs.storageNode;

import edu.usfca.cs.dfs.Client;
import edu.usfca.cs.dfs.data.NodeId;
import edu.usfca.cs.dfs.fileUtil.Fileify;
import edu.usfca.cs.dfs.nodes.NodeClient;
import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.nodes.SfdfsNode;

import java.io.IOException;
import java.util.Timer;

import static java.lang.System.exit;


public class StorageNode implements SfdfsNode {

    private String nodeType;
    private String address;
    private int port;
    public String nodeId;

    //private StorageStorageMessagesHelper helper;

    public StorageNode(String nodeType, String address, int port) {
        this.nodeType = nodeType;
        this.address = address;
        this.port = port;
        this.nodeId = NodeId.getId(this.address, this.port);

        //this.helper = new StorageStorageMessagesHelper();

        this.createSfdfsDirs(); // creating initial directory structure

        this.keepSendingHeartBeat();
    }

    private void createSfdfsDirs() {
        Fileify.createDirectory("/users/manalipatil/", "sfdfs_"+nodeId);
    }

    public void keepSendingHeartBeat() {
        Timer timer = new Timer();
        timer.schedule(
                new HeartBeatSender(
                        //this,
                        "localhost",
                        7777,
                        StorageStorageMessagesHelper.buildHeartBeat(this.address,this.port)),
                0,
                5000);
    }

    public void startClient(String connectingAddress, int connectingPort, StorageMessages.StorageMessageWrapper msgWrapper)
            throws InterruptedException {

        new Client().runClient(false, this.nodeType, connectingAddress, connectingPort, msgWrapper);
//
//
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        MessagePipeline pipeline = new MessagePipeline(this.nodeType); // nodeType can be storage or controller
//
//        Bootstrap bootstrap = new Bootstrap()
//                .group(workerGroup)
//                .channel(NioSocketChannel.class)
//                .option(ChannelOption.SO_KEEPALIVE, true)
//                .handler(pipeline);
//
//        ChannelFuture cf = bootstrap.connect(connectingAddress, connectingPort);// connecting info
//        cf.syncUninterruptibly();
//
//        // ByteString data = ByteString.copyFromUtf8("Hello World!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//
////        StorageMessages.HeartBeat heartBeat = StorageMessages.HeartBeat.newBuilder() // building heartbeat
////                .setIpAddress(address)
////                .setPort(String.valueOf(port))
////                .setSpaceRemainingMB("1024")  // new File("\").getSpace
////                .build();
////        /*StorageMessages.StoreChunk storeChunkMsg
////                = StorageMessages.StoreChunk.newBuilder()
////                .setFileName("my_file.txt")
////                .setChunkId(3)
////                .setData(data)
////                .build();*/
////
////
////        // new StorageNodeClientHelper().startSendingHeartBeat(cf); // start sending heartbeat
////        StorageMessages.StorageMessageWrapper msgWrapper =
////                StorageMessages.StorageMessageWrapper.newBuilder()
////                        .setHeartBeat(heartBeat)
////                        .build();
//
//        Channel chan = cf.channel();
//        ChannelFuture write = chan.write(msgWrapper);
//        chan.flush();
//        write.syncUninterruptibly();
//
//        /* Don't quit until we've disconnected: */
//        System.out.println("Shutting down client");
//        workerGroup.shutdownGracefully();
    }



}


