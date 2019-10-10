package edu.usfca.cs.dfs.fileUtil;

public class Entropy {

    /**
     * Calculates the entropy per character/byte of a byte array.
     *
     * @param input array to calculate entropy of
     *
     * @return entropy bits per byte
     */
    public static double entropy(byte[] input) {
        if (input.length == 0) {
            return 0.0;
        }

        /* Total up the occurrences of each byte */
        int[] charCounts = new int[256];
        for (byte b : input) {
            charCounts[b & 0xFF]++;
        }

        double entropy = 0.0;
        for (int i = 0; i < 256; ++i) {
            if (charCounts[i] == 0.0) {
                continue;
            }

            double freq = (double) charCounts[i] / input.length;
            entropy -= freq * (Math.log(freq) / Math.log(2));
        }

        return entropy;
    }
//
//    @SuppressWarnings("boxing")
//    public static double getShannonEntropy(String s){
//
//        Collection<String> tokens = Arrays.asList(s.split(" "));
//
//        Map<String, Double> probabilityTable = new HashMap<String, Double>();
//
//        //create probability table where token->probability
//        for(String token: tokens){
//            double probability = 1d/tokens.size();
//
//            if(probabilityTable.containsKey(token))
//                probabilityTable.put(token, probabilityTable.get(token)+probability);
//            else
//                probabilityTable.put(token, probability);
//        }
//
//        //now generate the shannon entropy
//        double entropy = 0;
//
//        for (double prob : probabilityTable.values())
//            entropy = entropy + prob * Math.log(prob);
//
//
//        return entropy*-1;
//
//    }
//
//    public static void main(String[] args){
//
//        String str = "Manali";
//
//        double entropy = Shanon.getShannonEntropy(str);
//        System.out.printf("Shannon entropy of %40s: %.12f%n", "\"" + str + "\"", entropy);
//
//        return;
//    }

}
