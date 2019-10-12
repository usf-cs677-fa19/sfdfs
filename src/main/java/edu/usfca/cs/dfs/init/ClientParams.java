package edu.usfca.cs.dfs.init;

import com.google.gson.Gson;

public class ClientParams {

   private static ClientParams params;

    private String nodeType;
    private String connectingAddress;
    private int connectingPort;
    private int generalChunkSize;

//    private ClientParams(String nodeType, String connectingAddress, int connectingPort, int generalChunkSize) {
//        this.nodeType = nodeType;
//        this.connectingAddress = connectingAddress;
//        this.connectingPort = connectingPort;
//        this.generalChunkSize = generalChunkSize;
//    }
    public ClientParams() {

    }

    /**
     * Reads the configSystem.json file into ArrayList<ConfigSystemParam> params
     * @param filename
     */
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
}
