package edu.usfca.cs.dfs.storageNode;

import com.google.protobuf.ByteString;
import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.storageNode.data.ChunkFileMeta;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class StorageStorageMessagesHelper {
//
////    private String nodeType;
////    private String address;
////    private int port;
////    private String connectingAddress;
////    private int connectingPort;
//
//
//    public StorageStorageMessagesHelper() {
//    }
//
////    public StorageClientHelper(String nodeType, String address, String port) {
////        this.nodeType = nodeType;
////        this.address = address;
////        this.port = port;
////    }

    public static StorageMessages.StorageMessageWrapper prepareHeartBeat
            (String address, int port, long spaceRemaining, long requestProcessed, long retrievalProcessed) {
        StorageMessages.HeartBeat heartBeat = StorageMessages.HeartBeat.newBuilder() // building heartbeat
                .setIpAddress(address)
                .setPort(String.valueOf(port))
                .setSpaceRemaining(spaceRemaining)  // new File("\").getSpace
                .setRequestProcessed(requestProcessed)
                .setRetrievalProcessed(retrievalProcessed)
                .build();

        StorageMessages.StorageMessageWrapper msgWrapper =
                StorageMessages.StorageMessageWrapper.newBuilder()
                        .setHeartBeat(heartBeat)
                        .build();

        return msgWrapper;
    }


    public static StorageMessages.StorageMessageWrapper prepareStoreChunkMsgForReplica(StorageMessages.StorageMessageWrapper msg, int toReplicaNo) {

        StorageMessages.StoreChunk recvStoreChunk = msg.getStoreChunkMsg();

        System.out.println("Size of StorageNodeIds : "+ recvStoreChunk.getStorageNodeIdsList().size());
        StorageMessages.StoreChunk storeChunkMsg
                = StorageMessages.StoreChunk.newBuilder()
                .setFileName(recvStoreChunk.getFileName())
                .setChunkId(recvStoreChunk.getChunkId())
                .setChunkSize(recvStoreChunk.getChunkSize())
                .setTotalChunks(recvStoreChunk.getTotalChunks())
                .addAllStorageNodeIds(recvStoreChunk.getStorageNodeIdsList())
                .setData(recvStoreChunk.getData())
                .setToStorageNodeId(recvStoreChunk.getStorageNodeIdsList().get(toReplicaNo))
                .build();

        StorageMessages.StorageMessageWrapper msgWrapper =
                StorageMessages.StorageMessageWrapper.newBuilder()
                        .setStoreChunkMsg(storeChunkMsg)
                        .build();

        return msgWrapper;
    }

    public static StorageMessages.StorageMessageWrapper prepareChunkMetaInfo(ChunkFileMeta chunkFileMeta){
        StorageMessages.StorageChunkMeta chunkMetaInfo = StorageMessages.StorageChunkMeta.newBuilder()
                .setFileName(chunkFileMeta.getFileName())
                .setChunkId(chunkFileMeta.getChunkId())
                .setTotalChunks(chunkFileMeta.getTotalChunks()).build();

        StorageMessages.StorageMessageWrapper msgWrapper =
                StorageMessages.StorageMessageWrapper.newBuilder()
                        .setStorageChunkMeta(chunkMetaInfo)
                        .build();

        return msgWrapper;
    }

    public static StorageMessages.StorageMessageWrapper prepareChunkMsg(String fileChunkId, ByteBuffer buff){
        StorageMessages.Chunk chunk = StorageMessages.Chunk.newBuilder()
                .setFileChunkId(fileChunkId)
                .setData(ByteString.copyFrom(buff))
                .build();

        StorageMessages.StorageMessageWrapper msgWrapper =
                StorageMessages.StorageMessageWrapper.newBuilder()
                        .setChunkMsg(chunk)
                        .build();

        return msgWrapper;
    }


    public static StorageMessages.StorageMessageWrapper buildNewPrimaryAlert(String forAddress, String forPort){

        StorageMessages.NewPrimaryAlert newPrimaryAlertMsg = StorageMessages.NewPrimaryAlert.newBuilder()
                .setForIpAddress(forAddress)
                .setForPort(forPort)
                .setNewIpAddress(StorageNodeDS.getInstance().getIpAddress())
                .setNewPort(String.valueOf(StorageNodeDS.getInstance().getPort()))
                .build();

        StorageMessages.StorageMessageWrapper msgWrapper =
                StorageMessages.StorageMessageWrapper.newBuilder()
                        .setNewPrimaryAlert(newPrimaryAlertMsg)
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
                .addAllStorageNodeIds(cmMsg.getStorageNodeIdsList()) // new storage id at 0
                .setData(ByteString.copyFrom(buffer))
                .setToStorageNodeId(cmMsg.getStorageNodeIdsList().get(1))
                .build();

        StorageMessages.StorageMessageWrapper msgWrapper =
                StorageMessages.StorageMessageWrapper.newBuilder()
                        .setStoreChunkMsg(storeChunkMsg)
                        .build();

        return msgWrapper;
    }

    public static StorageMessages.StorageMessageWrapper prepareStoreChunk(String nodeId, List<String> storageNodes,ChunkFileMeta chunkFileMeta){


        StorageMessages.StoreChunk  storeChunk = StorageMessages.StoreChunk.newBuilder()
                .setChunkId(chunkFileMeta.getChunkId())
                .setChunkSize(chunkFileMeta.getChunkSize())
                .setFileName(chunkFileMeta.getFileName())
                .setTotalChunks(chunkFileMeta.getTotalChunks())
                .addAllStorageNodeIds(storageNodes)
                .setToStorageNodeId(nodeId)
                .build();

        StorageMessages.StorageMessageWrapper msgWrapper = StorageMessages.StorageMessageWrapper.newBuilder()
                .setStoreChunkMsg(storeChunk)
                .build();

        return msgWrapper;
    }

}
