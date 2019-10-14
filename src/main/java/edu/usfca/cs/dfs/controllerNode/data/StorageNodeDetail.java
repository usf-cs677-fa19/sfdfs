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
    private long spaceRemaining;
    private long requestProcessed;
    private long retrievalProcessed;
    private Instant timeStamp;
    private BloomFilter bloom;

    public StorageNodeDetail(String ipAddress, String port, long spaceRemaining, long requestProcessed, long retrievalProcessed, Instant timeStamp) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.spaceRemaining = spaceRemaining;
        this.requestProcessed = requestProcessed;
        this.retrievalProcessed = retrievalProcessed;
        this.timeStamp = timeStamp;
        this.bloom = this.InitBloom();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPort() {
        return port;
    }

    public long getSpaceRemaining() {
        return spaceRemaining;
    }

    public long getRequestProcessed() {
        return requestProcessed;
    }

    public long getRetrievalProcessed() {
        return retrievalProcessed;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setSpaceRemaining(long spaceRemaining) {
        this.spaceRemaining = spaceRemaining;
    }

    public void setRequestProcessed(long requestProcessed) {
        this.requestProcessed = requestProcessed;
    }

    public void setRetrievalProcessed(long retrievalProcessed) {
        this.retrievalProcessed = retrievalProcessed;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public BloomFilter getBloomFilter() {
        return bloom;
    }

    public void storeInBloomFilter(String chunkName){
        this.bloom.put(chunkName);
    }

    private BloomFilter InitBloom() {
        Map<String, String> c = Init.readConfigFileIntoMap("configFilter.json");
        System.out.println(c);

        int filterSize = Integer.parseInt(c.get("filterSize"));
        int hashes = Integer.parseInt(c.get("hashes"));

        return new BloomFilter(filterSize, hashes);

    }
}
