package edu.usfca.cs.dfs.fileUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Zipper {

//    public static byte[] compress(byte[] uncompressedData){
//
//        byte[] compressedData = new byte[]{};
//       try(
//               ByteArrayOutputStream baos = new ByteArrayOutputStream();     //Creates a new byte array output stream, with a buffer capacity of the specified size, in bytes.
//               GZIPOutputStream gos = new GZIPOutputStream(baos) //writes array of bytes to the compressed output stream. This method will block until all the bytes are written.
//       )
//       {
//
//           gos.write(uncompressedData);
//           gos.finish();
//
//           compressedData =  baos.toByteArray();
//
//
////            gzipOutputStream.write(uncompressedData);
////            gzipOutputStream.finish();
////            compressedData = byteArrayOutputStream.toByteArray();
//       }catch (IOException e){
//           e.printStackTrace();
//       }
//        return compressedData;
//    }
//
//
//    public static byte[] decompress(final byte[] compressed) throws IOException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//        if ((compressed == null) || (compressed.length == 0)) {
//            return "".getBytes();
//        }
//        if (isCompressed(compressed)) {
//            final GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
//            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis));
//            System.out.println("-> uncompressing");
//
////            String line;
////            while ((line = bufferedReader.readLine()) != null) {
////                //outStr.append(line);
////                baos.write(line.getBytes());
////            }
//            int r;
//            while ((r = bufferedReader.read()) != -1) {
//                baos.write(r);
//            }
//        } else {
//            //outStr.append(compressed);
//            baos.write(compressed);
//
//        }
//        //return outStr.toString();
//        return baos.toByteArray();
//    }
//
//
//    public static boolean isCompressed(final byte[] compressed) {
//        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
//    }

    //

    public static byte[] compress(byte[] chunk) {

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream(chunk.length);
            try {
                GZIPOutputStream gzipOS = new GZIPOutputStream(byteStream);
                try {
                    gzipOS.write(chunk);
                } finally {
                    gzipOS.close();
                }
            } catch (IOException e) {
                System.out.println("FATAL: Compression failed to perform!!");
                e.getMessage();
            } finally {
                try {
                    byteStream.close();
                } catch (IOException e) {
                    System.out.println("FATAL: Compression failed to perform in final loop!!");
                    e.getMessage();
                }
            }
            byte[] compressedChunk = byteStream.toByteArray();
            return compressedChunk;

    }


    public static byte[] decompress (byte[] chunk) {

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(chunk);
             GZIPInputStream gis = new GZIPInputStream(byteArrayInputStream)) {
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int length;
            while ((length = gis.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            gis.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            System.out.println("FATAL: Decompression failed to perform in final loop!!");
            e.getMessage();
        }

        return chunk;
    }


    public static void main(String[] args) throws IOException {
        String string = "Hey there this is a string that should be compressed\ni hope it gets compressed and decompressed properly!!!";

        byte[] compressedData = Zipper.compress(string.getBytes());

        System.out.println("This is compressed data : "+compressedData.toString()+"\n Of length original String :"+string.length());

        System.out.println("Length of compressed data : "+compressedData.length);

        String uncompresedData = new String(Zipper.decompress(compressedData));
        System.out.println("This is uncompressed data"+uncompresedData+"\nOf length :"+uncompresedData.length());
    }
}
