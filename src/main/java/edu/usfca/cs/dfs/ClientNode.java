package edu.usfca.cs.dfs;

import edu.usfca.cs.dfs.clientNode.ClientStorageMessagesHelper;
import edu.usfca.cs.dfs.fileUtil.Fileify;

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
            System.out.println("");
            e.printStackTrace();
        }
    }

    public void storeFileInSfdfs(String filePath) throws IOException, ExecutionException, InterruptedException { //
       if (!Fileify.doesFileExist(filePath)) {
           System.out.println("File does not exist");
           return;
        }

        String fileName = filePath;
        long fileSizeInBytes = Fileify.getFileSize(filePath);

        long chunkSizeInBytes = 10*1024*1024; // todo : read from config
        if (fileSizeInBytes < chunkSizeInBytes) {
            chunkSizeInBytes = fileSizeInBytes;
        }

        int totalChunks = this.getTotalChunks(fileSizeInBytes, chunkSizeInBytes);
        System.out.println("Based on chunk size : "+chunkSizeInBytes/1024+" , File : "+fileName+" : will be broken into : "+totalChunks+ " chunks.");

        this.createAndSendMeta(filePath, fileSizeInBytes, chunkSizeInBytes, totalChunks);

    }

    /**
     * createAndSendMeta func
     * @param fileName
     * @param fileSizeInBytes
     * @param chunkSizeInBytes
     * @param totalChunks
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */

    public void createAndSendMeta(String fileName, long fileSizeInBytes, long chunkSizeInBytes, int totalChunks) throws IOException, ExecutionException, InterruptedException {
        for(int i=1; i<totalChunks; i++) {
            this.createAndSendMetaHelper(fileName, i, chunkSizeInBytes, totalChunks);
        }

        long lastChunkSize = fileSizeInBytes - chunkSizeInBytes*(totalChunks-1);
        int lastChunkId = totalChunks;
        this.createAndSendMetaHelper(fileName, lastChunkId, lastChunkSize, totalChunks);
    }

    // todo: parallize async method
    public void createAndSendMetaHelper(String fileName, int chunkId, long chunkSizeInBytes, int totalChunks) throws IOException, ExecutionException, InterruptedException {
        //ChunkMeta m = this.createChunkMeta(fileName, chunkId, (int)(chunkSizeInBytes/1024), totalChunks);

        StorageMessages.StorageMessageWrapper msgWrapper = ClientStorageMessagesHelper.buildChunkMeta(
                fileName,
                chunkId,
                (int)(chunkSizeInBytes/1024),
                totalChunks);
        this.runClient(msgWrapper);
    }

    private int getTotalChunks(long fileSize, long chunkSizeInBytes) {
        return (int)(fileSize/(chunkSizeInBytes))+1;
    }

//    public ChunkMeta createChunkMeta(String fileName, int i, int chunkSizeInBytes, int totalChunks) {
//        return new ChunkMeta()
//                .setFilename(fileName)
//                .setChunkId(i)
//                .setChunkSize((int)(chunkSizeInBytes/1024))
//                .setTotalChunks(totalChunks);
//    }


    public void runClient(StorageMessages.StorageMessageWrapper msgWrapper)
            throws IOException, ExecutionException, InterruptedException {

        new Client().runClient(true, nodeType, this.connectingIpAddress, this.connectingPort, msgWrapper);
    }


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ClientNode c = new ClientNode("localhost", 7777);

        for (; ; ) {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextLine()) {
                String inCmd = scanner.nextLine();
                String[] inCmdParams = inCmd.split(" ");
                if(inCmdParams.length == 2) {
                    if(inCmdParams[0].equalsIgnoreCase("store")) {
                        c.store(inCmdParams[1]);
                    }
                }
//                if(scanner.nextLine().equalsIgnoreCase("ok")) {
//
//
//                    StorageMessages.StorageMessageWrapper msgWrapper =
//                            ClientStorageMessagesHelper.buildChunkMeta("fileName", 5, 102, 7);
//
//                    c.runClient(msgWrapper);
//
//                }
            }
        }

    }

}
