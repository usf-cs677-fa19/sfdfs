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

    private Map<String,Map<String, ChunkFileMeta>> storageNodeMetaData = new HashMap<String, Map<String, ChunkFileMeta>>();


    //Map<NameofDirectoriesHavingChunks, Map<ChunkFileName,ChunkMetadata>
    public Map<String,Map<String, ChunkFileMeta>> getStorageNodeMetaData(){
        return this.storageNodeMetaData;
    }

    public ChunkFileMeta getTheMetadataOfAChunk(String nameOfDirectoriesHavingChunk, String chunkFileName){
        ChunkFileMeta chunkFileMeta = null;
        if (storageNodeMetaData.containsKey(nameOfDirectoriesHavingChunk)) {
            Map<String, ChunkFileMeta> mapOfChunksToChunkMeta = storageNodeMetaData.get(nameOfDirectoriesHavingChunk);

            if (mapOfChunksToChunkMeta.containsKey(chunkFileName)) {
                chunkFileMeta = mapOfChunksToChunkMeta.get(chunkFileName);
            }
        }
        return chunkFileMeta;
    }



}
