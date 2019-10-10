package edu.usfca.cs.dfs;

import edu.usfca.cs.dfs.clientNode.ClientStorageMessagesHelper;
import edu.usfca.cs.dfs.fileUtil.Fileify;
import edu.usfca.cs.dfs.data.ChunkMeta;
import edu.usfca.cs.dfs.net.MessagePipeline;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ClientNode {

    private final String nodeType = "client";
    private String connectingIpAddress;
    private int connectingPort;

    public ClientNode(String controllerIpAddress, int port) { // todo : read controller address and port from config file
        this.connectingIpAddress = controllerIpAddress;
        this.connectingPort = port;
    }

    public void store(String filePath) {
        try {
            this.storeFileInSfdfs(filePath);
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void storeFileInSfdfs(String filePath) throws IOException, ExecutionException, InterruptedException { //
       //Fileify ff = new Fileify();
       if (!Fileify.doesFileExist(filePath)) {
           System.out.println("File does not exist");
           return;
        }

       String fileName = filePath;
       long fileSizeInBytes = Fileify.getFileSize(filePath);
       long chunkSizeInBytes = 256*1024; // todo : read from config
       int totalChunks = this.getTotalChunks(fileSizeInBytes, chunkSizeInBytes);

       this.createAndSendMeta(filePath, fileSizeInBytes, chunkSizeInBytes, totalChunks);

    }

    public void createAndSendMeta(String fileName, long fileSizeInBytes, long chunkSizeInBytes, int totalChunks) throws IOException, ExecutionException, InterruptedException {
        for(int i=1; i<totalChunks; i++) {
            this.createAndSendMetaHelper(fileName, i, fileSizeInBytes, chunkSizeInBytes, totalChunks); // todo: parallize
        }

        long lastChunkSize = fileSizeInBytes - chunkSizeInBytes*(totalChunks-1);
        int lastChunkId = totalChunks;
        this.createAndSendMetaHelper(fileName, lastChunkId, fileSizeInBytes, lastChunkSize, totalChunks); // todo: parallize
    }

    // todo: parallize async method
    public void createAndSendMetaHelper(String fileName, int chunkId, long fileSizeInBytes, long chunkSizeInBytes, int totalChunks) throws IOException, ExecutionException, InterruptedException {
        ChunkMeta m = this.createChunkMeta(fileName, chunkId, (int)(chunkSizeInBytes/1024), totalChunks);

        StorageMessages.StorageMessageWrapper msgWrapper = ClientStorageMessagesHelper.buildChunkMeta(m);//this.buildChunkMeta(m);
        this.runClient(msgWrapper);
    }

    private int getTotalChunks(long fileSize, long chunkSizeInBytes) {
        return (int)(fileSize/(chunkSizeInBytes))+1;
    }

    public ChunkMeta createChunkMeta(String fileName, int i, int chunkSizeInBytes, int totalChunks) {
        return new ChunkMeta()
                .setFilename(fileName)
                .setChunkId(i)
                .setChunkSize((int)(chunkSizeInBytes/1024))
                .setTotalChunks(totalChunks);
    }

//    public StorageMessages.StorageMessageWrapper buildChunkMeta( ChunkMeta m) {
//        StorageMessages.ChunkMeta chunkMetaMsg
//                = StorageMessages.ChunkMeta.newBuilder()
//                .setFileName(m.getFilename())
//                .setChunkId(m.getChunkId())
//                .setChunkSize(m.getChunkSize())
//                .setTotalChunks(m.getTotalChunks())
//                .build();
//
//        StorageMessages.StorageMessageWrapper msgWrapper =
//                StorageMessages.StorageMessageWrapper.newBuilder()
//                        .setChunkMetaMsg(chunkMetaMsg)
//                        .build();
//
//        return msgWrapper;
//    }

    public void runClient(StorageMessages.StorageMessageWrapper msgWrapper)
            throws IOException, ExecutionException, InterruptedException {

        new Client().runClient(true, nodeType, this.connectingIpAddress, this.connectingPort, msgWrapper);
//
//
//
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        MessagePipeline pipeline = new MessagePipeline("client"); // client pipeline
//
//        Bootstrap bootstrap = new Bootstrap()
//                .group(workerGroup)
//                .channel(NioSocketChannel.class)
//                .option(ChannelOption.SO_KEEPALIVE, true)
//                .handler(pipeline);
//
//        ChannelFuture cf = bootstrap.connect(this.connectingIpAddress, this.connectingPort);// connecting info
//        cf.syncUninterruptibly();
//
//
//       //bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
//        Channel chan = cf.channel();
//        ChannelFuture write = chan.write(msgWrapper);
//        //write.addListener(ChannelFutureListener.CLOSE);
//        chan.flush();
//        chan.closeFuture().sync();
//
//        write.syncUninterruptibly();
//
//        /* Don't quit until we've disconnected: */
////        System.out.println("Shutting down client"); //todo add closing mechanism
////        workerGroup.shutdownGracefully();
//        //this.shutDownEventLoopGroup(workerGroup);
    }

//    public void shutDownEventLoopGroup(EventLoopGroup workerGroup) {
//        System.out.println("Shutting down client"); //todo add closing mechanism
//        workerGroup.shutdownGracefully();
//    }


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ClientNode c = new ClientNode("localhost", 7777);

        for (; ; ) {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextLine()) {
                if(scanner.nextLine().equalsIgnoreCase("ok")) {

                    ChunkMeta m = new ChunkMeta()
                            .setFilename("fileName")
                            .setChunkId(5)
                            .setChunkSize((int)(102))
                            .setTotalChunks(7);
//
//                    StorageMessages.ChunkMeta chunkMetaMsg
//                            = StorageMessages.ChunkMeta.newBuilder()
//                            .setFileName(m.getFilename())
//                            .setChunkId(m.getChunkId())
//                            .setChunkSize(m.getChunkSize())
//                            .setTotalChunks(m.getTotalChunks())
//                            .build();

                    StorageMessages.StorageMessageWrapper msgWrapper = ClientStorageMessagesHelper.buildChunkMeta(m);
//                            StorageMessages.StorageMessageWrapper.newBuilder()
//                                    .setChunkMetaMsg(chunkMetaMsg)
//                                    .build();

                    c.runClient(msgWrapper);

                }
            }
        }

    }

//    public static void main(String[] args)
//    throws IOException {
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        MessagePipeline pipeline = new MessagePipeline("client");
//
//        Bootstrap bootstrap = new Bootstrap()
//            .group(workerGroup)
//            .channel(NioSocketChannel.class)
//            .option(ChannelOption.SO_KEEPALIVE, true)
//            .handler(pipeline);
//
//        ChannelFuture cf = bootstrap.connect("localhost", 7777);
//        cf.syncUninterruptibly();
//
//        ByteString data = ByteString.copyFromUtf8("Hello World!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        StorageMessages.StoreChunk storeChunkMsg
//            = StorageMessages.StoreChunk.newBuilder()
//                .setFileName("my_file.txt")
//                .setChunkId(3)
//                .setData(data)
//                .build();
//
//        StorageMessages.StorageMessageWrapper msgWrapper =
//            StorageMessages.StorageMessageWrapper.newBuilder()
//                .setStoreChunkMsg(storeChunkMsg)
//                .build();
//
//        Channel chan = cf.channel();
//        ChannelFuture write = chan.write(msgWrapper);
//        chan.flush();
//
//
//        write.syncUninterruptibly();
//
//        /* Don't quit until we've disconnected: */
//        System.out.println("Shutting down");
//        workerGroup.shutdownGracefully();
//    }
}
