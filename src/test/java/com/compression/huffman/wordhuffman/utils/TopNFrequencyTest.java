package com.compression.huffman.wordhuffman.utils;

import com.compression.huffman.utils.FrequencyMap;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class TopNFrequencyTest {

    @Test
    public void getTopNFrequencyMap() {
        FrequencyMap frequencyMap = new FrequencyMap();
        frequencyMap.set("Hello", 3);
        frequencyMap.set("my", 2);
        frequencyMap.set("Hel", 2);
        frequencyMap.set("No", 1);
        TopNFrequency frequency = new TopNFrequency();
        HashMap<String, Integer> hm = (HashMap<String, Integer>) frequency.getTopNFrequencyMap(frequencyMap, Double.valueOf(50));
        System.out.println(hm.toString());
    }
}