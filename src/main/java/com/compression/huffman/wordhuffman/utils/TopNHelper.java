package com.compression.huffman.wordhuffman.utils;

import com.compression.huffman.utils.FrequencyMap;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

public class TopNHelper {
    FrequencyMap frequencyMap;
    public TopNHelper(FrequencyMap frequencyMap) {
            this.frequencyMap = frequencyMap;
    }
     public double[] bestTopNFrequency(double start, double end) {
        double[] pocket = new double[3];
        pocket[0] = Integer.MAX_VALUE;
        pocket[1] = 0;
        pocket[2] = Integer.MAX_VALUE;
        double[] currentState;
        currentState = pocket;
        TopNFrequency topNFrequency = new TopNFrequency();
        FrequencyMap tempFreq = new FrequencyMap();
        double temp = 10000;
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
                currentState[2] = headerLength;
            }
            if(currentState[0] < pocket[0]) {
                pocket = currentState;
            }
            temp = temp / (1 + coolingRate * temp);
        }
        return pocket;
    }

    public List<Double> bestTopNFrequency(List<Double> temperatureArray){
        List<Double> pocket = new ArrayList<>();
        pocket.add((double) Integer.MAX_VALUE);
        pocket.add(0.0);
        pocket.add((double) Integer.MAX_VALUE);
        List<Double> currentState = new ArrayList<>();
        currentState = pocket;
        TopNFrequency topNFrequency = new TopNFrequency();
        FrequencyMap tempFreq = new FrequencyMap();
        for(int i = 0; i < temperatureArray.size(); i++) {
            Double percentage = Math.random() * 100.0;
            HashMap<String, Integer> hm = (HashMap<String, Integer>) topNFrequency.getTopNFrequencyMap(this.frequencyMap, percentage);
            tempFreq.setFrequencyMap(hm);
            HuffmanTree huffmanTree = HuffmanTree.buildHuffmanTree(tempFreq);
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
            Future<Double> head = threadPoolExecutor.submit(() -> calculateHeaderLength(tempFreq));
            double headerLength = 0;
            double bodyLength = calculateBodyLength(tempFreq, huffmanTree);
            try {
                headerLength = head.get();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            threadPoolExecutor.shutdown();

            double totalLen = headerLength + bodyLength;
            if(probability(currentState.get(0), totalLen, temperatureArray.get(i)) > Math.random()) {
                currentState.set(0, totalLen);
                currentState.set(1, percentage);
                currentState.set(2, headerLength);
            }
            if(currentState.get(0) < pocket.get(0)) {
                pocket = currentState;
            }
        }
//        LOGGER.log(Level.INFO, "Percentage returned : " + pocket.get(1));
        return pocket;
    }
    private double calculateBodyLength(FrequencyMap tempFreq, HuffmanTree huffmanTree) {
        long len = 0;
        for(Map.Entry<String, Integer> i : tempFreq.getKeyValues()) {
            len += ((long)i.getValue() * (huffmanTree.getCode(i.getKey())).size());
        }
        return len / 8.0;
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