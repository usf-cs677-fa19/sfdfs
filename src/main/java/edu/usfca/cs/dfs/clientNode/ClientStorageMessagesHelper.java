package edu.usfca.cs.dfs.clientNode;

import com.google.protobuf.ByteString;
import edu.usfca.cs.dfs.StorageMessages;

import java.nio.ByteBuffer;
import java.util.*;

public class ClientStorageMessagesHelper {

    public static StorageMessages.StorageMessageWrapper prepareChunkMeta(String fileName, int chunkId, int chunkSize, int totalChunks) {
        StorageMessages.ChunkMeta chunkMetaMsg
                = StorageMessages.ChunkMeta.newBuilder()
                .setFileName(fileName)
                .setChunkId(chunkId)
                .setChunkSize(chunkSize)
                .setTotalChunks(totalChunks)
                .build();

        StorageMessages.StorageMessageWrapper msgWrapper =
                StorageMessages.StorageMessageWrapper.newBuilder()
                        .setChunkMetaMsg(chunkMetaMsg)
                        .build();

        return msgWrapper;
    }

    public static StorageMessages.StorageMessageWrapper prepareStoreChunkMsg(StorageMessages.ChunkMeta cmMsg, ByteBuffer buffer) {

        System.out.println("Size of StorageNodeIds : "+ cmMsg.getStorageNodeIdsList().size());
        StorageMessages.StoreChunk storeChunkMsg
                = StorageMessages.StoreChunk.newBuilder()
                .setFileName(cmMsg.getFileName())
                .setChunkId(cmMsg.getChunkId())
                .setChunkSize(cmMsg.getChunkSize())
                .setTotalChunks(cmMsg.getTotalChunks())
                .addAllStorageNodeIds(cmMsg.getStorageNodeIdsList())
                .setData(ByteString.copyFrom(buffer))
                .setToStorageNodeId(cmMsg.getStorageNodeIdsList().get(0))
                .build();

        StorageMessages.StorageMessageWrapper msgWrapper =
                StorageMessages.StorageMessageWrapper.newBuilder()
                        .setStoreChunkMsg(storeChunkMsg)
                        .build();

        return msgWrapper;
    }

    public static StorageMessages.StorageMessageWrapper prepareRetrieveFileMsg(String fileName){

        StorageMessages.RetrieveFile retrieveFile = StorageMessages.RetrieveFile.newBuilder()
                .setFileName(fileName).build();

        StorageMessages.StorageMessageWrapper msgWrapper = StorageMessages.StorageMessageWrapper.newBuilder()
                .setRetrieveFileMsg(retrieveFile)
                .build();

        return msgWrapper;
    }

    public static ArrayList<Map<String, StorageMessages.StorageMessageWrapper>> prepareRetrieveChunkMapArray(Map<String, StorageMessages.StorageNodesHavingChunk> mapping) {
        ArrayList<Map<String, StorageMessages.StorageMessageWrapper>> retrieveChunkMapArray = new ArrayList<>();

        for (Map.Entry<String, StorageMessages.StorageNodesHavingChunk> eachMapping : mapping.entrySet()) {
            retrieveChunkMapArray.add(ClientStorageMessagesHelper.prepareRetrieveChunkMap(eachMapping));
        }
        return retrieveChunkMapArray;
    }

    private static Map<String, StorageMessages.StorageMessageWrapper> prepareRetrieveChunkMap(Map.Entry<String, StorageMessages.StorageNodesHavingChunk> mapping) {

        Map<String,  StorageMessages.StorageMessageWrapper> chunkAskMap = new HashMap<>();
        //value
        StorageMessages.StorageMessageWrapper retrieveChunkMsg = ClientStorageMessagesHelper.prepareRetrieveChunk(mapping.getKey());
        //key(s) - storage node ids
        if(mapping.getValue().getStorageNodeCount() > 0) {
            for(int i = 0; i < mapping.getValue().getStorageNodeCount(); i++ ) {
                String key = mapping.getValue().getStorageNode(i);
                chunkAskMap.put(key, retrieveChunkMsg);
            }
        }
        return chunkAskMap;
    }

    private static StorageMessages.StorageMessageWrapper prepareRetrieveChunk(String fileChunkId){

        StorageMessages.RetrieveChunk retrieveChunk = StorageMessages.RetrieveChunk.newBuilder()
                .setFileChunkId(fileChunkId)
                .build();

        StorageMessages.StorageMessageWrapper msgWrapper = StorageMessages.StorageMessageWrapper.newBuilder()
                .setRetrieveChunkMsg(retrieveChunk)
                .build();

        return msgWrapper;
    }



}
