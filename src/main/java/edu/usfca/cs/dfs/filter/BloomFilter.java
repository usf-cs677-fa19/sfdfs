package edu.usfca.cs.dfs.filter;


import com.sangupta.murmur.Murmur3;
import com.sun.deploy.util.ArrayUtil;
import com.sun.tools.javac.util.ArrayUtils;


import java.util.ArrayList;
import java.util.BitSet;

import static java.lang.System.exit;

public class BloomFilter {

    //actions {put, get, delete, fpp}; // bloom filter actions
    private BitSet bloom;
    private int[] counter;
    private int n; // no of elements in bloomfilter
    private final int m; // length of bitmap and counter
    private final int k; // no of hashes for each string

    /**
     * BloomFilter class implements a bloom filter and provides the following functionality
     * put : puts a string in bloom filter
     *      example > put fileChunk1
     * get : checks a string in bloom filter
     *      example > get fileChunk1
     * delete : deletes a string from bloom filter
     *      example > delete fileChunk1
     * fpp : provides false positive probability of the bloom filter for current number elements in the filter
     *      example > fpp
     * exit : exits the program
     *      example > exit
     *
     * @param bitsInFilter int
     * @param noOfHashes int
     */
    public BloomFilter(int bitsInFilter, int noOfHashes) {
        this.bloom = new BitSet(bitsInFilter);
        this.counter = new int[bitsInFilter];
        this.n = 0;
        this.m = bitsInFilter;
        this.k = noOfHashes;
    }

    private void updateN(int val) {

        this.n += val;
        if (this.n < 0) {
            this.n = 0;
        }
        //System.out.println("Updated value of n : "+ n);
    }

    /**
     * put functions adds a string to bloom filter
     * @param data string
     */
    public void put(String data) {
        this.putInBloom(data);
    }

    /**
     * get function returns true if the bloom filter maybe contains element otherwise returns false
     * @param data string
     * @return
     */
    private boolean get(String data) {
        return this.getFromBloom(data);
    }

    /**
     * delete function gelets the elemt from bloom filter
     * @param data string
     */
    private void delete(String data) {
        this.deleteFromBloom(data);
    }


    /**
     * falsePositiveProb function returns probability of false positive
     * @return float
     */
    private float falsePositiveProb() {
        return this.getFalsePositiveProb();
    }


    private void putInBloom(String data) {
        ArrayList<Integer> results = this.getHashes(data);
        for(int result : results) {
            this.counter[result] += 1;
            this.bloom.set(result);
        }
        this.updateN(1);
    }

    public boolean getFromBloom(String data) {
        ArrayList<Integer> results = this.getHashes(data);
        int count = 0;
        for(int result : results) {
            if(this.bloom.get(result)) {
                count +=1;
            }
        }
        return count == this.k;
    }

    private void deleteFromBloom(String data) {
        ArrayList<Integer> results = this.getHashes(data);

        if (this.get(data)) {
            for(int result : results) {
                if(this.counter[result] > 1) {
                    this.counter[result] -= 1;
                } else if (counter[result] == 1) {
                    this.counter[result] = 0;
                    this.bloom.clear(result);
                }
            }
            this.updateN(-1);
        }

    }

    private float getFalsePositiveProb() {
        return (float)Math.pow(1 - Math.exp(-(float)k / ((float)m / n)), k);
    }

    private ArrayList<Integer> getHashes(String data) {
        ArrayList<Integer> results = new ArrayList<>(this.k);
        long hash1 = Murmur3.hash_x86_32(data.toLowerCase().getBytes(), data.getBytes().length, 777);
        long hash2 = Murmur3.hash_x86_32(data.toLowerCase().getBytes(), data.getBytes().length, hash1);

        for(int i=0; i<k; i++) {
            results.add(Math.abs((int)(hash1 + i * hash2)%this.m));
        }
        //System.out.println(results);
        return results;
    }

    public boolean mergeBloomFilters(BloomFilter newBloomFliter){
        if(this.bloom.size() == newBloomFliter.bloom.size()) {
            for (int i=0;i<newBloomFliter.bloom.size();i++){
                if(!this.bloom.get(i) && newBloomFliter.bloom.get(i)) {
                    this.bloom.set(i);
                }
                this.counter[i] += newBloomFliter.counter[i] ;
            }
            return true;
        }
        return false;
    }

    public void executeCommand(String line) {
        if (line.contains(" ")) {
            String[] params = line.split(" ");
            this.execCommand(params[0], params[1]);
        } else {
            this.execCommand(line);
        }
    }

    private void execCommand(String cmd, String str) {
        switch (cmd) {
            case "put":
                this.put(str);
                System.out.println("added : "+ str);
                break;
            case "get":
                System.out.println("got "+ str + " ? " + this.get(str));
                break;
            case "delete" :
                this.delete(str);
                System.out.println("deleted : "+ str);
                break;
            default:
                System.out.println("Command not found");
        }
    }

    private void execCommand(String cmd) {
        switch (cmd) {
            case "fpp" :
                System.out.println("fpp : "+ this.falsePositiveProb());
                break;
            case "exit" :
                System.out.println("Exiting ...");
                exit(0);
            default:
                System.out.println("Command not found");
        }
    }


}