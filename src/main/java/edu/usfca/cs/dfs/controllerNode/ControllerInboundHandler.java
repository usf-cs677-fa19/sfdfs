package edu.usfca.cs.dfs.controllerNode;

import edu.usfca.cs.dfs.init.ConfigSystemParam;
import edu.usfca.cs.dfs.net.MessageSender;
import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.controllerNode.data.FileMetaData;
import edu.usfca.cs.dfs.data.ChunkMetaPOJO;
import edu.usfca.cs.dfs.data.FileChunkId;
import edu.usfca.cs.dfs.data.NodeId;
import edu.usfca.cs.dfs.net.InboundHandler;
import edu.usfca.cs.dfs.storageNode.StorageNodeDS;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerInboundHandler extends InboundHandler {

    @Override
    public void channelRead0(
            ChannelHandlerContext ctx,
            StorageMessages.StorageMessageWrapper msg) {

        if(msg.hasHeartBeatMsg()) {  // if message is a heartbeat
            this.recvHeartBeat(msg);

        }
        else if(msg.hasRetrieveFileMsg()){ // controller receieving a:  RetrieveFile message from client should return file containing mapping of each chunk to storage node //Map<ChunkName,StorageNode>
            System.out.println("Request from client to retrieve file!!!");
            String filename = msg.getRetrieveFileMsg().getFileName();
            //get list of storage nodes from bloomFilter, for chunk 1
            ArrayList<String> storageNodes = ControllerNodeHelper.getStorageNodeFromBloomFiltersForChunk(filename,1);

            System.out.println("Storage node id list for 1st chuck!!");
            // for First node in the list Send RetrieveChunkMeta To Storage
            if(storageNodes.size() == 0){
                System.out.println("No Storage Nodes have the file!!!");
                // todo sed not found
                StorageMessages.StorageMessageWrapper msgWrapper =
                        ControllerStorageMessagesHelper.prepareNoFile(filename);

                Channel clientChan = ctx.channel();
                ChannelFuture clientfuture = clientChan.write(msgWrapper);
                clientChan.flush();

            } else {

                String fileChunkId = FileChunkId.getFileChunkId(filename,1);
                sendRetrieveChunkMetaToStorage(storageNodes.get(0), fileChunkId);

                FileMetaData fileMetaData = ControllerDS.getInstance().getFileMetaData();

                if(fileMetaData != null) {

                    System.out.println("FileMetaData : " + fileMetaData);
                    HashMap<String, ArrayList<String>> mapping = ControllerDS.getInstance().getMappingOfChunkIdToStorageNodes(fileMetaData.getFileName(), fileMetaData.getTotalChunks());
                    //HashMap<String,ArrayList<String>> mapping = ControllerDS.getInstance().getTheMappingOfChunkIdToStorageNodesForClientRequest();
                    fileMetaData = null;
                    if (mapping.size() > 0) {

//                        for(String eachMappingKey : mapping.keySet()) { //todo may be getMappingOfChunkIdToStorageNodes
//                            List<String> replicas = ControllerDS.getInstance().getReplicaList(eachMappingKey);
//                            mapping.get(eachMappingKey).addAll(replicas);
//                        } // todo: anurag add replicas for primary


                        System.out.println("The Mapping in the controller is not empty!!!!");
                        //  StorageMessages.StorageMessageWrapper msgWrapper = ControllerStorageMessagesHelper.buildMappingChunkIdToStorageNodes(mapping);
                        StorageMessages.StorageMessageWrapper msgWrapper = ControllerStorageMessagesHelper.buildMappingChunkIdToStorageNodes(mapping);

                        Channel clientChan = ctx.channel();
                        ChannelFuture clientfuture = clientChan.write(msgWrapper);
                        clientChan.flush();
                    }
                }
            }
        }
        else if(msg.hasChunkMetaMsg()){
            System.out.println("Received chunkMetaMsg from client");

            StorageMessages.ChunkMeta receivedChunkMetaMsg = msg.getChunkMetaMsg();

            ChunkMetaPOJO cm = new ChunkMetaPOJO()
                    .setFilename(receivedChunkMetaMsg.getFileName())
                    .setChunkId(receivedChunkMetaMsg.getChunkId())
                    .setChunkSize(receivedChunkMetaMsg.getChunkSize())
                    .setTotalChunks(receivedChunkMetaMsg.getTotalChunks());

            String[] storageNodesAssigned = this.getStorageNodesForChunkMeta(cm);

            String primaryNode = storageNodesAssigned[0];
           // if (storageNodesAssigned.length > 0) {
            ArrayList<Boolean> chunksStoredInBloomFilters = new ArrayList<>();
            //store the details in bloomfilter

                Boolean stored = ControllerDS.getInstance().storeChunkInBloomFilter(primaryNode,receivedChunkMetaMsg.getFileName(),receivedChunkMetaMsg.getChunkId());
                if(stored) {
                    chunksStoredInBloomFilters.add(stored);
                }
            if (chunksStoredInBloomFilters.size() == 1){
                System.out.println("Bloomfilters filled successfully!!!");
            }

            cm.setStorageNodeIds(storageNodesAssigned);
           // }
                StorageMessages.StorageMessageWrapper msgWrapper = ControllerStorageMessagesHelper.buildChunkMeta(cm); //this.buildChunkMeta(cm);

                Channel chan = ctx.channel();
                ChannelFuture write = chan.write(msgWrapper);
                chan.flush();
               // write.addListener(ChannelFutureListener.CLOSE);

            //todo here - handle context
//
                System.out.println("Sent chunkMetaMsg with list of storage nodes back to  client");

            //ctx.close();
        }
        else if(msg.hasStorageChunkMetaMsg()){
            System.out.println("\n Controller recieved the meta for first chunk from Storage Node!!!!!");

            ControllerDS.getInstance().setFileMetaData(new FileMetaData(msg.getStorageChunkMetaMsg().getFileName(),
                    msg.getStorageChunkMetaMsg().getChunkId(),
                    msg.getStorageChunkMetaMsg().getTotalChunks()));

            ctx.close();
//            String fileName = msg.getStorageChunkMeta().getFileName();
//            int chunkId = msg.getStorageChunkMeta().getChunkId();
//            int totalChunks = msg.getStorageChunkMeta().getTotalChunks();

           // HashMap<String,ArrayList<String>> mapping = ControllerDS.getInstance().getMappingOfChunkIdToStorageNodes(fileName,totalChunks);

           // StorageMessages.StorageMessageWrapper msgWrapper = ControllerStorageMessagesHelper.buildMappingChunkIdToStorageNodes(mapping);
        }
        else if(msg.hasReplyMsg()){

            System.out.println("New Primary created successfully!!");
            ctx.close();

        }
        else if(msg.hasBadChunkFoundMsg()) {
            // getting bad chunk found message

            System.out.println("Recv Bad chunk message from storage node");
            StorageMessages.BadChunkFound badChunkFound = msg.getBadChunkFoundMsg();
            String recvSelfId = badChunkFound.getSelfId();
            String badChunkFoundId = badChunkFound.getFileChunkId();
            String primaryIdForChunk = badChunkFound.getPrimaryIdForChunk();
            // checking in bloomfilter to find primary
            ArrayList<String> primaries = ControllerDS.getInstance().checkBloomFiltersForChunk(badChunkFoundId);
            // and adding replicas
            List<String> primariesWithReplicas =  ControllerDS.getInstance().getListOfReplicasForTheNodes(primaries);
            // removing receieved selfId from list
            for(String id : primariesWithReplicas) {
                if(id.equalsIgnoreCase(recvSelfId)) {
                    primariesWithReplicas.remove(id);
                }
            }
            // prepare HealBadChunkMsg
            StorageMessages.StorageMessageWrapper healBadChunkMsgWrapper =
                    ControllerStorageMessagesHelper.prepareHealBadChunkMsg(recvSelfId, badChunkFoundId, primariesWithReplicas, primaryIdForChunk);
            String[] connectingInfo = NodeId.getIPAndPort(recvSelfId);
            //String replicaNode = "";
//            if(primariesWithReplicas.size() > 0) {
//                replicaNode = primariesWithReplicas.get(0);
//            }
            //String[] connectingInfo = NodeId.getIPAndPort(replicaNode);
            try {
                new MessageSender().send(false,
                        ConfigSystemParam.getNodeType(),
                        connectingInfo[0],
                        Integer.parseInt(connectingInfo[1]),
                        healBadChunkMsgWrapper);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctx.close();
        }
        else {
            System.out.println("Donno what message was received");
            ctx.close();
        }
    }


    private String[] getStorageNodesForChunkMeta(ChunkMetaPOJO cm) {
        String[] arr = null;
        ArrayList<String> threeNodes = ControllerNodeHelper.getThreeNodes(cm);
        arr = threeNodes.toArray(new String[threeNodes.size()]);
        return arr;
    }

    private void recvHeartBeat(StorageMessages.StorageMessageWrapper msg) {
        ControllerNodeHelper.recvHeartBeat(msg);
    }


    public ChannelFuture sendRetrieveChunkMetaToStorage(String storageNode,String fileChunkId ){

        System.out.println("Trying to connect to the primary storage Node");
        String[] connectingInfo = NodeId.getIPAndPort(storageNode);
        String connectingAddress = connectingInfo[0];
        int connectingPort = Integer.parseInt(connectingInfo[1]);

        System.out.println("Connecting Address : "+connectingAddress);

        System.out.println("Connecting Port : "+connectingPort);
        // to be send to storage nodce and recev chunkMeta from storage
        StorageMessages.StorageMessageWrapper msgWrapper =
                ControllerStorageMessagesHelper.buildretrieveChunkMeta(fileChunkId);
        //contact the storage node to get the metadata of the First Chunk
        ChannelFuture write= null;
        try {
            write = new MessageSender().send(true,"controller",connectingAddress,connectingPort,msgWrapper);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return write;
      //  ControllerClient.runControllerClient(connectingAddress,connectingPort, msgWrapper);  not using todo remove this
    }

}
