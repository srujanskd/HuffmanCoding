package com.compression.huffman.wordhuffman.compress;

import java.util.ArrayList;
import java.util.List;

public class Temperature {
    public List<List<Double>> splitArray;

    Temperature(double temperature, double coolingFactor, int split) {
        splitArray = new ArrayList<>();
        ArrayList<Double> temperatureArray = new ArrayList<>();
        while (temperature > 1) {
            temperatureArray.add(temperature);
            temperature = temperature / (1 + coolingFactor * temperature);
        }
        splitArray = splitArray(split, temperatureArray);
    }

    private List<List<Double>> splitArray(int split, ArrayList<Double> arr) {
        int len = arr.size();
        int elems = len / split;
        List<List<Double>> ans = new ArrayList<>();
        for (int i = 0; i < len; i = i + elems) {
            if(i + elems <= len) {
                List<Double> part = arr.subList(i, i + elems);
                ans.add(part);
            }
        }
        if (len % split != 0){
            List<Double> part =arr.subList(len - 1, len);
            ans.add(part);
        }
        return ans;
    }
}
