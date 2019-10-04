package edu.usfca.cs.dfs.dfsEntropy;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Shanon {

    @SuppressWarnings("boxing")
    public static double getShannonEntropy(String s){

        Collection<String> tokens = Arrays.asList(s.split(" "));

        Map<String, Double> probabilityTable = new HashMap<String, Double>();

        //create probability table where token->probability
        for(String token: tokens){
            double probability = 1d/tokens.size();

            if(probabilityTable.containsKey(token))
                probabilityTable.put(token, probabilityTable.get(token)+probability);
            else
                probabilityTable.put(token, probability);
        }

        //now generate the shannon entropy
        double entropy = 0;

        for (double prob : probabilityTable.values())
            entropy = entropy + prob * Math.log(prob);


        return entropy*-1;

    }

    public static void main(String[] args){

        String str = "Manali";

        double entropy = Shanon.getShannonEntropy(str);
        System.out.printf("Shannon entropy of %40s: %.12f%n", "\"" + str + "\"", entropy);

        return;
    }

}
