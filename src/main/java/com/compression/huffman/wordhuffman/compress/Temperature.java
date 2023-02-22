package com.compression.huffman.wordhuffman.compress;

import java.util.ArrayList;
import java.util.List;

public class Temperature {
    public List<Double> temperatureArray;
    Temperature(double temperature, double coolingFactor) {
        this.temperatureArray = new ArrayList<>();
        while(temperature > 1) {
            this.temperatureArray.add(temperature);
            temperature = temperature / (1 + coolingFactor * temperature);
        }
    }
    public int[] getStartAndMidIndex() {
        return new int[] {0, this.temperatureArray.size() / 2};
    }
    public int[] getMidAndEndIndex() {
        int size = this.temperatureArray.size();
        return new int[] {(size / 2) + 1, size};
    }
}
