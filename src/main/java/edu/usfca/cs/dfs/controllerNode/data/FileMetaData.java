package edu.usfca.cs.dfs.controllerNode.data;

import edu.usfca.cs.dfs.data.FileChunkId;

public class FileMetaData {

    private String fileName;
    private int chunkId;
    private int totalChunks;

    public FileMetaData(String fileName,int chunkId,int totalChunks){
        this.fileName = fileName;
        this.chunkId = chunkId;
        this.totalChunks = totalChunks;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getChunkId() {
        return chunkId;
    }

    public void setChunkId(int chunkId) {
        this.chunkId = chunkId;
    }

    public int getTotalChunks() {
        return totalChunks;
    }

    public void setTotalChunks(int totalChunks) {
        this.totalChunks = totalChunks;
    }
}
