package edu.usfca.cs.dfs;

import edu.usfca.cs.dfs.net.ServerMessageRouter;

import java.io.IOException;

public class ControllerNode {

    ServerMessageRouter messageRouter;

    public ControllerNode() { }


    public void start()
            throws IOException {
        messageRouter = new ServerMessageRouter("controller");
        messageRouter.listen(7777);
        System.out.println("Listening for connections on port 7777");
    }

    public static void main(String[] args)
            throws IOException {
        ControllerNode c = new ControllerNode();
        c.start();
    }
}
