package edu.usfca.cs.dfs.storageNode;

import edu.usfca.cs.dfs.StorageMessages;

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


}
