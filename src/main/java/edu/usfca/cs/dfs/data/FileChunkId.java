package edu.usfca.cs.dfs.data;

public class FileChunkId {

    public static String getId(String filename,String chunkNumber) {
        return filename+"_Chunk_"+chunkNumber;
    }

    public static String getId(String filename,int chunkNumber) {
        return filename+"_Chunk_"+chunkNumber;
    }


}
