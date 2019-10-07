package edu.usfca.cs.dfs.data;

public class NodeId {

    String Id;

    public NodeId(String ipAdd, int port) {
        this.Id = ipAdd+":"+port;
    }

    public NodeId(String ipAdd, String port) {
        this.Id = ipAdd+":"+port;
    }

    public String getId() {
        return Id;
    }
}
