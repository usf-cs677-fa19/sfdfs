package edu.usfca.cs.dfs.notusing;

import edu.usfca.cs.dfs.StorageMessages;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.*;

public class ControllerClient {

    public static void runControllerClient(String host, int port,StorageMessages.StorageMessageWrapper msgWrapper){

        System.out.println("Running controller client!!!!!");
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ControllerClientHandler());
                }
            });

            ChannelFuture cf = b.connect(host, port);
            cf.syncUninterruptibly();

            Channel chan = cf.channel();
            ChannelFuture write = chan.write(msgWrapper);
            chan.flush();

            chan.closeFuture().sync();

            write.syncUninterruptibly();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
