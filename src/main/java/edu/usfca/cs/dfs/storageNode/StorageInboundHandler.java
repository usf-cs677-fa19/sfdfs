package edu.usfca.cs.dfs.storageNode;

import edu.usfca.cs.dfs.net.MessageSender;
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
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            if (msg.getStoreChunkMsg().getToStorageNodeId().equals(msg.getStoreChunkMsg().getStorageNodeIds(0))) { // in primary node
                // change toaddress in strorechunk message to 2nd replica and send to 2nd replica
                String[] sendingInfo = NodeId.getIPAndPort(msg.getStoreChunkMsg().getStorageNodeIds(1));
                try {
                    new MessageSender().send(
                            false, "storage", sendingInfo[0], Integer.parseInt(sendingInfo[1]),
                            StorageStorageMessagesHelper.prepareStoreChunkMsgForReplica(msg, 1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else if(msg.getStoreChunkMsg().getToStorageNodeId().equals(msg.getStoreChunkMsg().getStorageNodeIds(1))) { //in 1st replica
                // change toaddress in strorechunk message to 3rd replica and send to 3rd replica
                String[] sendingInfo = NodeId.getIPAndPort(msg.getStoreChunkMsg().getStorageNodeIds(2));
                try {
                    new MessageSender().send(
                            false, "storage", sendingInfo[0], Integer.parseInt(sendingInfo[1]),
                            StorageStorageMessagesHelper.prepareStoreChunkMsgForReplica(msg, 2));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else if(msg.getStoreChunkMsg().getToStorageNodeId().equals(msg.getStoreChunkMsg().getStorageNodeIds(2))) { // in 2nd replica

            }
        }  // store chunk and send to replica if needed
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
        }
        else if(msg.hasRetrieveChunk()) {  //storage node should send chunkMsg
            System.out.println("Client asking for a file chunk");
            ByteBuffer buff;

            String fileChunkId = msg.getRetrieveChunk().getFileChunkId();
            String pathForFileChunkId = StorageNodeDS.getInstance().getBasePath()+ StorageNodeDS.getInstance().getNodeId()+ "/chunkFiles/"+ fileChunkId;
            if(Fileify.doesFileExist(pathForFileChunkId)) {
                try {

                    buff = Fileify.readToBuffer(pathForFileChunkId);

                    StorageMessages.StorageMessageWrapper msgWrapper = StorageStorageMessagesHelper.prepareChunkMsg(fileChunkId, buff);


                    Channel chan = ctx.channel();
                    ChannelFuture future = chan.write(msgWrapper);
                    chan.flush();  // sending data back to client

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(msg.hasBecomePrimary()){

            String fromIP = msg.getBecomePrimary().getForApAddress();
            String fromPort = msg.getBecomePrimary().getForPort();
            List<String> selfReplicas = msg.getBecomePrimary().getAskIdsList();

            String fromNodeId = NodeId.getId(fromIP,fromPort);

            String source = StorageNodeDS.getInstance().getBasePath()+fromNodeId;

            String destination = StorageNodeDS.getInstance().getBasePath() + StorageNodeDS.getInstance().getNodeId();

            if(checkIfSourceExists(source)){
                Fileify.copyDirectory(new File(source),new File(destination));

                //replicate the changes to the replicas

                //delete the source folder

            }else{
                //todo: if the storage node is not the replica of the node to be deleted, Should send the list of replicas of the node to be deleted to contact
            }

            //respond to the controller

        }


    }

    private boolean checkIfSourceExists(String sourcePath){
        Path path = Paths.get(sourcePath);
        return Files.exists(path);
    }

    private void sendChunksToReplica(String nodeId,String sourcePath){

        File source = new File(sourcePath);

        if(source.isDirectory()){
            File[] files = source.listFiles();
            for(File file : files) {
                String filename = file.toString();
                ByteBuffer buff = null;
                try {
                    buff = Fileify.readToBuffer(filename);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }




        }


    }



}
