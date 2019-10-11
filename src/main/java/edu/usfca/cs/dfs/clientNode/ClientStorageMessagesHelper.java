package edu.usfca.cs.dfs.clientNode;

import com.google.protobuf.ByteString;
import edu.usfca.cs.dfs.StorageMessages;

import java.nio.ByteBuffer;

public class ClientStorageMessagesHelper {

    public static StorageMessages.StorageMessageWrapper buildChunkMeta(String fileName, int chunkId, int chunkSize, int totalChunks) {
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

}
