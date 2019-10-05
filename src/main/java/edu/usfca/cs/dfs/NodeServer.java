package edu.usfca.cs.dfs;

import edu.usfca.cs.dfs.init.ConfigSystemParam;
import edu.usfca.cs.dfs.net.ServerMessageRouter;

import java.io.IOException;

public class NodeServer {

    ServerMessageRouter messageRouter;
    NodeClient client ;

    ConfigSystemParam nodeParam;

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

            this.client = new NodeClient(nodeParam.getNodeType(), nodeParam.getAddress(), nodeParam.getPort());
    }



}
