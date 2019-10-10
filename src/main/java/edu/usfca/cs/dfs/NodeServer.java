package edu.usfca.cs.dfs;

import edu.usfca.cs.dfs.controllerNode.ControllerNodeHelper;
import edu.usfca.cs.dfs.init.ConfigSystemParam;
import edu.usfca.cs.dfs.net.ServerMessageRouter;
import edu.usfca.cs.dfs.nodes.NodeClient;
import edu.usfca.cs.dfs.storageNode.StorageNodeClient;

import java.io.IOException;

public class NodeServer {

    private ServerMessageRouter messageRouter;
    private NodeClient client;
//    private StorageNodeClient client ;
//    private ControllerNodeClient cclient;

    private ConfigSystemParam nodeParam;

    public NodeServer(ConfigSystemParam nodeParam) {
        this.nodeParam = nodeParam;
    }

    private void start(String nodeType, int port)
            throws IOException {
        messageRouter = new ServerMessageRouter(nodeType);
        messageRouter.listen(port);
        System.out.println("Listening for connections on port : "+port);
    }

    public void run()
            throws IOException {

        this.start(nodeParam.getNodeType(), nodeParam.getPort());

        if(nodeParam.getNodeType().equals("storage")) { // if storage  node
            this.client = new StorageNodeClient(nodeParam.getNodeType(), nodeParam.getAddress(), nodeParam.getPort());

        } else if(nodeParam.getNodeType().equals("controller")) {

            ControllerNodeHelper.checkAliveStorageNodes();
            //ControllerDS controllerDS = new ControllerDS();
            //ControllerNodeHelper.getControllerNodeHelper();
            //new ControllerNodeHelper(/*controllerDS*/);

        }

    }
}
