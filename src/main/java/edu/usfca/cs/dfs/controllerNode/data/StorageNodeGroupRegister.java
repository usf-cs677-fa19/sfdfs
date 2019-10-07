package edu.usfca.cs.dfs.controllerNode.data;

import java.util.*;

public class StorageNodeGroupRegister {
    Map<String, List<String>> storageNodeGroupRegister;

    public StorageNodeGroupRegister() {
        storageNodeGroupRegister = new HashMap<>();
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

    public ArrayList<String> checkStorageNodeGroupRegister(String node){
        ArrayList<String> storageNodePrimaryReplicaDetails = new ArrayList<>();
        if(checkIfPrimaryExists(node)){
            storageNodePrimaryReplicaDetails.addAll(getReplicaList(node));
            storageNodePrimaryReplicaDetails.add(node);
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
