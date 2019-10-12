package edu.usfca.cs.dfs.controllerNode;

import edu.usfca.cs.dfs.Client;
import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.data.ChunkMetaPOJO;
import edu.usfca.cs.dfs.data.FileChunkId;
import edu.usfca.cs.dfs.data.NodeId;
import edu.usfca.cs.dfs.net.InboundHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.concurrent.Future;

public class ControllerInboundHandler extends InboundHandler {

    @Override
    public void channelRead0(
            ChannelHandlerContext ctx,
            StorageMessages.StorageMessageWrapper msg) {

        if(msg.hasHeartBeat()) {  // if message is a heartbeat
            this.recvHeartBeat(msg);

        }else if(msg.hasRetrieveFileMsg()){ // controller receieving a:  RetrieveFile message from client
            // should return file containing mapping of each chunk to storage node //Map<ChunkName,StorageNode>
            System.out.println("Request from client to retrieve file!!!");
            String filename = msg.getRetrieveFileMsg().getFileName();
            System.out.println("File name to retrieve : "+filename);
            //get list of storage nodes from bloomFilter, for chunk 1
            ArrayList<String> storageNodes = ControllerNodeHelper.getStorageNodeFromBloomFiltersForChunk(filename,1);

            System.out.println("Storage Nodes containing this chunk : "+storageNodes);
            // for First node in the list Send RetrieveChunkMeta To Storage
            if(storageNodes.size() == 0){
                System.out.println("No Storage Nodes have the file!!!");
            } else {
                String fileChunkId = FileChunkId.getFileChunkId(filename,1);
                sendRetrieveChunkMetaToStorage(ctx, storageNodes.get(0), fileChunkId);
            }
        } else if(msg.hasChunkMetaMsg()){ //
            ///
            System.out.println("Received chunkMetaMsg from client");

            StorageMessages.ChunkMeta receivedChunkMetaMsg = msg.getChunkMetaMsg();

            ChunkMetaPOJO cm = new ChunkMetaPOJO()
                    .setFilename(receivedChunkMetaMsg.getFileName())
                    .setChunkId(receivedChunkMetaMsg.getChunkId())
                    .setChunkSize(receivedChunkMetaMsg.getChunkSize())
                    .setTotalChunks(receivedChunkMetaMsg.getTotalChunks());

            String[] storageNodesAssigned = this.getStorageNodesForChunkMeta(cm);

            String primaryNode = storageNodesAssigned[0];
           // if (storageNodesAssigned.length > 0) {
            ArrayList<Boolean> chunksStoredInBloomFilters = new ArrayList<>();
            //store the details in bloomfilter

                Boolean stored = ControllerDS.getInstance().storeChunkInBloomFilter(primaryNode,receivedChunkMetaMsg.getFileName(),receivedChunkMetaMsg.getChunkId());
                if(stored) {
                    chunksStoredInBloomFilters.add(stored);
                }

            if (chunksStoredInBloomFilters.size() == 1){
                System.out.println("Bloomfilters filled successfully!!!");
            }

            cm.setStorageNodeIds(storageNodesAssigned);
           // }
                StorageMessages.StorageMessageWrapper msgWrapper = ControllerStorageMessagesHelper.buildChunkMeta(cm); //this.buildChunkMeta(cm);

                Channel chan = ctx.channel();
                ChannelFuture future = chan.write(msgWrapper);
                chan.flush();
//            //future.addListener(ChannelFutureListener.CLOSE);

            //todo here - handle context
//
                System.out.println("Sent chunkMetaMsg with list of storage nodes back to  client");

            ctx.close();


        }else if(msg.hasStorageChunkMeta()){
            System.out.println("\n\n\n\n Controller recieved the meta for first chunk from Storage Node!!!!!");
        }
        else {
            StorageMessages.StoreChunk storeChunkMsg
                    = msg.getStoreChunkMsg();
            System.out.println("Storing file name: "
                    + storeChunkMsg.getFileName());
        }

    }

//    private StorageMessages.StorageMessageWrapper buildChunkMeta(ChunkMeta cm) {
//        StorageMessages.ChunkMeta chunkMetaMsg
//                = StorageMessages.ChunkMeta.newBuilder()
//                .setFileName(cm.getFilename())
//                .setChunkId(cm.getChunkId())
//                .setChunkSize(cm.getChunkSize())
//                .setTotalChunks(cm.getTotalChunks())
//                .addAllStorageNodeIds(Arrays.asList(cm.getStorageNodeIds()))
//                .build();
//
//            StorageMessages.StorageMessageWrapper msgWrapper =
//                    StorageMessages.StorageMessageWrapper.newBuilder()
//                            .setChunkMetaMsg(chunkMetaMsg)
//                            .build();
//
//            return msgWrapper;
//
//    }

    private String[] getStorageNodesForChunkMeta(ChunkMetaPOJO cm) {
        String[] arr = null;
        ArrayList<String> threeNodes = ControllerNodeHelper.getThreeNodes(cm);
        arr = threeNodes.toArray(new String[threeNodes.size()]);
        return arr;
    }


    private void recvHeartBeat(StorageMessages.StorageMessageWrapper msg) {
        ControllerNodeHelper.recvHeartBeat(msg);
    }


    public static void sendRetrieveChunkMetaToStorage(ChannelHandlerContext recveivedCtx, String storageNode,String fileChunkId ){

        System.out.println("Trying to connect to the primary storage Node");
        String[] connectingInfo = NodeId.getIPAndPort(storageNode);
        String connectingAddress = connectingInfo[0];
        int connectingPort = Integer.parseInt(connectingInfo[1]);

        System.out.println("Connecting Address : "+connectingAddress);

        System.out.println("Connecting Port : "+connectingPort);
        // to be send to storage nodce and recev chunkMeta from storage
        StorageMessages.StorageMessageWrapper msgWrapper =
                ControllerStorageMessagesHelper.buildretrieveChunkMeta(fileChunkId);

        //contact the storage node to get the metadata of the First Chunk

        try {
            new Client().runClient(true,"controller",connectingAddress,connectingPort,msgWrapper);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
      //  ControllerClient.runControllerClient(connectingAddress,connectingPort, msgWrapper);  not using todo remove this

    }

}
