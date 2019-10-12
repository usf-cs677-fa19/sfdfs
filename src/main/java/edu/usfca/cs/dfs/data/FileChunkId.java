package edu.usfca.cs.dfs.data;

public class FileChunkId {

    public static String getFileChunkId(String filename,String chunkNumber) {
        return filename+"_Chunk_"+chunkNumber;
    }

    public static String getFileChunkId(String filename,int chunkNumber) {
        return filename+"_Chunk_"+chunkNumber;
    }

    public static String[] getFileChunkIds(String filename,int initialChunkNumber, int finalChunkNumber) {
        String[] chunkIDs = new String[finalChunkNumber - initialChunkNumber];
        for(int i=initialChunkNumber; i<=finalChunkNumber; i++) {
            chunkIDs[i] = getFileChunkId(filename, i);
        }
        return  chunkIDs;
    }


}
