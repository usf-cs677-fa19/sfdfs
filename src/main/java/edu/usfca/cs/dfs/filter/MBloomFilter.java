package edu.usfca.cs.dfs.filter;


import com.sangupta.murmur.Murmur3;

import java.util.ArrayList;
import java.util.BitSet;

public class MBloomFilter {

    private BitSet bloomFilter;
    private int[] bloomFilterCounter;
    private int size;
    private int numberOfHashes;
    private int numberOfElements;

    public MBloomFilter(int size, int hashes){
        this.bloomFilter = new BitSet(size);
        this.bloomFilterCounter = new int[size];
        this.numberOfElements = 0;
        this.numberOfHashes = hashes;
        this.size = size;
    }

    public void put(String data){
        putInBloomFilter(data.getBytes());
    }

    public boolean get(String data){
        return getFromBloomFilter(data.getBytes());
    }

    public float falsePositiveProb(){

        return (float)Math.pow(1 - Math.exp(-(float)this.numberOfHashes / ((float)this.size / this.numberOfElements)), this.numberOfHashes);
    }

    private void putInBloomFilter(byte[] data){
        ArrayList<Integer>  indexes = getIndexes(data);

        for(int index : indexes){
            this.bloomFilterCounter[index] += 1;
            this.bloomFilter.set(index);
        }
        this.numberOfElements += 1;
    }

    private boolean getFromBloomFilter(byte[] data){

        ArrayList<Integer> indexes = getIndexes(data);
        int count = 0;
        for(int index : indexes){
            if(this.bloomFilter.get(index)){
                count += 1;
            }
        }
        return count == this.numberOfHashes;
    }

    private ArrayList<Integer>  getIndexes(byte[] data){
        ArrayList<Integer> hashes = getHashes(data);
        return hashes;
    }

    private ArrayList<Integer> getHashes(byte[] data){
        ArrayList<Integer> results = new ArrayList<Integer>(this.numberOfHashes);
        long hash1 = Murmur3.hash_x86_32(data, data.length, 777);
        long hash2 = Murmur3.hash_x86_32(data,data.length,hash1);
        for(int i=0; i< this.numberOfHashes; i++){
            results.add(Math.abs((int) (hash1 + i * hash2) % this.size));
        }
        return results;
    }
}