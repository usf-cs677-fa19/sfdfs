package edu.usfca.cs.dfs.storageNode;

import edu.usfca.cs.dfs.storageNode.data.ChunkFileMeta;

import java.util.HashMap;
import java.util.Map;



public class StorageNodeDS {

    private static StorageNodeDS storageNodeDS = null;

    private StorageNodeDS(){
    }

    public static StorageNodeDS getInstance(){
        if(storageNodeDS == null){
            storageNodeDS =  new StorageNodeDS();
        }
        return storageNodeDS;
    }

    private String ipAddress;
    private int port;
    private int requestFivePerSecond;
    private int generalChunkSize;
    private String controllerIpAddress;
    private int controllerPort;
    private Map<String, ChunkFileMeta> chunksMetaInfo = new HashMap<>();


    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getRequestFivePerSecond() {
        return requestFivePerSecond;
    }

    public void setRequestFivePerSecond(int requestFivePerSecond) {
        this.requestFivePerSecond = requestFivePerSecond;
    }

    //Map<NameOfDirectoriesHavingChunks, Map<ChunkFileName,ChunkMetadata>
    public Map<String,ChunkFileMeta> getChunksMetaInfo(){
        return this.chunksMetaInfo;
    }

    public ChunkFileMeta getChunkMetaInfo(String chunkFileName){
        ChunkFileMeta chunkFileMeta = null;
            if (chunksMetaInfo.containsKey(chunkFileName)) {
                chunkFileMeta = chunksMetaInfo.get(chunkFileName);
            }
        return chunkFileMeta;
    }


}
