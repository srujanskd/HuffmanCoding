package com.compression.huffman.wordhuffman.utils;

import com.compression.huffman.utils.FrequencyMap;
import org.apache.commons.lang3.SerializationUtils;

import java.util.HashMap;
import java.util.Map;

public class TopNHelper {
    private double[] pocket;
    FrequencyMap frequencyMap;
    public TopNHelper(FrequencyMap frequencyMap) {
            this.frequencyMap = frequencyMap;
    }
    synchronized public double[] bestTopNFrequency(double start, double end) {
        pocket = new double[2];
        pocket[0] = Integer.MAX_VALUE;
        pocket[1] = 0;
        double[] currentState;
        currentState = pocket;
        TopNFrequency topNFrequency = new TopNFrequency();
        FrequencyMap tempFreq = new FrequencyMap();
        double temp = 1000;
        double coolingRate = 0.1;
        while(temp > 1) {
            double percentage = Math.random() * (end - start + 1) + start;
            HashMap<String, Integer> hm = (HashMap<String, Integer>) topNFrequency.getTopNFrequencyMap(this.frequencyMap, percentage);
            tempFreq.setFrequencyMap(hm);
            HuffmanTree huffmanTree = HuffmanTree.buildHuffmanTree(tempFreq);
            double headerLength = calculateHeaderLength(tempFreq);
            double bodyLength = calculateBodyLength(tempFreq, huffmanTree);
            double totalLen = headerLength + bodyLength;
            if(probability(currentState[0], totalLen, temp) > Math.random()) {
                currentState[1] = percentage;
                currentState[0] = totalLen;
            }
            if(currentState[0] < pocket[0]) {
                pocket = currentState;
            }
            temp = temp / (1 + coolingRate * temp);
        }
        System.out.println(pocket[1]);
        return pocket;
    }


    private double calculateBodyLength(FrequencyMap tempFreq, HuffmanTree huffmanTree) {
        long len = 0;
        for(Map.Entry<String, Integer> i : tempFreq.getKeyValues()) {
            len += (i.getValue() * (huffmanTree.getCode(i.getKey())).size());
        }
        return len / 8;
    }

    private double calculateHeaderLength(FrequencyMap tempFreq) {
        return SerializationUtils.serialize(tempFreq).length;
    }
    private static double probability(double f1, double f2, double temp) {
        if (f2 < f1) return 1;
        return Math.exp((f1 - f2) / temp);
    }
}

//    Top N percent : 57.47881258323766%
//        Header Length : 301.331KB
//        Average Huffman Bits : 3.4355587853243343
//        Input file size :5732001Bytes
//        Compressed file size :2477509Bytes
//        Compression Percentage :56.777589536359116%
//        Execution time : 3960ms
//        Total memory Used : 36MB