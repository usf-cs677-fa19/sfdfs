package edu.usfca.cs.dfs.clientNode;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.data.ChunkMeta;

import java.util.Arrays;

public class ClientStorageMessagesHelper {

    public static StorageMessages.StorageMessageWrapper buildChunkMeta(ChunkMeta m) {
        StorageMessages.ChunkMeta chunkMetaMsg
                = StorageMessages.ChunkMeta.newBuilder()
                .setFileName(m.getFilename())
                .setChunkId(m.getChunkId())
                .setChunkSize(m.getChunkSize())
                .setTotalChunks(m.getTotalChunks())
                .build();

        StorageMessages.StorageMessageWrapper msgWrapper =
                StorageMessages.StorageMessageWrapper.newBuilder()
                        .setChunkMetaMsg(chunkMetaMsg)
                        .build();

        return msgWrapper;
    }

}
