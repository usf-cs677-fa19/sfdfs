package edu.usfca.cs.dfs.storageNode;

import edu.usfca.cs.dfs.init.ConfigSystemParam;
import edu.usfca.cs.dfs.net.MessageSender;
import edu.usfca.cs.dfs.StorageMessages;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.TimerTask;
import java.util.logging.Level;

public class HeartBeatSender implements Runnable {

    //private StorageMessages.StorageMessageWrapper heartBeat;
    private String connectingAddress;
    private int connectingPort;
    //private StorageNodeClient client;

    public Logger logger = Logger.getLogger(HeartBeatSender.class.getName());

    public HeartBeatSender(/*String connectingAddress, int connectingPort*/) {
        //this.client = client;
        //this.connectingAddress = connectingAddress;
        //this.connectingPort = connectingPort;
        //this.heartBeat = heartBeat;
    }

    @Override
    public void run() {

        for (; ;) {
            StorageMessages.StorageMessageWrapper heartBeat = StorageStorageMessagesHelper.prepareHeartBeat(
                    StorageNodeDS.getInstance().getIpAddress(),
                    StorageNodeDS.getInstance().getPort(),
                    StorageNodeDS.getInstance().getSpaceRemaining(),
                    StorageNodeDS.getInstance().getRequestProcessed(),
                    StorageNodeDS.getInstance().getRetrievalProcessed()


            );
            logger.log(Level.INFO," new heartbeat : \n"+
                    "IpAdd: "+heartBeat.getHeartBeatMsg().getIpAddress()+
                    ", port: "+heartBeat.getHeartBeatMsg().getPort()+
                    ", space: "+heartBeat.getHeartBeatMsg().getSpaceRemaining()+
                    ", reqs: "+heartBeat.getHeartBeatMsg().getRequestProcessed()+
                    ", rets: "+heartBeat.getHeartBeatMsg().getRetrievalProcessed());
            this.sendHeartBeat(heartBeat);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



    }

    public void sendHeartBeat(StorageMessages.StorageMessageWrapper heartBeat) {

        try {
            new MessageSender().send(false, "storage", ConfigSystemParam.getControllerAddress(),
                    ConfigSystemParam.getControllerPort(), heartBeat);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


//        try {
//            this.client.startClient(this.connectingAddress, this.connectingPort, this.heartBeat);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }


}