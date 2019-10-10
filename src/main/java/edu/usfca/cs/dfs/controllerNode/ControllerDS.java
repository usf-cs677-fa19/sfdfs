package edu.usfca.cs.dfs.controllerNode;

import edu.usfca.cs.dfs.controllerNode.data.StorageNodeDetail;
//import edu.usfca.cs.dfs.controllerNode.data.StorageNodeGroupRegister;
import edu.usfca.cs.dfs.data.NodeId;
import edu.usfca.cs.dfs.filter.BloomFilter;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControllerDS {

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

    public Map<String, StorageNodeDetail> getStorageNodeRegister() {
        return storageNodeRegister;
    }

    public String getStorageNodeKey(String ipAddress, String port) {
       return NodeId.getId(ipAddress,port);
       // return ipAddress + port;
    }

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
        if (storageNodeRegister.containsKey(key)) {
            storageNodeRegister.remove(key);
        }
    }

    public String findTheStorageNodeToSaveChunk(int size){
        String storageNodeKey = new String();
        storageNodeKey = getSNWithMaxSpace(size);
        return storageNodeKey;
    }

    public String getSNWithMaxSpace(int requiredChunkSize){
        String node = "";
        int size = 0;

        Iterator storageNodeIterator = storageNodeRegister.entrySet().iterator();
        while (storageNodeIterator.hasNext()){
            Map.Entry storageNode = (Map.Entry) storageNodeIterator.next();

            StorageNodeDetail details = (StorageNodeDetail) storageNode.getValue();

            if(size < Integer.parseInt(details.getSpaceRemainingMB())){
                size =  Integer.parseInt(details.getSpaceRemainingMB());
                node = (String) storageNode.getKey();
            }
        }
        if(size > requiredChunkSize) {
            return node;
        }else{
            System.out.println("Storage Node size is less than the required Chunk size!!");
            return "";
        }
    }

    public ArrayList<String> getReplicas(int requiredChunkSize, String primaryNodeKey){
        ArrayList<String> replicas = new ArrayList<>();
        String replica1 = "";
        String replica2 = "";
        int size1 = 0;
        int size2 = 0;
        if(!storageNodeRegister.isEmpty()){
            Iterator storageNodeIterator = storageNodeRegister.entrySet().iterator();
            while (storageNodeIterator.hasNext()){
                Map.Entry storageNode = (Map.Entry) storageNodeIterator.next();
                if(storageNode.getKey() != primaryNodeKey){
                    StorageNodeDetail details = (StorageNodeDetail) storageNode.getValue();
                    if(size1 <  Integer.parseInt(details.getSpaceRemainingMB())){
                        size2 = size1;
                        size1 =  Integer.parseInt(details.getSpaceRemainingMB());

                        replica2 = replica1;
                        replica1 = (String) storageNode.getKey();
                    }else if(size2 <  Integer.parseInt(details.getSpaceRemainingMB())){
                        size2 =  Integer.parseInt(details.getSpaceRemainingMB());
                        replica2 = (String) storageNode.getKey();
                    }
                }
            }
        }
       if(size1 > requiredChunkSize){
            replicas.add( replica1);
           replicas.add( replica2);
       }
        return replicas;
    }

    public Map<String, List<String>> getStorageNodeGroupRegister(){
        return this.storageNodeGroupRegister;
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

        System.out.println("Check Storage Node Group Register !!!! \n\n\n");
        ArrayList<String> storageNodePrimaryReplicaDetails = new ArrayList<>();
        if(checkIfPrimaryExists(node)){
            storageNodePrimaryReplicaDetails.add(node);
            storageNodePrimaryReplicaDetails.addAll(getReplicaList(node));
        }else{
            storageNodePrimaryReplicaDetails = ControllerDS.getInstance().getReplicas(chunkSize,node);
        }
        return storageNodePrimaryReplicaDetails;
    }

    public boolean checkIfPrimaryExists(String node){
        return this.storageNodeGroupRegister.containsKey(node);
    }

    public List<String> getReplicaList(String node){
        return this.storageNodeGroupRegister.get(node);
    }

    public ArrayList<String> checkBloomFiltersForChunk(String chunkName){
        ArrayList<String> storageNodes = new ArrayList<>();

        if(!storageNodeRegister.isEmpty()){
            Iterator storageNodeIterator = storageNodeRegister.entrySet().iterator();
            while (storageNodeIterator.hasNext()) {
                Map.Entry storageNode = (Map.Entry) storageNodeIterator.next();

                StorageNodeDetail storageNodeDetail1 = (StorageNodeDetail) storageNode.getValue();
                BloomFilter bloomFilter = storageNodeDetail1.getBloomFilter();
                boolean inStorageNode = bloomFilter.getFromBloom(chunkName);
                if(inStorageNode) {
                    String storageNodeKey = (String) storageNode.getKey();
                    storageNodes.add(storageNodeKey);
                }
            }
        }else {
            System.out.println("The StorageNode register is empty! No storage Node Details are stored! ");
        }
        return storageNodes;
    }

}
