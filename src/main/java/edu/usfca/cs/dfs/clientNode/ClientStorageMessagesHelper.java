package edu.usfca.cs.dfs.clientNode;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.data.ChunkMeta;

import java.util.Arrays;

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

}
