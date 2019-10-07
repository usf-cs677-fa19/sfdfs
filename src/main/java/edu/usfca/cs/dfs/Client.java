package edu.usfca.cs.dfs;

import com.google.protobuf.ByteString;
import edu.usfca.cs.dfs.clientNode.Fileify;
import edu.usfca.cs.dfs.data.ChunkMeta;
import edu.usfca.cs.dfs.net.MessagePipeline;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.nio.channels.FileChannel;

public class Client {

    public Client() {

    }

    public void store(String filePath) {
        try {
            this.storeFileInSfdfs(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void storeFileInSfdfs(String filePath) throws IOException { //
       Fileify ff = new Fileify();
       if (!ff.doesFileExist(filePath)) {
           System.out.println("File does not exist");
        }

       String fileName = filePath;
       long fileSizeInBytes = ff.getFileSize(filePath);
       long chunkSizeInBytes = 256*1024; // todo : read from config
       int totalChunks = this.getTotalChunks(fileSizeInBytes, chunkSizeInBytes);

    }

    public void createAndSendMetaData(String fileName, long fileSizeInBytes, long chunkSizeInBytes, int totalChunks) {
        for(int i=1; i<totalChunks; i++) {
            //int chunkId = i;
            ChunkMeta m = new ChunkMeta()
                    .setFilename(fileName)
                    .setChunkId(i)
                    .setChunkSize((int)(chunkSizeInBytes/1024))
                    .setTotalChunks(totalChunks);

            StorageMessages.chunkMeta chunkMetaMsg
                    = StorageMessages.chunkMeta.newBuilder()
                    .setFileName(m.getFilename())
                    .setChunkId(m.getChunkId())
                    .setChunkSize(m.getChunkSize())
                    .setTotalChunks(m.getTotalChunks())
                    .build();

            StorageMessages.StorageMessageWrapper msgWrapper =
                    StorageMessages.StorageMessageWrapper.newBuilder()
                            .setChunkMetaMsg(chunkMetaMsg)
                            .build();

            // todo: from here


        }
        long lastChunkSize = fileSizeInBytes - chunkSizeInBytes*(totalChunks-1);
        int lastChunkId = totalChunks;
    }


    private int getTotalChunks(long fileSize, long chunkSizeInBytes) {
        return (int)(fileSize/(chunkSizeInBytes))+1;
    }


    public static void main(String[] args)
    throws IOException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        MessagePipeline pipeline = new MessagePipeline("client");

        Bootstrap bootstrap = new Bootstrap()
            .group(workerGroup)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .handler(pipeline);

        ChannelFuture cf = bootstrap.connect("localhost", 7777);
        cf.syncUninterruptibly();

        ByteString data = ByteString.copyFromUtf8("Hello World!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        StorageMessages.StoreChunk storeChunkMsg
            = StorageMessages.StoreChunk.newBuilder()
                .setFileName("my_file.txt")
                .setChunkId(3)
                .setData(data)
                .build();

        StorageMessages.StorageMessageWrapper msgWrapper =
            StorageMessages.StorageMessageWrapper.newBuilder()
                .setStoreChunkMsg(storeChunkMsg)
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
