package com.compression.huffman.wordhuffman.compress;

import com.compression.huffman.utils.FrequencyMap;
import com.compression.huffman.wordhuffman.utils.TopNHelper;

import java.util.List;

public class ThreadExtender implements Runnable{
    private double percent1;
    private double percent2;
    public double[] bestResults;
    Integer start;
    Integer end;
    FrequencyMap frequencyMap;
    List<Double> temperatureArray;
    ThreadExtender(double minPercent, double maxPercent, FrequencyMap frequencyMap) {
        this.percent1 = minPercent;
        this.percent2 = maxPercent;
        this.frequencyMap = frequencyMap;

    }
    ThreadExtender(int start, int end, List<Double> tempArray, FrequencyMap frequencyMap) {
        this.frequencyMap = frequencyMap;
        this.start = start;
        this.end = end;
        this.temperatureArray = tempArray;
    }
    @Override
    public void run() {
        TopNHelper topNHelper = new TopNHelper(frequencyMap);
        this.bestResults = topNHelper.bestTopNFrequency(start, end, temperatureArray);
        System.out.println("Best Percentage of thread "+Thread.currentThread().getName() + " : "+this.bestResults[1]);
    }

}
