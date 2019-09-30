package edu.usfca.cs.dfs;

import edu.usfca.cs.dfs.filter.MBloomFilter;

import java.util.Scanner;

public class StarterM {

    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of hashes : ");
        int numberOfHashes = scanner.nextInt();

        System.out.println("Enter the size of the bloomfilter");
        int size = scanner.nextInt();

        System.out.println("Initializing Bloomfilter....");
        MBloomFilter bloomFilter = new MBloomFilter(size,numberOfHashes);

        System.out.println("Enter the name of the file");
        String filename = scanner.nextLine();

        bloomFilter.put(filename);

        System.out.println("Getting the filename from Bloomfilter: "+ bloomFilter.get(filename));

        System.out.println("Checking the falsePositiveProbability : "+bloomFilter.falsePositiveProb());

    }

}