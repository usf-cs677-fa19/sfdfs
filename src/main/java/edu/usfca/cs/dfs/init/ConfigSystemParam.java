package edu.usfca.cs.dfs.init;

import com.google.gson.Gson;

public class ConfigSystemParam {

    private static ConfigSystemParam params;

    private String nodeType;
    private String address;
    private int port;
    //controller specific
    private int replication;
    //storageNodeSpecific
    private String controllerAddress;
    private int controllerPort;

    public ConfigSystemParam() {
    }

    public ConfigSystemParam(String filename) {
        params = this.buildConfigSystemParams(filename);
    }

    public static ConfigSystemParam getParams() {
        return params;
    }

    /**
     * Reads the configSystem.json file into ArrayList<ConfigSystemParam> params
     * @param filename
     */
    private ConfigSystemParam buildConfigSystemParams(String filename) {
        return (new Gson().fromJson(Init.readUsingFileChannel(filename,4096), ConfigSystemParam.class));
    }


    public static String getNodeType() {
        return params.nodeType;
    }

    public static int getPort() {
        return params.port;
    }

    public static String getAddress() {
        return params.address;
    }

    public static String getControllerAddress() {
        return params.controllerAddress;
    }

    public static int getControllerPort() {
        return params.controllerPort;
    }

    //    public int getGeneralChunkSize() {
//        return generalChunkSize;
//    }


    public static int getReplication() {
        return params.replication;
    }

    public static String getString() {
        return "System Params: \n"+
                "nodeType: "+params.nodeType+
                " ,  address: "+params.address+
                " ,  port: "+ params.port+
                "\n";
    }
}
