package edu.usfca.cs.dfs.data;

public class HeartBeat {

    private String ipAddress;
    private String port;
    private String spaceRemaining;

    public HeartBeat(String ipAddress,String port,String spaceRemainings){
        this.ipAddress = ipAddress;
        this.port = port;
        this.spaceRemaining = spaceRemainings;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSpaceRemaining() {
        return spaceRemaining;
    }

    public void setSpaceRemaining(String spaceRemaining) {
        this.spaceRemaining = spaceRemaining;
    }
}
