package edu.usfca.cs.dfs.controllerNode;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.data.ChunkMetaPOJO;

import java.util.Arrays;

public class ControllerStorageMessagesHelper {


    public static StorageMessages.StorageMessageWrapper buildChunkMeta(ChunkMetaPOJO cm) {
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

    //
    public static StorageMessages.StorageMessageWrapper buildretrieveChunkMeta(String chunkFileId){
            StorageMessages.RetrieveChunkMeta retrieveChunkMeta = StorageMessages.RetrieveChunkMeta.newBuilder()
                    .setFileChunkId(chunkFileId)
                    .build();

            StorageMessages.StorageMessageWrapper msgWrapper =
                    StorageMessages.StorageMessageWrapper.newBuilder()
                            .setRetrieveChunkMeta(retrieveChunkMeta)
                            .build();

            return msgWrapper;
    }

}
