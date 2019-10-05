package edu.usfca.cs.dfs.controllerNode.data;

import java.time.Instant;

public class StorageNodeDetail {

    private final String ipAddress;
    private final String port;
    private final String spaceRemainingMB;
    private Instant timeStamp;

    public StorageNodeDetail(String ipAddress, String port, String spaceRemainingMB, Instant timeStamp) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.spaceRemainingMB = spaceRemainingMB;
        this.timeStamp = timeStamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPort() {
        return port;
    }

    public String getSpaceRemainingMB() {
        return spaceRemainingMB;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }


}
