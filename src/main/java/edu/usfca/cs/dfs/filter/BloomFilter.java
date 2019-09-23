package edu.usfca.cs.dfs.filter;


import com.sangupta.murmur.Murmur1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.System.exit;

public class BloomFilter {
    private final int[] bloom; //bloomFilter
    private final int size;
    private enum exist {maybe, no};
    private enum action {add, check, delete}

    public BloomFilter(int size) { //constructor
        this.size = size;
        this.bloom = new int[this.size];
    }

    public void add(String str) {
        this.addToBloom(str);

    }

    public String check(String str) {
        return this.checkInBloom(str);
    }

    public void delete(String str) {
        this.deleteFromBloom(str);
    }

    private void addToBloom(String str) {
        this.bloom[this.getHash(str)] += 1;
    }

    private String checkInBloom(String str) {
        if (this.bloom[this.getHash(str)] > 0) {
            return exist.maybe.toString();
        }
        return exist.no.toString();
    }

    private void deleteFromBloom(String str) {
        int hash = this.getHash(str);
        if (this.bloom[hash]>0) {
            this.bloom[hash] -= 1;
        }

    }

    private int getHash(String str) { // case insensitive hashing
        //int hash = Math.abs(str.toLowerCase().hashCode()%this.size);
        byte[] bytearr = str.toLowerCase().getBytes();
        long SEED = 111;
        long hash = Murmur1.hash(bytearr, bytearr.length, SEED);
        System.out.println("hash of "+ str + " is : "+ hash);
        return Math.abs((int)(hash%this.size));
    }

//    private void executeCommand(String cmd, String str) {
//        if (action.add.toString().equals(cmd)) {
//            this.add(str);
//            System.out.println("added");
//
//        } else if (action.check.toString().equals(cmd)) {
//            System.out.println(this.check(str));
//
//        } else if (cmd.equals(action.delete.toString())) {
//            this.delete(str);
//            System.out.println("deleted");
//
//        } else {
//            exit(1);
//        }
//    }


//    public static void main(String[] args) throws IOException {
//        System.out.println("::: Bloom Filter :::");
//        BloomFilter bf = new BloomFilter(483647);
//
//        for(;;) {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//            String input = reader.readLine();
//
//            if (input.contains(" ")) {
//                String[] inputs = input.split(" ");
//                bf.executeCommand(inputs[0], inputs[1]);
//            } else {
//                exit(1);
//            }
//        }
//
//    }




}

