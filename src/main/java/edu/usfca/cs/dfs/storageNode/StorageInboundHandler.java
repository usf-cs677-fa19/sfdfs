package edu.usfca.cs.dfs.storageNode;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.net.InboundHandler;
import io.netty.channel.ChannelHandlerContext;

public class StorageInboundHandler extends InboundHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, StorageMessages.StorageMessageWrapper msg) {

        if(msg.hasStoreChunkMsg()) {

            // 1. create a directory, where directory name is 1st storage node in storageNodeIds field
            // 2. read the bytes in data field - getData()
            // 3. check for shanon entropy
            // 4. if entropy is low - compress using gzip
            // 5. calculate checksum of compressed or uncompressed data in byte[]
            // 6.

        }

    }
}
