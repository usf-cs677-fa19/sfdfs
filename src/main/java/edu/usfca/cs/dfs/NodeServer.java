package edu.usfca.cs.dfs;

import edu.usfca.cs.dfs.controllerNode.ControllerNodeHelper;
import edu.usfca.cs.dfs.init.ConfigSystemParam;
import edu.usfca.cs.dfs.net.ServerMessageRouter;
import edu.usfca.cs.dfs.nodes.SfdfsNode;
import edu.usfca.cs.dfs.storageNode.StorageNode;

import java.io.IOException;

public class NodeServer {

    private ServerMessageRouter messageRouter;
    private SfdfsNode node;
//    private StorageNodeClient client ;
//    private ControllerNodeClient cclient;

    private ConfigSystemParam nodeParam;

    public NodeServer(ConfigSystemParam nodeParam) {
        this.nodeParam = nodeParam;
    }

    private void start(ConfigSystemParam nodeParam/*String nodeType, String ipAddress, int port,*/ )
            throws IOException {
        messageRouter = new ServerMessageRouter(nodeParam.getNodeType()); // nodeType = storage or controller or client
        messageRouter.listen(nodeParam.getAddress(), nodeParam.getPort()); // ipAddress and port

        System.out.println(nodeParam.toString());
        System.out.println("Listening for connections on address : "+nodeParam.getAddress()+":"+nodeParam.getPort());


        if(nodeParam.getNodeType().equals("storage")) { // if storage  node
            new StorageNode(nodeParam.getNodeType(), nodeParam.getAddress(), nodeParam.getPort(), nodeParam.getGeneralChunkSize());

        } else if(nodeParam.getNodeType().equals("controller")) { // if controller  node
            ControllerNodeHelper.checkAliveStorageNodes();

        }

    }

    public void run() throws IOException {
        this.start(nodeParam);
        //this.start(nodeParam.getNodeType(), nodeParam.getPort());

    }
}
