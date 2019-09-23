package edu.usfca.cs.dfs.fileUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class Fileify {


    /**
     * readUsingFileChannel using RandomAccessFile and ByteArrayOutputStream
     * @param filename
     * @return file content in string
     */
    public static String readUsingFileChannel(String filename, int bufferSize) {
        try(
                RandomAccessFile reader = new RandomAccessFile(filename, "r");
                FileChannel fc = reader.getChannel();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            //int bufferSize = 4096;
            if (bufferSize > fc.size()) {
                bufferSize = (int)fc.size();
            }
            ByteBuffer buff = ByteBuffer.allocate(bufferSize);
            while (fc.read(buff) > 0) {
                out.write(buff.array(),0,buff.position());
                buff.clear();
            }

            String fileContent = new String(out.toByteArray(), StandardCharsets.UTF_8);
            return fileContent;

        } catch (FileNotFoundException e) {
            System.out.println("Exception in readUsingFileChannel - FileNotFoundException : "+e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Exception in readUsingFileChannel - IOException : "+e.getMessage());
            e.printStackTrace();
        }
        return "";
    }






} //closing class Fileify