package edu.usfca.cs.dfs.init;

public class ConfigSystemParam {
    private final String nodeType;
    private final String address;
    private final int port;

    public ConfigSystemParam(String nodeType, String address, int port) {
        this.nodeType = nodeType;
        this.address = address;
        this.port = port;
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

    public String toString() {
        return "nodeType: "+this.nodeType+" ,  address: "+this.address+" ,  port: "+this.port+"\n";
    }
}
