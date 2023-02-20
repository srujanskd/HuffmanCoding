package com.compression.huffman.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class FrequencyMap implements iFrequencyTable<String>, Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> frequencyMap;
    public FrequencyMap() {
        this.frequencyMap = new HashMap<>();
    }
    @Override
    public int get(String symbol) {
        Objects.requireNonNull(symbol);
        return this.frequencyMap.getOrDefault(symbol, 0);
    }
    public int size() {
        return this.frequencyMap.size();
    }

    @Override
    public void set(String symbol, int frequency) {
        Objects.requireNonNull(symbol);
        if(frequency < 0) {
            throw new IllegalArgumentException("Negative frequency is not allowed");
        }
        this.frequencyMap.put(symbol, frequency);
    }
    @Override
    public void increment(String symbol) {
        Integer freq = this.frequencyMap.getOrDefault(symbol, 0);
        if(freq == Integer.MAX_VALUE){
            throw new IllegalArgumentException("Cannot increment since symbol frequency is already at Integer Max value");
        }
        this.frequencyMap.put(symbol, freq + 1);
    }
    public Set<Map.Entry<String, Integer>> getKeyValues() {
        return this.frequencyMap.entrySet();
    }


    public void setFrequencyMap(HashMap<String, Integer> hm) {
        Objects.requireNonNull(hm);
        this.frequencyMap = (Map<String, Integer>) hm.clone();
    }
    @Override
    public void buildFrequencyTable(InputStream input) {
        Objects.requireNonNull(input);
        StringBuilder sb = new StringBuilder();
        boolean loop = true;
        try(InputStream inp = new BufferedInputStream(input)) {
            while(loop) {
                int b =  inp.read();
                if(b != -1) {
                    Character c = (char) b;
                    if (Character.isLetter(c)) {
                        sb.append(c);
                    } else {
                        if(sb.toString().length() > 0)
                            this.increment(sb.toString());
                        this.increment(String.valueOf(c));
                        sb.setLength(0);
                    }
                }
                else{
                    loop = false;
                    if(sb.length() > 0) {
                        this.increment(sb.toString());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
