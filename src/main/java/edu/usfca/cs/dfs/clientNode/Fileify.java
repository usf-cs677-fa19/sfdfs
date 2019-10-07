package edu.usfca.cs.dfs.clientNode;

import com.google.protobuf.ByteString;
import edu.usfca.cs.dfs.StorageMessages;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Fileify {

    //private static String HOME = System.getProperty("user.home"); todo : use it for storage node to check if file exist


    //behaviour
    //open directory
    //create directory
    //delete directory
    //change directory
    // does file exist
    public boolean doesFileExist(String filePath) {
        Path p = Paths.get(filePath);
        return Files.exists(p);
    }
    //create file
    //open filechannel for a file
//    public FileChannel openFileAndGetFileChannel(String filename, String mode) throws IOException {

//        try(
//                RandomAccessFile reader = new RandomAccessFile(filename, mode);
//                FileChannel fc = reader.getChannel();
//                //ByteArrayOutputStream out = new ByteArrayOutputStream();
//        ) {
//            return fc;
//        }
//    }

    //read file

    //get file size
    public long getFileSize(String filename) throws IOException {
        try(
                RandomAccessFile reader = new RandomAccessFile(filename, "r");
                FileChannel fc = reader.getChannel();
        ) {
            return fc.size();
        }

    }
    //write from start
    // write append
    // close fileChannel
    public void closeFileChannel(FileChannel fc) throws IOException {
        fc.close();
    }

    /**
     * readFileInChunks reads a file in chunks of byte array, writes to channel and adds to list of Channel Futures
     * @param filename
     * @param chan
     * @param writes
     * @param chunkSize
     * @return
     * @throws IOException
     */
    public static void transferFileInChunks(String filename, Channel chan, List<ChannelFuture> writes, int chunkSize)
            throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(chunkSize);

        try(
                RandomAccessFile reader = new RandomAccessFile(filename,"r");
                FileChannel fc = reader.getChannel();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            System.out.println("File Transfer started");

            while((fc.read(buf))>0) {

                out.write(buf.array(),0,buf.position());

                //PrefixedMessage msg = new PrefixedMessage(out.toByteArray()); //



//                public StorageMessages.StorageMessageWrapper buildStoreChunk(String address, int port) {
                    StorageMessages.StoreChunk storeChunk = StorageMessages.StoreChunk.newBuilder() // building heartbeat
                            .setFileName("filename")
                            .setChunkId(1)
                            .setData(ByteString.copyFrom(out.toByteArray()))
                            .build();

                    StorageMessages.StorageMessageWrapper msgWrapper =
                            StorageMessages.StorageMessageWrapper.newBuilder()
                                    .setStoreChunkMsg(storeChunk)
                                    .build();

//                    return msgWrapper;
//                }




                //PrefixedMessage msg = new PrefixedMessage(buf.array());

                //System.out.println("Msg this round : \n"+new String(msg.payload()));
                writes.add(chan.write(storeChunk));

                buf.clear();
                out.reset();
                chan.flush();
            }

//            chan.flush();
            for (ChannelFuture write : writes) {
                write.syncUninterruptibly();
            }

        }
        System.out.println("File Transfer completed");

    }







} //closing class Fileify