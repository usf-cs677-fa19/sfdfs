package edu.usfca.cs.dfs.fileUtil;

import com.google.protobuf.ByteString;
import edu.usfca.cs.dfs.ClientNode;
import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.data.FileChunkId;
import edu.usfca.cs.dfs.init.ClientParams;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Fileify {

    public static ByteBuffer readToBuffer(StorageMessages.ChunkMeta cmMsg, long generalChunkSize) throws IOException {

        ByteBuffer directBuf = ByteBuffer.allocateDirect(cmMsg.getChunkSize());

        try (
            RandomAccessFile reader = new RandomAccessFile(cmMsg.getFileName(), "r");
            FileChannel chan = reader.getChannel();
            ) {
            // Sets the file-pointer offset
            reader.seek(((long)cmMsg.getChunkId()-1) * generalChunkSize);
            // read bytes into directBuf
            int bytesRead = chan.read(directBuf);
            System.out.println("No of bytes read : "+ bytesRead);
            // flipping the byte buffer before it can be read
            directBuf.flip();

            return directBuf;
        }

    }

    public static ByteBuffer readToBuffer(String chunkPath) throws IOException {

        long chunkSize = Fileify.getFileSize(chunkPath);
        ByteBuffer directBuf = ByteBuffer.allocateDirect((int)chunkSize);

        try (
                RandomAccessFile reader = new RandomAccessFile(chunkPath, "r");
                FileChannel chan = reader.getChannel();
        ) {
            // Sets the file-pointer offset
            reader.seek(0);
            // read bytes into directBuf
            int bytesRead = chan.read(directBuf);
            System.out.println("No of bytes read : "+ bytesRead);
            // flipping the byte buffer before it can be read
            directBuf.flip();

            return directBuf;
        }

    }

    public static void writeChunkToFile(StorageMessages.Chunk chunkMsg) {
        String[] filenameAndChunkId = FileChunkId.splitFileAndChunkId(chunkMsg.getFileChunkId());

        String fileName = filenameAndChunkId[0];
        String filePath = ClientNode.getInstance().getBasePath()+"/"+fileName;

        int chunkId = Integer.parseInt(filenameAndChunkId[1]);
        long startingPosition = (long)ClientParams.getGeneralChunkSize() * (chunkId - 1);

        if(Fileify.doesFileExist(filePath)) {
            Fileify.createFileIfDoesNotExist(filePath);
        }


        try (
                RandomAccessFile reader = new RandomAccessFile(filePath, "rw");
                FileChannel chan = reader.getChannel();
        ) {
            reader.seek(startingPosition);
            reader.write(chunkMsg.getData().toByteArray());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void createFileIfDoesNotExist(String filePath) {
        Path path = Paths.get(filePath);

        if(!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // does file exist
    public static boolean doesFileExist(String filePath) {
        Path p = Paths.get(filePath);
        return Files.exists(p);
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
//        Path path = Paths.get(basePath, dirName);
//
//        Files.deleteIfExists(path);

        Path rootPath = Paths.get(basePath, dirName);
        final List<Path> pathsToDelete = Files.walk(rootPath).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        for(Path path : pathsToDelete) {
            Files.deleteIfExists(path);
        }
    }

    public static String[] getListOfDirs(String basePath) {
        File file = new File(basePath);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        System.out.println(Arrays.toString(directories));
        return directories;
    }

    public static String[] getListOfFiles(String dirPath) {
        File file = new File(dirPath);
        String[] files = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isFile();
            }
        });
        System.out.println(Arrays.toString(files));
        return files;
    }

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

    public static void copyDirectory(File sourceLocation , File targetLocation){

        System.out.println("Start of Fileify.copyDiectory: ");

       // File sourceLocation = new File(sourceLocationString);
       // File targetLocation = new File(targetLocationString);
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