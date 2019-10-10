package edu.usfca.cs.dfs.storageNode.data;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ChunkFileMeta {

    private final String fileName;
    private final int chunkId;
    private final int chunkSize;
    private final int totalChunks;
    private final List<String> storageNodeIds;
    private final double entropy;
    private final boolean isCompressed;
    private final long checksum;


    public ChunkFileMeta(String fileName, int chunkId, int chunkSize, int totalChunks, List<String> storageNodeIds, double entropy, boolean isCompressed, long checksum) {
        this.fileName = fileName;
        this.chunkId = chunkId;
        this.chunkSize = chunkSize;
        this.totalChunks = totalChunks;
        this.storageNodeIds = storageNodeIds;
        this.entropy = entropy;
        this.isCompressed = isCompressed;
        this.checksum = checksum;
    }

    public String getFileName() {
        return fileName;
    }

    public int getChunkId() {
        return chunkId;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public int getTotalChunks() {
        return totalChunks;
    }

    public List<String> getStorageNodeIds() {
        return storageNodeIds;
    }

    public double getEntropy() {
        return entropy;
    }

    public boolean isCompressed() {
        return isCompressed;
    }

    public long getChecksum() {
        return checksum;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public ChunkFileMeta fromJson(String chunkFileMetaJson) {
        return new Gson().fromJson(chunkFileMetaJson, ChunkFileMeta.class);
    }


}
