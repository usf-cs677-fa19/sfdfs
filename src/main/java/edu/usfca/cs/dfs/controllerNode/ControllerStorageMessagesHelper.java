package edu.usfca.cs.dfs.controllerNode;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.data.ChunkMeta;

import java.util.Arrays;

public class ControllerStorageMessagesHelper {


    public static StorageMessages.StorageMessageWrapper buildChunkMeta(ChunkMeta cm) {
        StorageMessages.ChunkMeta chunkMetaMsg
                = StorageMessages.ChunkMeta.newBuilder()
                .setFileName(cm.getFilename())
                .setChunkId(cm.getChunkId())
                .setChunkSize(cm.getChunkSize())
                .setTotalChunks(cm.getTotalChunks())
                .addAllStorageNodeIds(Arrays.asList(cm.getStorageNodeIds()))
                .build();

        StorageMessages.StorageMessageWrapper msgWrapper =
                StorageMessages.StorageMessageWrapper.newBuilder()
                        .setChunkMetaMsg(chunkMetaMsg)
                        .build();

        return msgWrapper;

    }

}
