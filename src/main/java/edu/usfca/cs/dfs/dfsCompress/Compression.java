package edu.usfca.cs.dfs.dfsCompress;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Compression {

    public static byte[] gzipCompress(byte[] uncompressedData){

        byte[] compressedData = new byte[]{};
       try(
               ByteArrayOutputStream baos = new ByteArrayOutputStream();     //Creates a new byte array output stream, with a buffer capacity of the specified size, in bytes.
               GZIPOutputStream gos = new GZIPOutputStream(baos) //writes array of bytes to the compressed output stream. This method will block until all the bytes are written.
       )
       {

           gos.write(uncompressedData);
           gos.finish();

           compressedData =  baos.toByteArray();


//            gzipOutputStream.write(uncompressedData);
//            gzipOutputStream.finish();
//            compressedData = byteArrayOutputStream.toByteArray();
       }catch (IOException e){
           e.printStackTrace();
       }
        return compressedData;
    }

//    public static byte[] gzipUncompress(byte[] compressedData){
//        byte[] uncompressedData = new byte[]{};
//        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream)
////            InputStreamReader reader = new InputStreamReader(gzipInputStream);
////            BufferedReader in = new BufferedReader(reader);
//        )
//        {
//            System.out.println("Received length of compressed string : "+compressedData.length);
//            byte[] buffer = new byte[1024];
//            int length;
////            while ((length = gzipInputStream.read(buffer)) != -1)
////            {
////                byteArrayOutputStream.write(buffer,0,length);
////            }
//
//            for (int bytesRead=0; bytesRead != -1; bytesRead=gzipInputStream.read(buffer)) {
//                if(bytesRead > 0) {
//                    byteArrayOutputStream.write(buffer, 0, bytesRead);
//                }else{
//                    break;
//                }
//            }
//
//
////            for (; ; ) {
////                int bytesRead=gzipInputStream.read(buffer);
////                if (bytesRead < 0) {
////                    break;
////                }
////                byteArrayOutputStream.write(buffer,0,bytesRead);
////            }
//
////            String readed = "";
////            while ((readed = in..readLine()) != null) {
////                System.out.println(readed);
////            }
//
////            if(byteArrayOutputStream.size() > 0) {
////                uncompressedData = byteArrayOutputStream.toByteArray();
////            }
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//        return uncompressedData;
//    }


    public static byte[] decompress(final byte[] compressed) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if ((compressed == null) || (compressed.length == 0)) {
            return "".getBytes();
        }
        if (isCompressed(compressed)) {
            final GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, "UTF-8"));

//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                //outStr.append(line);
//                baos.write(line.getBytes());
//            }
            int r;
            while ((r = bufferedReader.read()) != -1) {
                baos.write(r);
            }
        } else {
            //outStr.append(compressed);
            baos.write(compressed);

        }
        //return outStr.toString();
        return baos.toByteArray();
    }

    public static boolean isCompressed(final byte[] compressed) {
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }


    public static void main(String[] args) throws IOException {
        String string = "Hey there this is a string that should be compressed\n i hope it gets compressed and decompressed properly!!!";

        byte[] compressedData = Compression.gzipCompress(string.getBytes());

        System.out.println("This is compressed data : "+compressedData.toString()+"\n Of length original String :"+string.length());

        System.out.println("Length of compressed data : "+compressedData.length);

        String uncompresedData = new String(Compression.decompress(compressedData));
        System.out.println("This is uncompressed data"+uncompresedData+"\n Of length :"+uncompresedData.length());
    }
}
