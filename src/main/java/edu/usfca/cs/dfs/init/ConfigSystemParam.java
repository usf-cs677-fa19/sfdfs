package edu.usfca.cs.dfs.init;

public class ConfigSystemParam {
    private final String nodeType;
    private final String address;
    private final int port;
    private final int generalChunkSize;

    public ConfigSystemParam(String nodeType, String address, int port) {
        this.nodeType = nodeType;
        this.address = address;
        this.port = port;
        this.generalChunkSize = 0;
    }

    public ConfigSystemParam(String nodeType, String address, int port, int chunkSize) {
        this.nodeType = nodeType;
        this.address = address;
        this.port = port;
        this.generalChunkSize = chunkSize;
    }

    public String getNodeType() {
        return nodeType;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }

    public int getGeneralChunkSize() {
        return generalChunkSize;
    }

    public String toString() {
        return "System Params: \n"+
                "+nodeType: "+this.nodeType+
                " ,  address: "+this.address+
                " ,  port: "+ this.port+
                " ,  generalChunkSize: "+ this.generalChunkSize+"\n";
    }
}
