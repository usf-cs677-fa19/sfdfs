package edu.usfca.cs.dfs.controllerNode;

import edu.usfca.cs.dfs.controllerNode.data.StorageNodeDetail;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;


public class CheckAliveStorageNodes extends TimerTask {

    @Override
    public void run(){
        System.out.println("\nChecking if Storage Nodes are alive : "+Instant.now()+"\n");
        //Map<String, StorageNodeDetail> storageNodes =  ControllerDS.CDS.getStorageNodeRegister();
        this.checkAliveStorageNodes(/*storageNodes*/);
    }


    public void checkAliveStorageNodes(/*Map<String, StorageNodeDetail> storageNodes*/){
//        System.out.println("Checking the hashmap and removing the nodes\n");
//        Iterator hmIterator = storageNodes.entrySet().iterator();
        Iterator hmIterator = ControllerDS.CDS.getStorageNodeRegister().entrySet().iterator();
        while(hmIterator.hasNext()){
            Map.Entry node = (Map.Entry)hmIterator.next();
            Instant instant = ((StorageNodeDetail)node.getValue()).getTimeStamp();

            if(instant.isBefore(Instant.now().minus(6, ChronoUnit.SECONDS))){
                System.out.println("Removing the storage node : "+node.getKey()+"\n");
//                 storageNodes.remove(node.getKey());
                ControllerDS.CDS.deleteFromStorageNodeRegister((String) node.getKey());
            }
        }
    }
}
