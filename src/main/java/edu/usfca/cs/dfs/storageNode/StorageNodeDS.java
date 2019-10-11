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

    private Map<String,Map<String, ChunkFileMeta>> chunksMetaInfo = new HashMap<>();


    //Map<NameOfDirectoriesHavingChunks, Map<ChunkFileName,ChunkMetadata>
    public Map<String,Map<String, ChunkFileMeta>> getChunksMetaInfo(){
        return this.chunksMetaInfo;
    }

    public ChunkFileMeta getChunkMetaInfo(String nameOfDirectoriesHavingChunk, String chunkFileName){
        ChunkFileMeta chunkFileMeta = null;
        if (chunksMetaInfo.containsKey(nameOfDirectoriesHavingChunk)) {
            Map<String, ChunkFileMeta> mapOfChunksToChunkMeta = chunksMetaInfo.get(nameOfDirectoriesHavingChunk);

            if (mapOfChunksToChunkMeta.containsKey(chunkFileName)) {
                chunkFileMeta = mapOfChunksToChunkMeta.get(chunkFileName);
            }
        }
        return chunkFileMeta;
    }



}
