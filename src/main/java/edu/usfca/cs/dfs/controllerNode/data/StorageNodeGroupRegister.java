package edu.usfca.cs.dfs.controllerNode.data;

import edu.usfca.cs.dfs.controllerNode.ControllerDS;
import edu.usfca.cs.dfs.controllerNode.ControllerNodeHelper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StorageNodeGroupRegister {

    private static StorageNodeGroupRegister register = null;
    private Map<String, List<String>> storageNodeGroupRegister;

    private StorageNodeGroupRegister() {
        storageNodeGroupRegister = new ConcurrentHashMap<>();
    }

    public static synchronized StorageNodeGroupRegister getStorageNodeGroupRegister(){
        if (register == null){
            return new StorageNodeGroupRegister();
        }else {
            return register;
        }
    }

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
            String newReplicaOne = ControllerDS.CDS.getSNWithMaxSpace(chunkSize);
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
