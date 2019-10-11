package edu.usfca.cs.dfs.data;

public class FileChunkId {

    public static String getFileChunkId(String filename,String chunkNumber) {
        return filename+"_Chunk_"+chunkNumber;
    }

    public static String getFileChunkId(String filename,int chunkNumber) {
        return filename+"_Chunk_"+chunkNumber;
    }


}
