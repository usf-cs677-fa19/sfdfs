package edu.usfca.cs.dfs.filter;

import com.sangupta.murmur.Murmur3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.exit;

public class ABloomFilter implements Filter {

    private final Map<Long,Integer> bloom; //bloomFilter
    private final long size;  // size of filter
    private enum action {add, check, delete, print} // actions on bloom filter

    /**
     * constructor, takes in size of filter as param
     * @param size
     */
    public ABloomFilter(long size) { //constructor
        super();
        this.size = size;
        this.bloom = new ConcurrentHashMap<>();
    }

    /**
     * toString func returns bloom in string format
     * @return
     */
    @Override
    public String toString() {
        StringBuilder strB = new StringBuilder();
        strB.append("Bloom Filter\nHash : Count\n");
        for(Map.Entry<Long, Integer> entry : this.bloom.entrySet()) {
            strB.append(entry.getKey()+ " :  "+ entry.getValue()+ "\n");
        }
        return strB.toString();
    }

    /**
     * add function takes in string and add it to bloom filter, returns the count of position
     * @param str
     * @return
     */
    public int add(String str) {
        return this.addToBloom(str);
    }

    /**
     * check function takes in a string and returns : true for "maybe" and false for "no"
     * @param str
     * @return
     */
    public boolean check(String str) {
        return this.checkInBloom(str);
    }

    /**
     * delete function takes in a string and deletes it from bloom filter, always returns true
     * @param str
     * @return
     */
    public boolean delete(String str) {
        return this.deleteFromBloom(str);
    }

    /**
     * addToBloom func takes in a string, gets hash (datatype: long) of it, then marks +1 at that index, returns new mark for that the hash
     * @param str
     * @return
     */
    private int addToBloom(String str) {
        long[] hashes = this.get3Hashes(str);
        int avgAdd = 0;
        for (long hash : hashes) {
            int count = this.bloom.containsKey(hash) ? this.bloom.get(hash) + 1 : 1;
            this.bloom.put(hash, count);
            avgAdd += count;
        }
        return avgAdd/3;
    }

    /**
     * checkInBloom func takes in a string, gets hash (datatype: long) of it, and returns if bloom filter contains key (hash of param)
     * @param str
     * @return
     */
    private boolean checkInBloom(String str) {
        long[] hashes = this.get3Hashes(str);
        int count = 0;
        for(int i=0; i<hashes.length; i++) {
            if(this.bloom.containsKey(hashes[i])) {
                count += 1;
            }
        }
        return count == hashes.length;
    }

    /**
     * deleteFromBloom func takes in a string, gets hash (datatype: long) of it, and decreases the count(until zero) and always return true
     * @param str
     * @return
     */
    private boolean deleteFromBloom(String str) {
        long[] hashes = this.get3Hashes(str);
        for(int i=0; i< hashes.length; i++) {
            int count = this.bloom.getOrDefault(hashes[i], -1);
            if (count > 1) {
                this.bloom.put(hashes[i], count-1);
            } else if(count == 1) {
                this.bloom.remove(hashes[i]);
            }
        }
        return true;
    }

    /**
     * get3Hashes return long array containing 3 hashes for a string
     * @param str
     * @return
     */
    private long[] get3Hashes(String str) {
        long[] threeHashes = new long[3];
        long[] seeds = new long[]{8191, 524287, 6700417};

        for (int i = 0; i<3; i++) {
            threeHashes[i] = this.getHash(str, seeds[i]);
        }
        return threeHashes;
    }

    /**
     * getHash func return hash for a string wrapped to size of filter
     * @param str
     * @return
     */
    private long getHash(String str, long seed) { // case insensitive hashing and hash wrapped to the size of filter and hardcoded seed
        byte[] barr = str.toLowerCase().getBytes();
        //long SEED = 6700417;
        long hash = Murmur3.hash_x86_32(barr, barr.length, seed/*SEED*/);
        hash = hash % this.size; // wrapping it to size of ABloomFilter
        //System.out.println("hash of "+ str + " is : "+ hash);
        return hash;
    }

    /**
     * executeCommand is a wrapper function to add, check and delete func
     * @param cmd
     * @param str
     */
    public void executeCommand(String cmd, String str) {
        if (action.add.toString().equals(cmd)) {
            System.out.println(this.add(str));

        } else if (action.check.toString().equals(cmd)) {
            System.out.println(this.check(str));

        } else if (cmd.equals(action.delete.toString())) {
            System.out.println(this.delete(str));

        }  else if (cmd.equals(action.print.toString())) {
            System.out.println(this.toString());

        } else {
            exit(1);
        }
    }




}