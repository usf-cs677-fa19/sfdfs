package edu.usfca.cs.dfs;

import edu.usfca.cs.dfs.net.ServerMessageRouter;
import edu.usfca.cs.dfs.storageNode.StorageNodeClient;

import java.io.IOException;

public class StorageNode {

    ServerMessageRouter messageRouter;
    StorageNodeClient client ;

//    // string is StroageMetaData.Id
//    private HashMap<String, Directory> folders;
//
//    private StorageMetadata metadata;
//
//    public StorageNode(String ipAddress, String port, float totalSpace) {
//        folders = new HashMap<>();
//
//        String key = ipAddress + port;
//        int id = key.hashCode();
//        metadata = new StorageMetadata(id, ipAddress,port,totalSpace);
//    }

    public void start(String nodeType, int port)
            throws IOException {
        messageRouter = new ServerMessageRouter(nodeType);
        messageRouter.listen(port);
        System.out.println("Listening for connections on port : "+port);
    }

    public static void main(String[] args)
            throws IOException {
        StorageNode s = new StorageNode();
        s.start();
        StorageNodeClient client = new StorageNodeClient();
        client.startClient();
    }



}
