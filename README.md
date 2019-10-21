# Project 1: DFS With Probabilistic Routing Version SFDFS

## Controller
Controller recieves the heartbeat from the storage nodes and keeps track of the storage nodes that are alive 

message HeartBeat{
    string ipAddress = 1;
    string port = 2;
    int64 spaceRemaining = 3;
    int64 requestProcessed = 4;
    int64 retrievalProcessed = 5;
}

The heartbeat contains the identification of the storage node i.e. the IPAddress and Port, and the statistics for spaceRemaining, requestProcessed and retrievalProcessed.

Controller recieves requests from client to retrieve or save a file. 
Save : In case of saving the file is broken into chunks by the client and sent to the controller with the file Metadata.
message ChunkMeta {
    string fileName = 1;
    int32 chunkId = 2;
    int32 chunkSize = 3;
    int32 totalChunks = 4;
    repeated string storageNodeIds = 5;
}
The Controller replies with list of storage nodes containing the primary and 2 replicas for each chunk using the bloomfilters.

Retrieval: The Controller recieves a request from the client to retrieve a file 
message RetrieveFile {
    string fileName = 1;
}

The controller prepares a check meta message for the first chunk and asks the storage node (probabilistically) containing the chunk for the metadata of the file.
message RetrieveChunkMeta{
    string fileChunkId = 1;
}

When the controller receives the metadata, the controller gets the mapping for chunkId to StorageNodes and sends to the client for retrieval
message MappingChunkIdToStorageNodes{
    map<string,StorageNodesHavingChunk> mapping = 1;
}



## Storage Nodes 


## Client


## Storing Chunks


## Retrieval 


## Fault Tolerance
















See the project spec here: https://www.cs.usfca.edu/~mmalensek/cs677/assignments/project-1.html

