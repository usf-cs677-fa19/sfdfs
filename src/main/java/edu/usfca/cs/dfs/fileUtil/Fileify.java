package edu.usfca.cs.dfs.fileUtil;

import com.google.protobuf.ByteString;
import edu.usfca.cs.dfs.StorageMessages;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Fileify {

    public static ByteBuffer readToBuffer(StorageMessages.ChunkMeta cmMsg, int generalChunkSize) throws IOException {

        ByteBuffer directBuf = ByteBuffer.allocateDirect(cmMsg.getChunkSize());
        //int generalChunkSize = //ConfigSystemParams.params[0].getGeneralChunkSize();//1000000; // todo : read from confiig

        try (
            RandomAccessFile reader = new RandomAccessFile(cmMsg.getFileName(), "r");
            FileChannel chan = reader.getChannel();
            ) {
            // Sets the file-pointer offset
            reader.seek((cmMsg.getChunkId()-1) * generalChunkSize);
            // read bytes into directBuf
            int bytesRead = chan.read(directBuf);
            System.out.println("No of bytes read : "+ bytesRead);
            // flipping the byte buffer before it can be read
            directBuf.flip();

            return directBuf;
        }

    }

    public static void copyDirectory(String fromDir, String toDir) {


//
//        String dirPath = "g:/Music/English";
//        File dir = new File(dirPath);
//        File[] files = dir.listFiles();
//        if (files.length == 0) {
//            System.out.println("The directory is empty");
//        } else {
//            for (File aFile : files) {
//                System.out.println(aFile.getName() + " - " + aFile.length());
//            }
//        }
    }







//    public static ChunkMeta getFilledBuffer(String filePath, StorageMessages.ChunkMeta cmMsg) throws IOException {
//        Path path = Paths.get(filePath/*"data/test.xml"*/);
//
//        AsynchronousFileChannel fileChannel =
//                AsynchronousFileChannel.open(path, StandardOpenOption.READ);
//
//    }todo: anurag

    //private static String HOME = System.getProperty("user.home"); todo : use it for storage node to check if file exist


    //behaviour

    //open directory
    //create directory
    public static void createDirectory(String basePath, String dirName) {
//        String filePath = basePath+"/"+dirName;
        Path path = Paths.get(basePath, dirName);

        if(!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void createDirectory(String dirPath) {
        Path path = Paths.get(dirPath);

        if(!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                System.out.println("dir already exists");
            }
        }

    }

    //delete directory
    public static void deleteDirectory(String basePath, String dirName) throws IOException {
        Path path = Paths.get(basePath, dirName);

        Files.deleteIfExists(path);
    }

    //change directory
    //public static void todo : anurag


    // does file exist
    public static boolean doesFileExist(String filePath) {
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
    public static long getFileSize(String filename) throws IOException {
        try(
                RandomAccessFile reader = new RandomAccessFile(filename, "r");
                FileChannel fc = reader.getChannel();
        ) {
            return fc.size();
        }

    }

    //write to a file from start
    public static boolean writeToAFile(String filePath, String content) {
        Path path = Paths.get(filePath);
        try {
            Files.write(path, content.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean writeToAFile(String filePath, byte[] content) {
        Path path = Paths.get(filePath);
        try {
            Files.write(path, content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

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


    public static void copyDirectory(File sourceLocation , File targetLocation){
        if(sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        }else{
            try {
                InputStream in = new FileInputStream(sourceLocation);
                OutputStream out = new FileOutputStream(targetLocation);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();

            } catch (FileNotFoundException e) {
                System.out.println("Error while copying files!!!");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Error while copying files!!!");
                e.printStackTrace();
            }
        }
    }

} //closing class Fileify