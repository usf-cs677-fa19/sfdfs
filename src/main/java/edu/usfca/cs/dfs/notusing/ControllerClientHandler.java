package edu.usfca.cs.dfs.notusing;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.net.InboundHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.EventExecutorGroup;

public class ControllerClientHandler extends InboundHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
                             StorageMessages.StorageMessageWrapper msg) {
        if(msg.isInitialized()){
            System.out.println("Recieved Msg from Storage Node!!!");
        }
    }
}
