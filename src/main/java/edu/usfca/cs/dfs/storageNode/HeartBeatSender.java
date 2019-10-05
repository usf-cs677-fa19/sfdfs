package edu.usfca.cs.dfs.storageNode;

import java.io.IOException;
import java.util.TimerTask;

public class HeartBeatSender extends TimerTask {

    @Override
    public void run() {
        this.sendHeartBeat();
    }

    public void sendHeartBeat() {
        try {
            this.startClient("localhost", 7777, helper.buildHeartBeat(this.address, this.port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
