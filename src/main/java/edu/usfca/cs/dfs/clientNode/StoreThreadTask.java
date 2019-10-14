package edu.usfca.cs.dfs.clientNode;

import edu.usfca.cs.dfs.net.MessageSender;
import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.fileUtil.Fileify;
import edu.usfca.cs.dfs.init.ClientParams;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class StoreThreadTask implements Runnable {

    private String filePath;

    public StoreThreadTask(String filePath) {
        this.filePath = filePath;
    }


    @Override
    public void run() {
        try {
            this.storeFileInSfdfs(filePath);
        } catch (IOException | ExecutionException | InterruptedException e) {
            System.out.println("");
            e.printStackTrace();
        }
    }

    public void storeFileInSfdfs(String filePath) throws IOException, ExecutionException, InterruptedException { //
        if (!Fileify.doesFileExist(filePath)) {
            System.out.println("File does not exist on client side");
            return;
        }
        long fileSizeInBytes = Fileify.getFileSize(filePath);

        long chunkSizeInBytes = ClientParams.getGeneralChunkSize();
        if (fileSizeInBytes < chunkSizeInBytes) {
            chunkSizeInBytes = fileSizeInBytes;
        }

        int totalChunks = (int)(fileSizeInBytes/(chunkSizeInBytes));//this.getTotalChunks(fileSizeInBytes, chunkSizeInBytes);
        if(!(fileSizeInBytes%chunkSizeInBytes==0)) {
            totalChunks += 1;
        }

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

        new MessageSender().send(true,
                ClientParams.getNodeType(),
                ClientParams.getConnectingAddress(),
                ClientParams.getConnectingPort(),
                msgWrapper);
    }

}
