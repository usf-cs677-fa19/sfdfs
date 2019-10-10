package edu.usfca.cs.dfs.data;

public class ChunkMetaPOJO {

    private String filename;
    private int chunkId;
    private int chunkSize;
    private int totalChunks;
    private String[] storageNodeIds;

    public ChunkMetaPOJO() {

    }

    public ChunkMetaPOJO(String filename, int totalChunks, int chunkNumber, int chunkSize){
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

    public String[] getStorageNodeIds() {
        return storageNodeIds;
    }

    public ChunkMetaPOJO setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public ChunkMetaPOJO setChunkId(int chunkId) {
        this.chunkId = chunkId;
        return this;
    }

    public ChunkMetaPOJO setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
        return this;
    }

    public ChunkMetaPOJO setTotalChunks(int totalChunks) {
        this.totalChunks = totalChunks;
        return this;
    }

    public ChunkMetaPOJO setStorageNodeIds(String[] storageNodeIds) {
        this.storageNodeIds = storageNodeIds;
        return this;
    }
}
