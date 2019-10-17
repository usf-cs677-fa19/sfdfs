package edu.usfca.cs.dfs.clientNode;

import edu.usfca.cs.dfs.ClientNode;
import edu.usfca.cs.dfs.net.MessageSender;
import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.data.FileChunkId;
import edu.usfca.cs.dfs.data.NodeId;
import edu.usfca.cs.dfs.fileUtil.Fileify;
import edu.usfca.cs.dfs.init.ClientParams;
import edu.usfca.cs.dfs.net.InboundHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class  ClientInboundHandler extends InboundHandler {

    public Logger logger = Logger.getLogger(ClientInboundHandler.class.getName());
    @Override
    public void channelRead0(
            ChannelHandlerContext ctx,
            StorageMessages.StorageMessageWrapper msg) {

        logger.log(Level.INFO,"IN CLIENT INBOUND HANDLER");
        ctx.close();

        if(msg.hasChunkMetaMsg()) { // msg returned from controller with storage nodes list // while client want to store chunks
            System.out.println("\nChunkMetaMsg received in CLIENT INBOUND HANDLER");
            int size = msg.getChunkMetaMsg().getStorageNodeIdsCount();
            if (size > 0) {
                System.out.println(msg.getChunkMetaMsg().getFileName()+"-"+msg.getChunkMetaMsg().getChunkId());
                for(int i = 0; i < size; i++) {
                    System.out.println(msg.getChunkMetaMsg().getStorageNodeIds(i));
                }

            } else {
                System.out.println(msg.getChunkMetaMsg().getFileName()+"-"+"NO STORAGE LIST");
            }

            this.recvChunkMetaMsg(msg);


        }
        else if(msg.hasMappingChunkIdToStorageNodesMsg()) { // client receieved cunk to sn mapping for all chunks for a file
            if(!msg.getMappingChunkIdToStorageNodesMsg().getMappingMap().isEmpty()) {
                System.out.println("\n Recieved mapping from controller : todo send the request for filechunks to all nodes!!");
                // key = chunkId, value = StorageNodeList
                Map<String, StorageMessages.StorageNodesHavingChunk> mapChunkIdToStorageNodes =
                        msg.getMappingChunkIdToStorageNodesMsg().getMappingMap();
                // key = storageNodeId, value = messageWrapperToBeSent
                ArrayList<Map<String, StorageMessages.StorageMessageWrapper>> retrieveChunkMapArray =
                        ClientStorageMessagesHelper.prepareRetrieveChunkMapArray(mapChunkIdToStorageNodes);
                ////rotate through Arraylist and send wrapper to storage nodes
                //rotate through Arraylist prepare Callables and add to callableTasks
                List<Callable<ChannelFuture>> callableTasks = new ArrayList<>();
                for(Map<String, StorageMessages.StorageMessageWrapper> retrieveChunkMap : retrieveChunkMapArray) {
                    callableTasks.add(new AskChunkThreadTask(retrieveChunkMap));
                }

                ExecutorService executor = Executors.newFixedThreadPool(3); // todo : remove hardcoding

               // String[] result = null;
                try {
                    List<Future<ChannelFuture>> result = executor.invokeAll(callableTasks);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Received empty mapping, file chunks not found");
            }

        }
        else if(msg.hasChunkMsg()) {
            ctx.close();

            System.out.println("\n Received chunkMsg from Storage Node");
            if(msg.getChunkMsg().getFound() == true) {
                Fileify.writeChunkToFile(msg.getChunkMsg());
            } else {
                System.out.println("CHUNK NOT FOUND MSG : ");
                System.out.println("Storage node Ids : ");
                for(int i =0; i<msg.getChunkMsg().getStorageNodeIdsList().size();i++) {
                    System.out.println(msg.getChunkMsg().getStorageNodeIds(i));
                }
                new AskChunkTask(ClientParams.getNodeType(), msg.getChunkMsg()).run();
                System.out.println("Chunk not found message");
            }

        }
        else if(msg.hasNoFileMsg()) {
            logger.log(Level.INFO,"File is not present in System");
        }
        else {
            logger.log(Level.INFO,"\n Donno what message receieved");
        }


//        ctx.close();  //todo from here to up
    }

    private void recvChunkMetaMsg( StorageMessages.StorageMessageWrapper msg){
        //check file name, start read position , and chunk size
        StorageMessages.ChunkMeta cmMsg = msg.getChunkMetaMsg();
        // read that much in the buffer
        ByteBuffer buffer = null;
        try {
            buffer = Fileify.readToBuffer(cmMsg, ClientParams.getGeneralChunkSize()); // filling buffer full
        } catch (IOException e) {
            e.printStackTrace();
        }
        // a. connecting info and prepare a storeChunk msg
        String[] connectingInfo = NodeId.getIPAndPort(cmMsg.getStorageNodeIds(0));
        StorageMessages.StorageMessageWrapper storeChunkMsg =
                ClientStorageMessagesHelper.prepareStoreChunkMsg(cmMsg, buffer);

        logger.log(Level.INFO,"storage nodes assigned to "
                + FileChunkId.getFileChunkId(cmMsg.getFileName(), cmMsg.getChunkId())
                +" are : "
                + storeChunkMsg.getStoreChunkMsg().getStorageNodeIdsList());

        // b. send to primary storage node
        try {
            new MessageSender().send(false, ClientParams.getNodeType(), connectingInfo[0], Integer.parseInt(connectingInfo[1]), storeChunkMsg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}
