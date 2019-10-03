package edu.usfca.cs.dfs.net;

import java.io.IOException;

public class Server {

    ServerMessageRouter messageRouter;

    public Server() { }

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
