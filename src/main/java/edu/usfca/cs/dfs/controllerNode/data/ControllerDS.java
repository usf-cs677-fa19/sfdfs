package edu.usfca.cs.dfs.controllerNode.data;

import java.util.ArrayList;
import java.util.List;

public class ControllerDS {

    private List<StorageNodeDetail> storageNodeDetailList;

    public ControllerDS() {
        storageNodeDetailList = new ArrayList<>();
    }

    public List<StorageNodeDetail> getStorageNodeDetailList() {
        return storageNodeDetailList;
    }

    public void addToStorageNodeDetailList(StorageNodeDetail snd) {
        this.storageNodeDetailList.add(snd);
    }
}
