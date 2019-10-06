package edu.usfca.cs.dfs.controllerNode;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.controllerNode.data.StorageNodeDetail;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ControllerDS {

    CDS;

    // here key = ip+port
    private Map<String, StorageNodeDetail> storageNodeRegister = new ConcurrentHashMap<>();


    public Map<String, StorageNodeDetail> getStorageNodeRegister() {
        return storageNodeRegister;
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
        String key = snd.getIpAddress()+snd.getPort();

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
}
