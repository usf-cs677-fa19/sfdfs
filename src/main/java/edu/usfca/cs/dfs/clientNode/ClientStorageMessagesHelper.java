package edu.usfca.cs.dfs.clientNode;

import com.google.protobuf.ByteString;
import edu.usfca.cs.dfs.StorageMessages;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientStorageMessagesHelper {

    public static Logger logger = Logger.getLogger(ClientStorageMessagesHelper.class.getName());

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

        logger.log(Level.INFO,"Size of StorageNodeIds : "+ cmMsg.getStorageNodeIdsList().size());
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
        List<String> storageIds = new ArrayList<>();
        for(Map.Entry<String, StorageMessages.StorageNodesHavingChunk> eachChunkId : mapping.entrySet()) {

            for(int i = 0 ; i < eachChunkId.getValue().getStorageNodeList().size(); i++) {
                logger.log(Level.INFO,"Root Cause : "+ eachChunkId.getValue().getStorageNode(i));
                storageIds.add(eachChunkId.getValue().getStorageNode(i));
            }
        }

        for (Map.Entry<String, StorageMessages.StorageNodesHavingChunk> eachMapping : mapping.entrySet()) {
            retrieveChunkMapArray.add(ClientStorageMessagesHelper.prepareRetrieveChunkMap(eachMapping, storageIds));
        }
        return retrieveChunkMapArray;
    }

    private static Map<String, StorageMessages.StorageMessageWrapper> prepareRetrieveChunkMap
            (Map.Entry<String, StorageMessages.StorageNodesHavingChunk> mapping, List<String> storageIds) {

        Map<String,  StorageMessages.StorageMessageWrapper> chunkAskMap = new HashMap<>();
        //value
        StorageMessages.StorageMessageWrapper retrieveChunkMsg = ClientStorageMessagesHelper.prepareRetrieveChunk(mapping.getKey(), storageIds);
        //key(s) - storage node ids
        if(mapping.getValue().getStorageNodeCount() > 0) {
            for(int i = 0; i < mapping.getValue().getStorageNodeCount(); i++ ) {
                String key = mapping.getValue().getStorageNode(i);
                chunkAskMap.put(key, retrieveChunkMsg);
            }
        }
        return chunkAskMap;
    }

    public static StorageMessages.StorageMessageWrapper prepareRetrieveChunk(String fileChunkId, List<String> storageIds){

        StorageMessages.RetrieveChunk retrieveChunk = StorageMessages.RetrieveChunk.newBuilder()
                .setFileChunkId(fileChunkId)
                .addAllStorageNodeIds(storageIds)
                .build();

        StorageMessages.StorageMessageWrapper msgWrapper = StorageMessages.StorageMessageWrapper.newBuilder()
                .setRetrieveChunkMsg(retrieveChunk)
                .build();

        return msgWrapper;
    }



}
