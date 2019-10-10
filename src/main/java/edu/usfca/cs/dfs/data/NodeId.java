package edu.usfca.cs.dfs.data;

public class NodeId {

//    String Id;
//
//    public NodeId(String ipAdd, int port) {
//        this.Id = ipAdd+"-"+port;
//    }

//    public NodeId(String ipAdd, String port) {
//        this.Id = ipAdd+"-"+port;
//    }

    public static String getId(String ipAdd, String port) {
        return ipAdd+"-"+port;
    }

    public static String getId(String ipAdd, int port) {
        return ipAdd+"-"+port;
    }
}
