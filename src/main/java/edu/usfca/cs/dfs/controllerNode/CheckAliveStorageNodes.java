package edu.usfca.cs.dfs.controllerNode;

import edu.usfca.cs.dfs.controllerNode.data.StorageNodeDetail;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Level;


public class CheckAliveStorageNodes extends TimerTask {

    @Override
    public void run(){
        ControllerDS.getInstance().logger.log(Level.INFO,"Checking if Storage Nodes are alive : "+Instant.now()+"\n");
        this.checkAliveStorageNodes();
    }

    public void checkAliveStorageNodes(){
        Iterator hmIterator = ControllerDS.getInstance().getStorageNodeRegister().entrySet().iterator();
        while(hmIterator.hasNext()){
            Map.Entry node = (Map.Entry)hmIterator.next();
            Instant instant = ((StorageNodeDetail)node.getValue()).getTimeStamp();

            if(instant.isBefore(Instant.now().minus(6, ChronoUnit.SECONDS))){
                ControllerDS.getInstance().logger.log(Level.INFO,"Removing the storage node : "+node.getKey()+"\n");
                String nodeToBeDeleted = (String) node.getKey();
               StorageNodeDetail storageNodeDetail = (ControllerDS.getInstance().getStorageNodeRegister()).get(nodeToBeDeleted);
               ControllerDS.getInstance().deleteFromStorageNodeRegister(nodeToBeDeleted);

               ControllerDS.getInstance().faultToleranceWhenAStorageNodeIsDown(nodeToBeDeleted,storageNodeDetail);
            }
        }
    }
}
