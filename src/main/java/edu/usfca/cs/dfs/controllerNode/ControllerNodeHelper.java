package edu.usfca.cs.dfs.controllerNode;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.data.ChunkMeta;
import edu.usfca.cs.dfs.controllerNode.data.StorageNodeDetail;
import java.time.Instant;
import java.util.Timer;

public class ControllerNodeHelper{

    //private static ControllerDS controllerDS;

    private static ControllerNodeHelper controllerNodeHelper = null;

    private ControllerNodeHelper(/*ControllerDS controllerDS*/) {
        //this.controllerDS = controllerDS;
        this.checkAliveStorageNodes();
    }

    public static synchronized ControllerNodeHelper getControllerNodeHelper(){
        if(controllerNodeHelper == null){
            controllerNodeHelper = new ControllerNodeHelper();
        }
        return controllerNodeHelper;
    }

    public void checkAliveStorageNodes() {
        Timer timer = new Timer();
        timer.schedule(
                new CheckAliveStorageNodes(),
                0,
                8000);
    }

//    public static Map<String, StorageNodeDetail> getStorageNodes(){
//        return controllerDS.getStorageNodeRegister();
//    }
//
//    public static void updateStorageNodeRegister(StorageNodeDetail snd){
//        controllerDS.updateStorageNodeRegister(snd);
//    }

    public void recvHeartBeat(StorageMessages.StorageMessageWrapper msg) {
        System.out.println("heartbeat from: "+msg.getHeartBeat().getIpAddress()+":"+msg.getHeartBeat().getPort());

        ControllerDS.CDS.updateStorageNodeRegister(new StorageNodeDetail(
                msg.getHeartBeat().getIpAddress(),
                msg.getHeartBeat().getPort(),
                msg.getHeartBeat().getSpaceRemainingMB(),
                Instant.now()
        ));

        System.out.println("StorageNodeDetailList size: "+ControllerDS.CDS.getStorageNodeRegister().size());
    }

    //for a chunk return 3 storage node

    public String[] storeChunkMetadata(ChunkMeta chunkMeta){
        //check if the space in storage nodes
       // Map<Integer, StorageNodeKey> threeStorageNodes = new HashMap<Integer, StorageNodeKey>();
        String[] threeStorageNodes = {};

        ControllerDS.CDS.findTheStorageNodeToSaveChunk(chunkMeta.getChunkSize());


        return threeStorageNodes;
    }



}
