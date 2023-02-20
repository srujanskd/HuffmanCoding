package com.compression.huffman.wordhuffman.utils;

import com.compression.huffman.utils.FrequencyMap;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class TopNFrequencyTest {

    FrequencyMap frequencyMap;
    TopNFrequency topNFrequency = new TopNFrequency();
    @Test
    public void getTopNFrequencyMap() {
        frequencyMap = new FrequencyMap();
        frequencyMap.set("Hello", 3);
        frequencyMap.set("my", 2);
        frequencyMap.set("Hel", 2);
        frequencyMap.set("No", 1);
        HashMap<String, Integer> hm = (HashMap<String, Integer>) topNFrequency.getTopNFrequencyMap(frequencyMap, 50.0);
        Assert.assertEquals("{Hello=3, e=2, H=2, my=2, l=2, N=1, o=1}", hm.toString());
    }
}