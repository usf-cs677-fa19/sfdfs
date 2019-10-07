package edu.usfca.cs.dfs.data;

public class ChunkMeta {

    private String filename;
    private int chunkId;
    private int checksum;
    private int chunkSize;
    //private int chunkNumber;
    private int totalChunks;

    private String[] storageNodeIds;

    public ChunkMeta() {

    }

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

    public int getChunkId() {
        return chunkId;
    }
//    public int getChunkNumber() {
//        return chunkNumber;
//    }

    public int getChunkSize() {
        return chunkSize;
    }

    public ChunkMeta setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public ChunkMeta setChunkId(int chunkId) {
        this.chunkId = chunkId;
        return this;
    }

    public ChunkMeta setChecksum(int checksum) {
        this.checksum = checksum;
        return this;
    }

    public ChunkMeta setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
        return this;
    }

    public ChunkMeta setTotalChunks(int totalChunks) {
        this.totalChunks = totalChunks;
        return this;
    }

    public ChunkMeta setStorageNodeIds(String[] storageNodeIds) {
        this.storageNodeIds = storageNodeIds;
        return this;
    }
}
