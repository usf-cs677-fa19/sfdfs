package edu.usfca.cs.dfs.controllerNode.data;

import edu.usfca.cs.dfs.filter.BloomFilter;
import edu.usfca.cs.dfs.init.Init;

import java.time.Instant;
import java.util.Map;
import java.util.Scanner;

//based on heartbeat recv'ed from storage nodes
public class StorageNodeDetail {

    private final String ipAddress;
    private final String port;
    private String spaceRemainingMB;
    private Instant timeStamp;
    private BloomFilter bloom;

    public StorageNodeDetail(String ipAddress, String port, String spaceRemainingMB, Instant timeStamp) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.spaceRemainingMB = spaceRemainingMB;
        this.timeStamp = timeStamp;
        this.bloom = this.InitBloom();
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

    public void setSpaceRemainingMB(String spaceRemainingMB) {
        this.spaceRemainingMB = spaceRemainingMB;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public BloomFilter getBloomFilter() {
        return bloom;
    }

    private BloomFilter InitBloom() {
        Map<String, String> c = Init.readConfigFileIntoMap("configFilter.json");
        System.out.println(c);

        int filterSize = Integer.parseInt(c.get("filterSize"));
        int hashes = Integer.parseInt(c.get("hashes"));

        return new BloomFilter(filterSize, hashes);

    }
}
