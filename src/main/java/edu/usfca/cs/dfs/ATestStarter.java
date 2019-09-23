package edu.usfca.cs.dfs;


import edu.usfca.cs.dfs.filter.ABloomFilter;
import edu.usfca.cs.dfs.init.Init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import static java.lang.System.exit;

public class ATestStarter {

    public static void main(String[] args) throws IOException {
        if(Init.isCorrectArgs(args)) {
            System.out.println("::: Bloom Filter :::");

            Map<String,String> c = Init.readConfigFileIntoMap(args[0]);
            long filterSize = Long.parseLong(c.get("filterSize"));
            System.out.println(c);

            ABloomFilter bf = new ABloomFilter(filterSize);

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

        } else {
            exit(1);
        }


    } // end of main


} // end of class
