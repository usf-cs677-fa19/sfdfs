package edu.usfca.cs.dfs.net;

import edu.usfca.cs.dfs.StorageMessages;
import edu.usfca.cs.dfs.net.MessagePipeline;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MessageSender {

    public Logger logger = Logger.getLogger(MessageSender.class.getName());

    public ChannelFuture send(boolean waitForCtxToClose, String nodeType,
                              String connectingIpAddress, int connectingPort,
                              StorageMessages.StorageMessageWrapper msgWrapper)
            throws InterruptedException {

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        MessagePipeline pipeline = new MessagePipeline(nodeType); // nodeType decides which inboundhandler will be used

        Bootstrap bootstrap = new Bootstrap()
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(pipeline);

        ChannelFuture cf = bootstrap.connect(connectingIpAddress, connectingPort);// connecting info
        cf.syncUninterruptibly();

        Channel chan = cf.channel();
        ChannelFuture write = chan.write(msgWrapper);
        chan.flush();

        if(waitForCtxToClose) {
            chan.closeFuture().sync(); /* Don't quit until responding context is closed */
        }

        write.syncUninterruptibly();

        if(!waitForCtxToClose) {
            this.shutDownEventLoopGroup(workerGroup); /* Quit as soon as message is sent */
        }

        return write;

    }

    private void shutDownEventLoopGroup(EventLoopGroup workerGroup) {
        logger.log(Level.INFO,"Shutting down client");
        workerGroup.shutdownGracefully();
    }
}
