package edu.usfca.cs.dfs.controllerNode;

import edu.usfca.cs.dfs.StorageMessages;
//import edu.usfca.cs.dfs.controllerNode.data.StorageNodeGroupRegister;
import edu.usfca.cs.dfs.data.ChunkMetaPOJO;
import edu.usfca.cs.dfs.controllerNode.data.StorageNodeDetail;
import edu.usfca.cs.dfs.data.FileChunkId;
import edu.usfca.cs.dfs.data.NodeId;
import edu.usfca.cs.dfs.net.Client;
import edu.usfca.cs.dfs.storageNode.StorageStorageMessagesHelper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class ControllerNodeHelper{

    public static void checkAliveStorageNodes() {
        Timer timer = new Timer();
        timer.schedule(
                new CheckAliveStorageNodes(),
                0,
                8000);
    }


    public static void recvHeartBeat(StorageMessages.StorageMessageWrapper msg) {
        System.out.println("heartbeat from: "+msg.getHeartBeat().getIpAddress()+":"+msg.getHeartBeat().getPort());

        ControllerDS.getInstance().updateStorageNodeRegister(new StorageNodeDetail(
                msg.getHeartBeat().getIpAddress(),
                msg.getHeartBeat().getPort(),
                msg.getHeartBeat().getSpaceRemainingMB(),
                Instant.now()
        ));

        System.out.println("StorageNodeDetailList size: "+ControllerDS.getInstance().getStorageNodeRegister().size());
    }
    //for a chunk return 3 storage node

    public static ArrayList<String> getThreeNodes(ChunkMetaPOJO chunkMetaPOJO){
        ArrayList<String> threeStorageNodes = new ArrayList<>();

        String primaryNode = ControllerDS.getInstance().findTheStorageNodeToSaveChunk(chunkMetaPOJO.getChunkSize());  //get the storage node to

        System.out.println("Primary node : \n\n"+primaryNode);
        if(primaryNode != "") {                                                                                     //if there is atleast one storage node registered with the client

            ArrayList<String> replicaNodesArrayList = (ControllerDS.getInstance())
                    .checkStorageNodeGroupRegister(primaryNode, chunkMetaPOJO.getChunkSize());                          //check if the primary node selected has replicas in the StorageNodeGropuRegister

            if (replicaNodesArrayList.size() > 0) {                                                                 //if number of storage nodes registered > 0
               // threeStorageNodes.add(primaryNode);                                                                 // add primary node
                System.out.println("The replicaNodesArrayList.size() : "+replicaNodesArrayList.size());
                threeStorageNodes.addAll(replicaNodesArrayList);                                                    //add the replicas
            }else{
                System.out.println("Only one Storage Node registered with the Controller!!");
                threeStorageNodes.add(primaryNode);                                                                 // add the primary node
            }
        }else{
            System.out.println("No Storage nodes registered with the Controller!!");                                //no storage nodes registered
        }
        System.out.println("The size of threeNodes : "+threeStorageNodes.size());
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
            System.out.println("Sending the becomePrimaryToStorageNode!!");
            new Client().runClient(true,"controller",newIP,newPort,msgWrapper);
        } catch (InterruptedException e) {
            System.out.println("Exception in connecting to the Storage node!");
            e.printStackTrace();
        }
    }
}
