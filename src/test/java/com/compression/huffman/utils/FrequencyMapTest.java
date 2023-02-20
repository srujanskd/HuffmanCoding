package com.compression.huffman.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
public class FrequencyMapTest {
    FrequencyMap frequencyMap;
    @Test
    public void get() {
        frequencyMap = new FrequencyMap();
        frequencyMap.set("1", 1);
        Assert.assertEquals(1, frequencyMap.get("1"));
        Assert.assertEquals(0, frequencyMap.get("2"));
    }
    @Test(expected = NullPointerException.class)
    public void get2() {
        frequencyMap = new FrequencyMap();
        frequencyMap.get(null);
    }

    @Test
    public void size() {
        frequencyMap = new FrequencyMap();
        Assert.assertEquals(0, frequencyMap.size());
    }

    @Test
    public void set() {
        frequencyMap = new FrequencyMap();
        frequencyMap.set("1" , 1);
        Assert.assertEquals(1, frequencyMap.get("1"));
    }
    @Test(expected = NullPointerException.class)
    public void set2() {
        frequencyMap = new FrequencyMap();
        frequencyMap.set(null , 1);
    }

    @Test
    public void increment() {
        frequencyMap = new FrequencyMap();
        frequencyMap.increment("1");
        Assert.assertEquals(1, frequencyMap.get("1"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void increment2() {
        frequencyMap = new FrequencyMap();
        frequencyMap.set("1", Integer.MAX_VALUE);
        frequencyMap.increment("1");
    }

    @Test
    public void getKeyValues() {
        frequencyMap = new FrequencyMap();
        frequencyMap.set("Hello", 1);
        for(Map.Entry<String, Integer> i : frequencyMap.getKeyValues()){
            Assert.assertEquals(Optional.of(1), Optional.of(i.getValue()));
            Assert.assertEquals("Hello", i.getKey());
        }
    }

    @Test
    public void setFrequencyMap() {
        frequencyMap = new FrequencyMap();
        HashMap<String, Integer> hm = new HashMap<>();
        hm.put("Hello", 1);
        frequencyMap.setFrequencyMap(hm);
        Assert.assertEquals(1, frequencyMap.get("Hello"));
    }

    @Test
    public void buildFrequencyTable() {
        frequencyMap = new FrequencyMap();
        String testString = "Hello ";
        InputStream testStream = new ByteArrayInputStream(testString.getBytes());
        frequencyMap.buildFrequencyTable(testStream);
        Assert.assertEquals(1, frequencyMap.get("Hello"));
        Assert.assertEquals(1, frequencyMap.get(" "));

    }
}