package edu.usfca.cs.dfs;

import edu.usfca.cs.dfs.data.StorageMetadata;
import edu.usfca.cs.dfs.filter.MBloomFilter;
import edu.usfca.cs.dfs.net.ServerMessageRouter;

import java.io.IOException;
import java.util.HashMap;

public class Controller {

    ServerMessageRouter messageRouter;
    // string is StorageMetadata.id
    HashMap<String,MBloomFilter> blooms;
    HashMap<String,StorageMetadata> storageMetadata;


    public Controller() {
        this.blooms = new HashMap<>();
        this.storageMetadata = new HashMap<>();
    }

    public void start()
            throws IOException {
        messageRouter = new ServerMessageRouter();
        messageRouter.listen(7777);
        System.out.println("Listening for connections on port 7777");
    }

    public static void main(String[] args)
            throws IOException {
        Server s = new Server();
        s.start();
    }
}
