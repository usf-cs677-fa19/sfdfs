package edu.usfca.cs.dfs.controllerNode;

import edu.usfca.cs.dfs.StorageMessages;
//import edu.usfca.cs.dfs.controllerNode.data.StorageNodeGroupRegister;
import edu.usfca.cs.dfs.data.ChunkMetaPOJO;
import edu.usfca.cs.dfs.controllerNode.data.StorageNodeDetail;
import edu.usfca.cs.dfs.data.FileChunkId;
import edu.usfca.cs.dfs.data.NodeId;

import edu.usfca.cs.dfs.net.MessageSender;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;

public class ControllerNodeHelper{

    public static void checkAliveStorageNodes() {
        Timer timer = new Timer();
        timer.schedule(
                new CheckAliveStorageNodes(),
                0,
                8000);
    }


    public static void recvHeartBeat(StorageMessages.StorageMessageWrapper msg) {

        ControllerDS.getInstance().logger.log(Level.INFO,"heartbeat from: "+msg.getHeartBeatMsg().getIpAddress()+":"+msg.getHeartBeatMsg().getPort());
        ControllerDS.getInstance().logger.log(Level.INFO,"SpaceRem: "+msg.getHeartBeatMsg().getSpaceRemaining()+", "
                + "ReqProcessed: "+msg.getHeartBeatMsg().getRequestProcessed()+", "
                + "RetrievalProcessed: "+msg.getHeartBeatMsg().getRetrievalProcessed());

        ControllerDS.getInstance().updateStorageNodeRegister(new StorageNodeDetail(
                msg.getHeartBeatMsg().getIpAddress(),
                msg.getHeartBeatMsg().getPort(),
                msg.getHeartBeatMsg().getSpaceRemaining(),
                msg.getHeartBeatMsg().getRequestProcessed(),
                msg.getHeartBeatMsg().getRetrievalProcessed(),
                Instant.now()
        ));

        ControllerDS.getInstance().logger.log(Level.INFO,"StorageNodeDetailList size: "+ControllerDS.getInstance().getStorageNodeRegister().size());
    }
    //for a chunk return 3 storage node

    public static ArrayList<String> getThreeNodes(ChunkMetaPOJO chunkMetaPOJO){
        ArrayList<String> threeStorageNodes = new ArrayList<>();

        String primaryNode = ControllerDS.getInstance().findTheStorageNodeToSaveChunk(chunkMetaPOJO.getChunkSize());  //get the storage node to

        ControllerDS.getInstance().logger.log(Level.INFO,"Primary node : \n\n"+primaryNode);
        if(primaryNode != "") {                                                                                     //if there is atleast one storage node registered with the client

            ArrayList<String> replicaNodesArrayList = (ControllerDS.getInstance())
                    .checkStorageNodeGroupRegister(primaryNode, chunkMetaPOJO.getChunkSize());                          //check if the primary node selected has replicas in the StorageNodeGropuRegister

            if (replicaNodesArrayList.size() > 0) {                                                                 //if number of storage nodes registered > 0
               // threeStorageNodes.add(primaryNode);                                                                 // add primary node
                ControllerDS.getInstance().logger.log(Level.INFO,"The replicaNodesArrayList.size() : "+replicaNodesArrayList.size());
                threeStorageNodes.addAll(replicaNodesArrayList);                                                    //add the replicas
            }else{
                ControllerDS.getInstance().logger.log(Level.INFO,"Only one Storage Node registered with the Controller!!");
                threeStorageNodes.add(primaryNode);                                                                 // add the primary node
            }
        }else{
            ControllerDS.getInstance().logger.log(Level.INFO,"No Storage nodes registered with the Controller!!");
        }
        ControllerDS.getInstance().logger.log(Level.INFO,"The size of threeNodes : "+threeStorageNodes.size());
        return threeStorageNodes;
    }

    public static ArrayList<String> getStorageNodeFromBloomFiltersForChunk(String filename, int chunkNumber) {

        String chunkName = FileChunkId.getFileChunkId(filename, chunkNumber);

        //check in bloomfilter and get list of chunkholders
        ArrayList<String> storageNodes = ControllerDS.getInstance().checkBloomFiltersForChunk(chunkName);
        return storageNodes;
    }

    public static void handleStorageNodeFail(String nodeIdDown) {
        ControllerDS.getInstance().getStorageNodeGroupRegister();
    }

    public static void becomeNewPrimary(String newPrimaryNode, List<String> newReplicas,String nodeIdToDelete){
        String[] ipAndPortToDelete = NodeId.getIPAndPort(nodeIdToDelete);
        String forIP = ipAndPortToDelete[0];
        String forPort = ipAndPortToDelete[1];

        String[] newPrimaryIPAndPort = NodeId.getIPAndPort(newPrimaryNode);
        String newIP = newPrimaryIPAndPort[0];
        int newPort = Integer.parseInt(newPrimaryIPAndPort[1]);

        StorageMessages.StorageMessageWrapper msgWrapper = ControllerStorageMessagesHelper.buildBecomePrimary(forIP,forPort,newReplicas);

        try {
            ControllerDS.getInstance().logger.log(Level.INFO,"Sending the becomePrimaryToStorageNode!!");
            new MessageSender().send(true,"controller",newIP,newPort,msgWrapper);
        } catch (InterruptedException e) {
            ControllerDS.getInstance().logger.log(Level.SEVERE,"Exception in connecting to the Storage node!");
            e.printStackTrace();
        }
    }
}
