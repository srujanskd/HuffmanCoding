package com.compression.huffman.wordhuffman.utils;

import com.compression.huffman.utils.FrequencyMap;

import java.util.*;

public class TopNFrequency {
    public Map<?, Integer> getTopNFrequencyMap(FrequencyMap frequencyMap, Double topN) {
        HashMap<String, Integer> mpp = new HashMap<>();
        ArrayList<AbstractMap.SimpleImmutableEntry<String, Integer>> sortedMap = new ArrayList<>();
        for(Map.Entry<String, Integer> i : frequencyMap.getKeyValues()) {
            sortedMap.add(new AbstractMap.SimpleImmutableEntry<>(i.getKey(), i.getValue()));
        }
        long top20 = (long) ((topN * frequencyMap.size()) / 100);
        Collections.sort(sortedMap, (i1, i2) -> i2.getValue().compareTo(i1.getValue())
        );
        for(AbstractMap.SimpleImmutableEntry<String, Integer> si : sortedMap) {
            if(top20-- > 0)
                mpp.put(si.getKey(), si.getValue());
            else {
                String s = si.getKey();
                for(Character c : s.toCharArray()) {
                    mpp.put(c.toString(), (si.getValue() + mpp.getOrDefault(c.toString(), 0)));
                }
            }
        }
        return mpp;
    }
}
