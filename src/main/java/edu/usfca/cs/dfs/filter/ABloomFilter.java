package edu.usfca.cs.dfs.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.exit;

public class ABloomFilter implements Filter {

    private final Map<Integer,Integer> bloom; //bloomFilter
    private enum action {add, check, delete}

    public ABloomFilter() { //constructor
        super();
        this.bloom = new HashMap<>();
    }

    public int add(String str) {
        return  this.addToBloom(str);
    }

    public boolean check(String str) {
        return this.checkInBloom(str);
    }

    public boolean delete(String str) {
        return this.deleteFromBloom(str);
    }

    private int addToBloom(String str) {
        int hash = this.getHash(str);
        int count = this.bloom.containsKey(hash) ? this.bloom.get(hash) + 1 : 1;
        this.bloom.put(hash, count);
        return count;

    }

    private boolean checkInBloom(String str) {
        return this.bloom.containsKey(this.getHash(str));

    }

    private boolean deleteFromBloom(String str) {
        int hash = this.getHash(str);
        int count = this.bloom.getOrDefault(hash, -1);
        if (count > 1) {
           this.bloom.put(hash, count-1);
        } else if(count == 1) {
            this.bloom.remove(hash);
        }
        return true;
    }

    private int getHash(String str) { // case insensitive hashing
        //int hash = Math.abs(str.toLowerCase().hashCode()%this.size);
//        byte[] bytearr = str.toLowerCase().getBytes();
//        long SEED = 111;
//        long hash = Murmur1.hash(bytearr, bytearr.length, SEED);
//        System.out.println("hash of "+ str + " is : "+ hash);
//        return Math.abs((int)(hash%this.size));
        int hash = str.toLowerCase().hashCode();
        return hash;

    }

    private void executeCommand(String cmd, String str) {
        if (action.add.toString().equals(cmd)) {
            System.out.println(this.add(str));

        } else if (action.check.toString().equals(cmd)) {
            System.out.println(this.check(str));

        } else if (cmd.equals(action.delete.toString())) {
            System.out.println(this.delete(str));

        } else {
            exit(1);
        }
    }


    public static void main(String[] args) throws IOException {
        System.out.println("::: Bloom Filter :::");
        ABloomFilter bf = new ABloomFilter();

        for(;;) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input = reader.readLine();

            if (input.contains(" ")) {
                String[] inputs = input.split(" ");
                bf.executeCommand(inputs[0], inputs[1]);
            } else {
                exit(1);
            }
        }

    }





}
