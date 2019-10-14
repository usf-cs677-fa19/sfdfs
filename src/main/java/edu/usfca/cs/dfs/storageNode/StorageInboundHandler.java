package edu.usfca.cs.dfs.storageNode;

import edu.usfca.cs.dfs.net.Client;
import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.data.FileChunkId;
import edu.usfca.cs.dfs.data.NodeId;
import edu.usfca.cs.dfs.fileUtil.Entropy;
import edu.usfca.cs.dfs.fileUtil.Fileify;
import edu.usfca.cs.dfs.fileUtil.Zipper;
import edu.usfca.cs.dfs.net.InboundHandler;
import edu.usfca.cs.dfs.storageNode.data.ChunkFileMeta;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StorageInboundHandler extends InboundHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, StorageMessages.StorageMessageWrapper msg) {

        if(msg.hasStoreChunkMsg()) {
            System.out.println("\n**************************storage receieved store chunk *******************************\n");
            System.out.println("Size of storage node list : "+ msg.getStoreChunkMsg().getStorageNodeIdsList().size());
            // 1. create a directory, where directory name is 1st storage node in storageNodeIds field
            String nodeDir = System.getProperty("user.home")+"/sfdfs_"+ msg.getStoreChunkMsg().getToStorageNodeId()+"/"+msg.getStoreChunkMsg().getStorageNodeIds(0);
            Fileify.createDirectory(nodeDir);
            // 2. read the bytes in data field - getData()
            byte[] dataArr = msg.getStoreChunkMsg().getData().toByteArray();
            // 3. check for shanon entropy
            double entropy = Entropy.entropy(dataArr);
            // 4. if entropy is low - compress using gzip
            boolean isCompressed = false;
            if(entropy < 0.6) {
                dataArr = Zipper.compress(dataArr.clone());
                isCompressed = true;
            }
            // 5. calculate checksum of compressed or uncompressed data in byte[]
            int checksum = Arrays.hashCode(dataArr);
            // 6. prepare chunkFileMeta top be stored for particular chunkData
            ChunkFileMeta forMetaFile = new ChunkFileMeta(
                    msg.getStoreChunkMsg().getFileName(),
                    msg.getStoreChunkMsg().getChunkId(),
                    msg.getStoreChunkMsg().getChunkSize(),
                    msg.getStoreChunkMsg().getTotalChunks(),
                    msg.getStoreChunkMsg().getStorageNodeIdsList(),
                    entropy,
                    isCompressed,
                    checksum
            );
            String forMetaFileJson = forMetaFile.toJson();
            // 7. check and create 1st storageNodeId meta and chunk dir
            String nodeMetaDir = nodeDir+"/metaFiles/";
            Fileify.createDirectory(nodeMetaDir);
            String nodeChunkDir = nodeDir+"/chunkFiles/";
            Fileify.createDirectory(nodeChunkDir);
            String fileChunkId = FileChunkId.getFileChunkId(msg.getStoreChunkMsg().getFileName(),msg.getStoreChunkMsg().getChunkId());
            // 8. store chunkFileMeta in a file inside metaFiles dir
            String metaFilePath = nodeMetaDir+"/"+fileChunkId;
            //String metaFilePath = nodeMetaDir+"/"+msg.getStoreChunkMsg().getFileName()+"_chunk_"+msg.getStoreChunkMsg().getChunkId();
            boolean isMetaWritten = Fileify.writeToAFile(metaFilePath, forMetaFileJson);
            // 9. store fileChunk in a file inside chunkFiles dir
            String chunkFilePath = nodeChunkDir+"/"+fileChunkId;
            //String chunkFilePath = nodeChunkDir+"/"+msg.getStoreChunkMsg().getFileName()+"_chunk_"+msg.getStoreChunkMsg().getChunkId();
            boolean isChunkWritten = Fileify.writeToAFile(chunkFilePath, msg.getStoreChunkMsg().getData().toByteArray());
            // 10. check if everything done
            if(isMetaWritten && isChunkWritten) {
                (StorageNodeDS.getInstance().getChunksMetaInfo()).put(fileChunkId, forMetaFile);
                System.out.println("Meta and Chunk saved on Storage node :->");
            } else {
                System.out.println("Something went wrong in Meta and Chunk saved on Storage node :-<");
            }

            ctx.close();
            //
            // forwarding storeChunk to other replica
            //
            if (msg.getStoreChunkMsg().getStorageNodeIdsList().size()>0 &&
                    msg.getStoreChunkMsg().getToStorageNodeId().equals(msg.getStoreChunkMsg().getStorageNodeIds(0))) { // in primary node
                // change toaddress in strorechunk message to 2nd replica and send to 2nd replica
                String[] sendingInfo = NodeId.getIPAndPort(msg.getStoreChunkMsg().getStorageNodeIds(1));
                try {
                    new Client().runClient(
                            false, "storage", sendingInfo[0], Integer.parseInt(sendingInfo[1]),
                            StorageStorageMessagesHelper.prepareStoreChunkMsgForReplica(msg, 1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else if(msg.getStoreChunkMsg().getStorageNodeIdsList().size()>1 &&
                    msg.getStoreChunkMsg().getToStorageNodeId().equals(msg.getStoreChunkMsg().getStorageNodeIds(1))) { //in 1st replica
                // change toaddress in strorechunk message to 3rd replica and send to 3rd replica
                String[] sendingInfo = NodeId.getIPAndPort(msg.getStoreChunkMsg().getStorageNodeIds(2));
                try {
                    new Client().runClient(
                            false, "storage", sendingInfo[0], Integer.parseInt(sendingInfo[1]),
                            StorageStorageMessagesHelper.prepareStoreChunkMsgForReplica(msg, 2));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else if(msg.getStoreChunkMsg().getStorageNodeIdsList().size()>2 &&
                    msg.getStoreChunkMsg().getToStorageNodeId().equals(msg.getStoreChunkMsg().getStorageNodeIds(2))) { // in 2nd replica

            }
        }
        else if(msg.hasRetrieveChunkMeta()) {
            System.out.println("RetrieveChunkMeta received from controller");

            String fileChunkId = msg.getRetrieveChunkMeta().getFileChunkId();
            ChunkFileMeta chunkFileMeta = StorageNodeDS.getInstance().getChunkMetaInfo(fileChunkId);

            StorageMessages.StorageMessageWrapper msgWrapper = StorageStorageMessagesHelper.prepareChunkMetaInfo(chunkFileMeta);

            Channel chan = ctx.channel();
            ChannelFuture future = chan.write(msgWrapper);
            chan.flush();

            System.out.println("Sent chunkMetaInfo Back to the controller");
           // ctx.close();
        }else if(msg.hasBecomePrimary()){
            System.out.println("Message recieved to become primary!!!!");

            String forIP = msg.getBecomePrimary().getForApAddress();
            String port = msg.getBecomePrimary().getForPort();
            List<String> replicas = msg.getBecomePrimary().getAskIdsList();

            String nodeId = NodeId.getId(forIP,port);

            String basePath = StorageNodeDS.getInstance().getBasePath();

            String storageNodeToReplicate = basePath+nodeId;

            String selfNodeId = basePath + StorageNodeDS.getInstance().getNodeId();

            Path path = Paths.get(storageNodeToReplicate);

            if(Files.exists(path)){
                System.out.println("NodeId exists!!!");
                File dirSource = new File(storageNodeToReplicate);
                File dirDestination = new File(selfNodeId);
                Fileify.copyDirectory(dirSource,dirDestination);
                System.out.println("Copy successfull!!");
            }

        }
    }
}
