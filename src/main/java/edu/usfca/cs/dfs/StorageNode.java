package edu.usfca.cs.dfs;

import edu.usfca.cs.dfs.data.StorageMetadata;
import edu.usfca.cs.dfs.storageNode.Directory;

import java.util.HashMap;

public class StorageNode {

    // string is StroageMetaData.Id
    private HashMap<String, Directory> folders;

    private StorageMetadata metadata;

    public StorageNode(String ipAddress, String port, float totalSpace) {
        folders = new HashMap<>();

        String key = ipAddress + port;
        int id = key.hashCode();
        metadata = new StorageMetadata(id, ipAddress,port,totalSpace);
    }

}
