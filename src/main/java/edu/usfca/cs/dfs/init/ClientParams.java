package edu.usfca.cs.dfs.init;

import com.google.gson.Gson;

public class ClientParams {

   private static ClientParams params;

    private String nodeType;
    private String nodeId;
    private String connectingAddress;
    private int connectingPort;
    private int generalChunkSize;
    private int noOfThreads;

    public ClientParams() {
    }

    public static void buildClientParams(String filename) {
        params = (new Gson().fromJson(Init.readUsingFileChannel(filename,4096), ClientParams.class));
    }

    public static String getNodeType() {
        return params.nodeType;
    }

    public static  String getConnectingAddress() {
        return params.connectingAddress;
    }

    public static int getConnectingPort() {
        return params.connectingPort;
    }

    public static int getGeneralChunkSize() {
        return params.generalChunkSize;
    }

    public static int getNoOfThreads() {
        return params.noOfThreads;
    }

    public static String getNodeId() {
        return params.nodeId;
    }
}
