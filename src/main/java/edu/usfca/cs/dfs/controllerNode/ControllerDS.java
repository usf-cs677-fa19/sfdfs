package edu.usfca.cs.dfs.controllerNode;

import edu.usfca.cs.dfs.controllerNode.data.StorageNodeDetail;
//import edu.usfca.cs.dfs.controllerNode.data.StorageNodeGroupRegister;
import edu.usfca.cs.dfs.data.NodeId;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControllerDS {

    //CDS;

    private static ControllerDS controllerDS = null;

    private ControllerDS(){
    }

    public static ControllerDS getInstance(){
        if(controllerDS == null){
            controllerDS =  new ControllerDS();
        }
        return controllerDS;
    }

    // here key = ip+port
    private Map<String, StorageNodeDetail> storageNodeRegister = new ConcurrentHashMap<>();
    private Map<String, List<String>> storageNodeGroupRegister = new ConcurrentHashMap<>(); //exp
    //public StorageNodeGroupRegister storageNodeGroupRegister = StorageNodeGroupRegister.getStorageNodeGroupRegister();

    public Map<String, StorageNodeDetail> getStorageNodeRegister() {
        return storageNodeRegister;
    }

    public String getStorageNodeKey(String ipAddress, String port) {
       return new NodeId(ipAddress,port).getId();
       // return ipAddress + port;
    }

//    private void recvHeartBeat(StorageMessages.StorageMessageWrapper msg) {
//        System.out.println("heartbeat from: "+msg.getHeartBeat().getIpAddress()+":"+msg.getHeartBeat().getPort());
//
//        this.updateStorageNodeRegister(new StorageNodeDetail(
//                msg.getHeartBeat().getIpAddress(),
//                msg.getHeartBeat().getPort(),
//                msg.getHeartBeat().getSpaceRemainingMB(),
//                Instant.now()
//        ));
//
//        System.out.println("StorageNodeDetailList size: "+this.getStorageNodeRegister().size());
//    }

    public void updateStorageNodeRegister(StorageNodeDetail snd) {
        String key = getStorageNodeKey(snd.getIpAddress(), snd.getPort());

        if(storageNodeRegister.containsKey(key)){
            existInStorageNodeRegister(key, snd);

        } else {
            newInStorageNodeRegister(key,snd);
        }
    }

    private void newInStorageNodeRegister(String key, StorageNodeDetail snd) {
        storageNodeRegister.put(key,snd);
    }

    private void existInStorageNodeRegister(String key, StorageNodeDetail snd) {
        storageNodeRegister.get(key).setSpaceRemainingMB(snd.getSpaceRemainingMB());
        storageNodeRegister.get(key).setTimeStamp(snd.getTimeStamp());
    }

    public void deleteFromStorageNodeRegister(String key) {
        //String key = snd.getIpAddress() + snd.getPort();
        if (storageNodeRegister.containsKey(key)) {
            storageNodeRegister.remove(key);
        }
    }

    public String findTheStorageNodeToSaveChunk(int size){
        String storageNodeKey = new String();

        storageNodeKey = getSNWithMaxSpace(size);
//        Iterator storageNodeIterator = storageNodeRegister.entrySet().iterator();
//
//        while (storageNodeIterator.hasNext()){
//            Map.Entry node = (Map.Entry) storageNodeIterator.next();
//
//            StorageNodeDetail details = (StorageNodeDetail) node.getValue();
//
//
//            if(Integer.getInteger(((StorageNodeDetail) node.getValue()).getSpaceRemainingMB()) >= size){
//
//                storageNodeKey = (String)node.getKey();
//            }
//        }
        return storageNodeKey;
    }

    public String getSNWithMaxSpace(int requiredChunkSize){
        String node = "";
        int size = 0;

        Iterator storageNodeIterator = storageNodeRegister.entrySet().iterator();
        while (storageNodeIterator.hasNext()){
            Map.Entry storageNode = (Map.Entry) storageNodeIterator.next();

            StorageNodeDetail details = (StorageNodeDetail) storageNode.getValue();

            if(size < Integer.getInteger(details.getSpaceRemainingMB())){
                size = Integer.getInteger(details.getSpaceRemainingMB());
                node = (String) storageNode.getKey();
            }
        }
        if(size > requiredChunkSize) {
            return node;
        }else{
            return "";
        }
    }

    public String[] getReplicas(int requiredChunkSize, String primaryNodeKey){
        String[] replicas = new String[2];
        String replica1 = "";
        String replica2 = "";
        int size1 = 0;
        int size2 = 0;
        Iterator storageNodeIterator = storageNodeRegister.entrySet().iterator();
       while (storageNodeIterator.hasNext()){
           Map.Entry storageNode = (Map.Entry) storageNodeIterator.next();
           if(storageNode.getKey() != primaryNodeKey){
               StorageNodeDetail details = (StorageNodeDetail) storageNode.getValue();
               if(size1 < Integer.getInteger(details.getSpaceRemainingMB())){
                   size2 = size1;
                   size1 = Integer.getInteger(details.getSpaceRemainingMB());

                   replica2 = replica1;
                   replica1 = (String) storageNode.getKey();
               }else if(size2 < Integer.getInteger(details.getSpaceRemainingMB())){
                   size2 = Integer.getInteger(details.getSpaceRemainingMB());
                   replica2 = (String) storageNode.getKey();
               }
           }
       }
       if(size1 > requiredChunkSize){
            replicas[1] = replica1;
            replicas[2] = replica2;
       }
        return replicas;
    }


    /// storageNodeGroupRegister
    public void addAPrimaryNode(String primaryKey) {
        storageNodeGroupRegister.put(primaryKey, new ArrayList<>());
    }

    public boolean addAReplica(String primaryKey, String replicaKey) {
        // todo : take no of replica from configSystem.json
        int replication = 2;
        if((storageNodeGroupRegister.containsKey(primaryKey)) && storageNodeGroupRegister.get(primaryKey).size() < replication) {
            storageNodeGroupRegister.get(primaryKey).add(replicaKey);
            return true;
        }
        return false;
    }

    public ArrayList<String> checkStorageNodeGroupRegister(String node, int chunkSize){
        ArrayList<String> storageNodePrimaryReplicaDetails = new ArrayList<>();
        if(checkIfPrimaryExists(node)){
            storageNodePrimaryReplicaDetails.add(node);
            storageNodePrimaryReplicaDetails.addAll(getReplicaList(node));
        }else{
            String newReplicaOne = ControllerDS.getInstance().getSNWithMaxSpace(chunkSize);
            //String
            //todo : get new replicas  ok
        }
        return storageNodePrimaryReplicaDetails;
    }

    public boolean checkIfPrimaryExists(String node){
        return this.storageNodeGroupRegister.containsKey(node);
    }

    public List<String> getReplicaList(String node){
        return this.storageNodeGroupRegister.get(node);
    }

}
