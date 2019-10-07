package edu.usfca.cs.dfs.data;

public class ChunkMeta {

    private String filename;
    private int chunkId;
    private int checksum;
    private int chunkSize;
    //private int chunkNumber;
    private int totalChunks;

    private String[] storageNodeIds;


    public ChunkMeta(String filename, int totalChunks, int chunkNumber, int chunkSize){
        this.filename = filename;
        this.totalChunks = totalChunks;
        this.chunkId = chunkNumber;
        this.chunkSize = chunkSize;
    }

    public String getFilename() {
        return filename;
    }

    public int getTotalChunks() {
        return totalChunks;
    }

//    public int getChunkNumber() {
//        return chunkNumber;
//    }

    public int getChunkSize() {
        return chunkSize;
    }
}
