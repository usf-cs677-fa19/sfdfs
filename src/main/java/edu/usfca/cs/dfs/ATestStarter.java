package edu.usfca.cs.dfs;


import edu.usfca.cs.dfs.filter.ABloomFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.System.exit;

public class ATestStarter {

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
