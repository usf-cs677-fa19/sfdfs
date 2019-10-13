package edu.usfca.cs.dfs.storageNode;

import edu.usfca.cs.dfs.data.NodeId;
import edu.usfca.cs.dfs.fileUtil.Fileify;
import edu.usfca.cs.dfs.init.ClientParams;
import edu.usfca.cs.dfs.init.ConfigSystemParam;
import edu.usfca.cs.dfs.storageNode.data.ChunkFileMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class StorageNodeDS {

    private static StorageNodeDS storageNodeDS = null;

    private String nodeId;
    private String ipAddress;
    private int port;
    private String controllerIpAddress;
    private int controllerPort;
    private String basePath;

    private int requestFivePerSecond;
    private int freeSpace;


    private Map<String, ChunkFileMeta> chunksMetaInfo;// = new HashMap<>();

    private StorageNodeDS(){
    }

    public StorageNodeDS(ConfigSystemParam nodeParam) {
        this.ipAddress = nodeParam.getAddress();
        this.port = nodeParam.getPort();
        this.nodeId = NodeId.getId(this.ipAddress, this.port);
        this.controllerIpAddress = nodeParam.getControllerAddress();
        this.controllerPort = nodeParam.getControllerPort();

        this.basePath = System.getProperty("user.home")+"/sfdfs_"+ NodeId.getId(this.ipAddress, this.port)+"/";
        Fileify.createDirectory(basePath);

        this.chunksMetaInfo = new HashMap<>();

    }

    public static void setInstance(ConfigSystemParam nodeParam) {
        if(storageNodeDS == null){
            storageNodeDS =  new StorageNodeDS(nodeParam);
        }
    }

    public static StorageNodeDS getInstance(){
        if(storageNodeDS == null){
            storageNodeDS =  new StorageNodeDS();
        }
        return storageNodeDS;
    }


    public String getIpAddress() {
        return ipAddress;
    }


    public String getNodeId() {
        return nodeId;
    }

    public int getPort() {
        return port;
    }

    public String getControllerIpAddress() {
        return controllerIpAddress;
    }

    public int getControllerPort() {
        return controllerPort;
    }

    public String getBasePath() {
        return basePath;
    }

    public int getFreeSpace() {
        return freeSpace;
    }

    public int getRequestFivePerSecond() {
        return requestFivePerSecond;
    }

    public void setRequestFivePerSecond(int requestFivePerSecond) {
        this.requestFivePerSecond = requestFivePerSecond;
    }

    //Map<NameOfDirectoriesHavingChunks, Map<ChunkFileName,ChunkMetadata>
    public Map<String,ChunkFileMeta> getChunksMetaInfo(){
        return this.chunksMetaInfo;
    }

    public ChunkFileMeta getChunkMetaInfo(String chunkFileName){
        ChunkFileMeta chunkFileMeta = null;
            if (chunksMetaInfo.containsKey(chunkFileName)) {
                chunkFileMeta = chunksMetaInfo.get(chunkFileName);
            }
        return chunkFileMeta;
    }

    ////

    public void keepSendingHeartBeat() {
        Timer timer = new Timer();
        timer.schedule(
                new HeartBeatSender(
                        StorageNodeDS.getInstance().getControllerIpAddress(),
                        StorageNodeDS.getInstance().getControllerPort(),
                        StorageStorageMessagesHelper.buildHeartBeat(StorageNodeDS.getInstance().getIpAddress(),StorageNodeDS.getInstance().getPort())),
                0,
                5000);
    }




}
