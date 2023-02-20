package com.compression.huffman.wordhuffman.utils;

import com.compression.huffman.utils.FrequencyMap;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TopNHelper {
    private ArrayList<Double> pocket;
    public double bestTopNFrequency(FrequencyMap frequencyMap) {
        pocket = new ArrayList<>();
        pocket.add(3300000.0);
        pocket.add(5.0);
        TopNFrequency topNFrequency = new TopNFrequency();
        FrequencyMap tempFreq = new FrequencyMap();
        double epsilon = 20;
        double percentage = 100.0;
        boolean flag = false;
        while(epsilon > 0.005) {
            if(flag)
                percentage += epsilon;
            else
                percentage -= epsilon;
            HashMap<String, Integer> hm = (HashMap<String, Integer>) topNFrequency.getTopNFrequencyMap(frequencyMap, percentage);
            tempFreq.setFrequencyMap(hm);
            HuffmanTree huffmanTree = HuffmanTree.buildHuffmanTree(tempFreq);
            double headerLength = calculateHeaderLength(tempFreq);
            double bodyLength = calculateBodyLength(tempFreq, huffmanTree);
            double totalLen = headerLength + bodyLength;
            if(totalLen <= pocket.get(0)){
                pocket.add(0, totalLen);
                pocket.add(1, percentage);
            }
            else {
                epsilon /= 2;
                flag = !flag;
            }
        }
        return pocket.get(1);
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
}

