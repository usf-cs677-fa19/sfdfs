package edu.usfca.cs.dfs.storageNode;

import edu.usfca.cs.dfs.net.MessageSender;
import edu.usfca.cs.dfs.StorageMessages;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.TimerTask;
import java.util.logging.Level;

public class HeartBeatSender extends TimerTask {

    private StorageMessages.StorageMessageWrapper heartBeat;
    private String connectingAddress;
    private int connectingPort;
    //private StorageNodeClient client;

    public Logger logger = Logger.getLogger(HeartBeatSender.class.getName());

    public HeartBeatSender(String connectingAddress, int connectingPort, StorageMessages.StorageMessageWrapper heartBeat) {
        //this.client = client;
        logger.log(Level.INFO,"heartbeat : \n"+
                "IpAdd: "+heartBeat.getHeartBeatMsg().getIpAddress()+
                ", port: "+heartBeat.getHeartBeatMsg().getPort()+
                ", space: "+heartBeat.getHeartBeatMsg().getSpaceRemaining()+
                ", reqs: "+heartBeat.getHeartBeatMsg().getRequestProcessed()+
                ", rets: "+heartBeat.getHeartBeatMsg().getRetrievalProcessed());
        this.connectingAddress = connectingAddress;
        this.connectingPort = connectingPort;
        this.heartBeat = heartBeat;


    }

    @Override
    public void run() {
        this.sendHeartBeat();
    }

    public void sendHeartBeat() {

        try {
            new MessageSender().send(false, "storage", connectingAddress, connectingPort, heartBeat);
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
