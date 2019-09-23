package edu.usfca.cs.dfs.filter;

import java.util.HashMap;

public class MBloomFilter extends Filter {

    private HashMap<Integer,Integer> bloomfilter;

    private int size;


    @Override
    public int add(String str) {
        return 0;
    }

    @Override
    public boolean check(String str) {
        return false;
    }

    @Override
    public boolean delete(String str) {
        return false;
    }
}
