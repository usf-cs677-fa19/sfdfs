package edu.usfca.cs.dfs.nodes;

import edu.usfca.cs.dfs.controllerNode.ControllerNodeHelper;
import edu.usfca.cs.dfs.init.ConfigSystemParam;
import edu.usfca.cs.dfs.net.ServerMessageRouter;
import edu.usfca.cs.dfs.nodes.SfdfsNode;
import edu.usfca.cs.dfs.storageNode.StorageNode;
import edu.usfca.cs.dfs.storageNode.StorageNodeDS;

import java.io.IOException;

public class NodeServer {

    private ServerMessageRouter messageRouter;
//    private SfdfsNode node;
//    private StorageNodeClient client ;
//    private ControllerNodeClient cclient;

    //private ConfigSystemParam nodeParam;

//    public NodeServer(ConfigSystemParam nodeParam) {
//        this.nodeParam = nodeParam;
//    }

    public NodeServer() {
    }

    private void start(/*ConfigSystemParam nodeParam*//*String nodeType, String ipAddress, int port,*/ )
            throws IOException {
        messageRouter = new ServerMessageRouter(ConfigSystemParam.getNodeType()); // nodeType = storage or controller or client
        messageRouter.listen(ConfigSystemParam.getAddress(), ConfigSystemParam.getPort()); // ipAddress and port

        System.out.println("Listening for connections on address : "+ConfigSystemParam.getAddress()+":"+ConfigSystemParam.getPort());


        if(ConfigSystemParam.getNodeType().equals("storage")) { // if storage  node
            StorageNodeDS.setInstance(ConfigSystemParam.getParams());
            StorageNodeDS.getInstance().keepSendingHeartBeat();

        } else if(ConfigSystemParam.getNodeType().equals("controller")) { // if controller  node
            ControllerNodeHelper.checkAliveStorageNodes();

        }

    }

    public void run() throws IOException {
        this.start(/*nodeParam*/);
        //this.start(nodeParam.getNodeType(), nodeParam.getPort());

    }
}
