package edu.usfca.cs.dfs;

import edu.usfca.cs.dfs.clientNode.ClientStorageMessagesHelper;
import edu.usfca.cs.dfs.fileUtil.Fileify;
import edu.usfca.cs.dfs.init.ClientParams;
import edu.usfca.cs.dfs.init.ConfigReader;
import edu.usfca.cs.dfs.init.Init;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ClientNode {

    public ClientNode(String fileName) {
        ClientParams.buildClientParams(fileName);
    }

    public void store(String filePath) {
        try {
            this.storeFileInSfdfs(filePath);
        } catch (IOException | ExecutionException | InterruptedException e) {
            System.out.println("");
            e.printStackTrace();
        }
    }

    public void get(String filePath) {
        System.out.println("Getting file : "+filePath);
        StorageMessages.StorageMessageWrapper messageWrapper = ClientStorageMessagesHelper.prepareRetrieveFileMsg(filePath);
        try {
            this.runClient(messageWrapper);
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void storeFileInSfdfs(String filePath) throws IOException, ExecutionException, InterruptedException { //
       if (!Fileify.doesFileExist(filePath)) {
           System.out.println("File does not exist on client side");
           return;
        }
        long fileSizeInBytes = Fileify.getFileSize(filePath);

        long chunkSizeInBytes = ClientParams.getGeneralChunkSize();//.1000000; // todo : read from config
        if (fileSizeInBytes < chunkSizeInBytes) {
            chunkSizeInBytes = fileSizeInBytes;
        }

        int totalChunks = (int)(fileSizeInBytes/(chunkSizeInBytes))+1;//this.getTotalChunks(fileSizeInBytes, chunkSizeInBytes);

        System.out.println("Based on chunk size : "+chunkSizeInBytes+" , File : "+filePath+" : will be broken into : "+totalChunks+ " chunks.");

        this.createAndSendMeta(filePath, fileSizeInBytes, chunkSizeInBytes, totalChunks);

    }

    public void createAndSendMeta(String fileName, long fileSizeInBytes, long chunkSizeInBytes, int totalChunks) throws IOException, ExecutionException, InterruptedException {
        for(int i=1; i<totalChunks; i++) {
            this.createAndSendMetaHelper(fileName, i, chunkSizeInBytes, totalChunks);
        }

        long lastChunkSize = fileSizeInBytes - chunkSizeInBytes*(totalChunks-1);
        if(lastChunkSize > 0) {
            int lastChunkId = totalChunks;
            this.createAndSendMetaHelper(fileName, lastChunkId, lastChunkSize, totalChunks);
        }

    }

    // todo: parallize async method
    public void createAndSendMetaHelper(String fileName, int chunkId, long chunkSizeInBytes, int totalChunks)
            throws IOException, ExecutionException, InterruptedException {

        StorageMessages.StorageMessageWrapper msgWrapper =
                ClientStorageMessagesHelper.prepareChunkMeta(fileName, chunkId, (int)(chunkSizeInBytes), totalChunks);

        this.runClient(msgWrapper);
    }

    private int getTotalChunks(long fileSize, long chunkSizeInBytes) {
        return (int)(fileSize/(chunkSizeInBytes))+1;
    }


    public void runClient(StorageMessages.StorageMessageWrapper msgWrapper)
            throws IOException, ExecutionException, InterruptedException {

        new Client().runClient(true,
                ClientParams.getNodeType(),
                ClientParams.getConnectingAddress(),
                ClientParams.getConnectingPort(),
                msgWrapper);
    }


    //// get


//        private StorageMessages.StorageMessageWrapper buildRetrieveFileRequest(String fileName){
//
//        StorageMessages.RetrieveFile retrieveFile = StorageMessages.RetrieveFile.newBuilder()
//                                                .setFileName(fileName).build();
//
//        StorageMessages.StorageMessageWrapper msgWrapper = StorageMessages.StorageMessageWrapper.newBuilder()
//                .setRetrieveFileMsg(retrieveFile)
//                .build();
//
//        return msgWrapper;
//        }

    /// closing get

    public static void main(String[] args) {
        if(Init.isCorrectArgs(args)) {
            ClientNode c = new ClientNode(args[0]);
            for (; ; ) {
                Scanner scanner = new Scanner(System.in);
                if (scanner.hasNextLine()) {
                    String inCmd = scanner.nextLine();
                    String[] inCmdParams = inCmd.split(" ");
                    if(inCmdParams.length == 2) {


                        if(inCmdParams[0].equalsIgnoreCase("store")) { // storing a file
                            c.store(inCmdParams[1]);
                        }
                        else if(inCmdParams[0].equalsIgnoreCase("get")) { // retrieving a file
                            c.get(inCmdParams[1]);
                        }


                    }
                }
            }
        }
    }

}
