package edu.usfca.cs.dfs.storageNode;

import edu.usfca.cs.dfs.data.HeartBeat;
import io.netty.channel.ChannelFuture;

import java.util.Timer;

public class StorageNodeClientHelper {

    public void startSendingHeartBeat(ChannelFuture cf) {
        for(;;) {
            Timer timer = new Timer();
            HeartBeat beat = new HeartBeat("127.0.0.1", "20000", "1024");
            //timer.schedule();


        }
    }
}
