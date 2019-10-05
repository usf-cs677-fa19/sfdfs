package edu.usfca.cs.dfs.controllerNode;

import edu.usfca.cs.dfs.controllerNode.data.StorageNodeDetail;

import java.util.HashMap;

public class ControllerDS {

    // here key = ip+port
    private HashMap<String, StorageNodeDetail> storageNodeRegister; // can keep bloomfilter in StorageNodeDetail if we assume bloomfilter should be r
    // efreshed everytime the storage node in deleted


    public ControllerDS() {
        storageNodeRegister = new HashMap<>();

    }

    public HashMap<String, StorageNodeDetail> getStorageNodeRegister() {
        return storageNodeRegister;
    }

    public void updateStorageNodeRegister(StorageNodeDetail snd) {
        String key = snd.getIpAddress()+snd.getPort();

        if(this.storageNodeRegister.containsKey(key)){
            this.existInStorageNodeRegister(key, snd);

        } else {
            this.newInStorageNodeRegister(key,snd);
        }
    }

    private void newInStorageNodeRegister(String key, StorageNodeDetail snd) {
        this.storageNodeRegister.put(key,snd);
    }

    private void existInStorageNodeRegister(String key, StorageNodeDetail snd) {
        this.storageNodeRegister.get(key).setSpaceRemainingMB(snd.getSpaceRemainingMB());
        this.storageNodeRegister.get(key).setTimeStamp(snd.getTimeStamp());
    }
}
