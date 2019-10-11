package edu.usfca.cs.dfs.storageNode;

import edu.usfca.cs.dfs.controllerNode.ControllerDS;
import edu.usfca.cs.dfs.data.NodeId;

import java.util.Map;

public class StorageNodeDS {

    private static StorageNodeDS storageNodeDS = null;

    private StorageNodeDS(){
    }

    public static StorageNodeDS getInstance(){
        if(storageNodeDS == null){
            storageNodeDS =  new StorageNodeDS();
        }
        return storageNodeDS;
    }

  //  private Map<String,Map<String,>>

}
