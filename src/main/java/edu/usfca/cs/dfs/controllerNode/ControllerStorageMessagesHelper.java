package edu.usfca.cs.dfs.controllerNode;

import com.google.protobuf.Descriptors;
import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.data.ChunkMetaPOJO;
import edu.usfca.cs.dfs.storageNode.StorageStorageMessagesHelper;

import java.lang.reflect.Array;
import java.util.*;

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


    public static StorageMessages.StorageMessageWrapper buildMappingChunkIdToStorageNodes(HashMap<String, ArrayList<String>> mapping){

        StorageMessages.MappingChunkIdToStorageNodes.Builder mappingMsgBuilder = StorageMessages.MappingChunkIdToStorageNodes.newBuilder();

        for(Map.Entry<String, ArrayList<String>> chunkMapping : mapping.entrySet()) {
            StorageMessages.StorageNodesHavingChunk storageNodesHavingChunkMsg = StorageMessages.StorageNodesHavingChunk.newBuilder()
                    .addAllStorageNode(chunkMapping.getValue())
                    .build();
            mappingMsgBuilder.putMaping(chunkMapping.getKey(), storageNodesHavingChunkMsg);
        }
        StorageMessages.MappingChunkIdToStorageNodes mappingMsg = mappingMsgBuilder.build();

        StorageMessages.StorageMessageWrapper msgWrapper =
                StorageMessages.StorageMessageWrapper.newBuilder()
                        .setMapingChunkIdToStorageNodes(mappingMsg)
                        .build();

        return msgWrapper;
    }


    public static StorageMessages.StorageMessageWrapper buildBecomePrimary(String forAddress, String forPort, List<String> askIds){

        StorageMessages.BecomePrimary becomePrimaryMsg = StorageMessages.BecomePrimary.newBuilder()
                .setForApAddress(forAddress)
                .setForPort(forPort)
                .addAllAskIds(askIds)
                .build();

        StorageMessages.StorageMessageWrapper msgWrapper =
                StorageMessages.StorageMessageWrapper.newBuilder()
                        .setBecomePrimary(becomePrimaryMsg)
                        .build();

        return msgWrapper;
    }

//    public static StorageMessages.StorageMessageWrapper StorageNodesHavingChunk(){
//
//    }

//    public static StorageMessages.StorageMessageWrapper buildMapingChunkIdToStorageNodes(HashMap<String, ArrayList<String>> mapping){
//        StorageMessages.MapingChunkIdToStorageNodes mapingChunkIdToStorageNodes= StorageMessages.MapingChunkIdToStorageNodes.newBuilder()
//
//   }
//
//    private static void setTargetField(final StorageMessages.MapingChunkIdToStorageNodes.Builder target, final Object sourceObject, final String targetField)
//            throws IllegalArgumentException {
//
//        Descriptors.FieldDescriptor fieldDescriptor = target.getDescriptorForType().findFieldByName("maping");
//        if (null == fieldDescriptor) {
//            throw new RuntimeException("Unknown target field in protobuf: " + targetField);
//        }
//
//        if (fieldDescriptor.isRepeated()) {
//            target.addRepeatedField(fieldDescriptor, sourceObject);
//        } else {
//            target.setField(fieldDescriptor, sourceObject);
//        }
//    }

}
