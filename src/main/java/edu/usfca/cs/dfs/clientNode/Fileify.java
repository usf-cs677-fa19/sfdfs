package edu.usfca.cs.dfs.clientNode;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

public class Fileify {


    /**
     * readFileInChunks reads a file in chunks of byte array, writes to channel and adds to list of Channel Futures
     * @param filename
     * @param chan
     * @param writes
     * @param chunkSize
     * @return
     * @throws IOException
     */
    public static void transferFileInChunks(String filename, Channel chan, List<ChannelFuture> writes, int chunkSize)
            throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(chunkSize);

        try(
                RandomAccessFile reader = new RandomAccessFile(filename,"r");
                FileChannel fc = reader.getChannel();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            System.out.println("File Transfer started");

            while((fc.read(buf))>0) {

                out.write(buf.array(),0,buf.position());

                PrefixedMessage msg = new PrefixedMessage(out.toByteArray());
                //PrefixedMessage msg = new PrefixedMessage(buf.array());

                //System.out.println("Msg this round : \n"+new String(msg.payload()));
                writes.add(chan.write(msg));

                buf.clear();
                out.reset();
                chan.flush();
            }

//            chan.flush();
            for (ChannelFuture write : writes) {
                write.syncUninterruptibly();
            }

        }
        System.out.println("File Transfer completed");

    }







} //closing class Fileify