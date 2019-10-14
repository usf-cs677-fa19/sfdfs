package edu.usfca.cs.dfs.storageNode;

import edu.usfca.cs.dfs.net.MessageSender;
import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.nodes.SfdfsNode;

import java.util.Timer;


//public class StorageNode implements SfdfsNode {


//    public StorageNode() {
//        this.keepSendingHeartBeat();
//    }

//    public void keepSendingHeartBeat() {
//        Timer timer = new Timer();
//        timer.schedule(
//                new HeartBeatSender(
//                        StorageNodeDS.getInstance().getControllerIpAddress(),
//                        StorageNodeDS.getInstance().getControllerPort(),
//                        StorageStorageMessagesHelper.buildHeartBeat(
//                                StorageNodeDS.getInstance().getIpAddress(),
//                                StorageNodeDS.getInstance().getPort(),
//                                StorageNodeDS.getInstance().
//                        )
//                ),
//                0,
//                5000);
//    }

//    public void startClient(String connectingAddress, int connectingPort, StorageMessages.StorageMessageWrapper msgWrapper)
//            throws InterruptedException {
//
//        new MessageSender().send(false, StorageNodeDS.getInstance().getNodeId(), connectingAddress, connectingPort, msgWrapper);
//
//    }



//}


