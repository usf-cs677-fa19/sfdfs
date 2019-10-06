package edu.usfca.cs.dfs.controllerNode;

import edu.usfca.cs.dfs.NodeClient;
import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.controllerNode.data.StorageNodeDetail;
import edu.usfca.cs.dfs.storageNode.HeartBeatSender;

import java.time.Instant;
import java.util.Map;
import java.util.Timer;

public class ControllerNodeHelper{

    private static ControllerDS controllerDS;

    public ControllerNodeHelper(ControllerDS controllerDS) {
        this.controllerDS = controllerDS;
        this.checkAliveStorageNodes();
    }

    public void checkAliveStorageNodes() {
        Timer timer = new Timer();
        timer.schedule(
                new CheckAliveStorageNodes(),
                0,
                7000);
    }

    public static Map<String, StorageNodeDetail> getStorageNodes(){
        return controllerDS.getStorageNodeRegister();
    }

    public static void updateStorageNodeRegister(StorageNodeDetail snd){
        controllerDS.updateStorageNodeRegister(snd);
    }

    public static void recvHeartBeat(StorageMessages.StorageMessageWrapper msg) {
        System.out.println("heartbeat from: "+msg.getHeartBeat().getIpAddress()+":"+msg.getHeartBeat().getPort());

        updateStorageNodeRegister(new StorageNodeDetail(
                msg.getHeartBeat().getIpAddress(),
                msg.getHeartBeat().getPort(),
                msg.getHeartBeat().getSpaceRemainingMB(),
                Instant.now()
        ));

        System.out.println("StorageNodeDetailList size: "+ControllerDS.getStorageNodeRegister().size());
    }

    //for a chunk return 3 storage node


}
