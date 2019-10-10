package edu.usfca.cs.dfs.controllerNode;

import edu.usfca.cs.dfs.controllerNode.data.StorageNodeDetail;

import java.util.ArrayList;

public class GetFileInfo {

    public static String getChunkNameFromFileName(String filename,int chunkNumber){
        String chunkName = filename+"_Chunk_"+chunkNumber;
        return chunkName;
    }

    public static ArrayList<String> checkBloomFilter(String chunkName){
        ArrayList<String> storageNodes = new ArrayList<>();

        storageNodes = ControllerDS.getInstance().checkBloomFiltersForChunk(chunkName);

        return storageNodes;
    }

}
