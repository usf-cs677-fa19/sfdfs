package edu.usfca.cs.dfs.storageNode;

import com.google.protobuf.ByteString;
import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.storageNode.data.ChunkFileMeta;

import java.nio.ByteBuffer;
import java.util.ArrayList;

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

    public static StorageMessages.StorageMessageWrapper buildHeartBeat(String address, int port) {
        StorageMessages.HeartBeat heartBeat = StorageMessages.HeartBeat.newBuilder() // building heartbeat
                .setIpAddress(address)
                .setPort(String.valueOf(port))
                .setSpaceRemainingMB(String.valueOf(1024*1024*1024))  // new File("\").getSpace
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


}
