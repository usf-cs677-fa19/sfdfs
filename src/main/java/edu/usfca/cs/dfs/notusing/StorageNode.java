//package edu.usfca.cs.dfs;
//
//import edu.usfca.cs.dfs.init.Init;
//import edu.usfca.cs.dfs.net.ServerMessageRouter;
//import edu.usfca.cs.dfs.notusing.StorageNodeClient;
//
//import java.io.IOException;
//
//public class StorageNode {
//
//    ServerMessageRouter messageRouter;
//    StorageNodeClient client ;
//
////    // string is StroageMetaData.Id
////    private HashMap<String, Directory> folders;
////
////    private StorageMetadata metadata;
////
////    public StorageNode(String ipAddress, String port, float totalSpace) {
////        folders = new HashMap<>();
////
////        String key = ipAddress + port;
////        int id = key.hashCode();
////        metadata = new StorageMetadata(id, ipAddress,port,totalSpace);
////    }
//
//    public void start(String nodeType, int port)
//            throws IOException {
//        messageRouter = new ServerMessageRouter(nodeType);
//        messageRouter.listen(port);
//        System.out.println("Listening for connections on port : "+port);
//    }
//
//    public static void main(String[] args)
//            throws IOException {
//        StorageNode s = new StorageNode();
//        if (Init.isCorrectArgs(args)) {
//            s.start(args[0], Integer.parseInt(args[1]));
//            StorageNodeClient client = new StorageNodeClient();
//            //client.startClient();
//
//        } else {
//            System.out.println("Incorrect arguments, enter nodetype and port");
//            System.exit(1);
//        }
//
//    }
//
//
//
//}
