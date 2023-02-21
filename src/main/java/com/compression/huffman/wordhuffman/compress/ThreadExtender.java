package com.compression.huffman.wordhuffman.compress;

import com.compression.huffman.utils.FrequencyMap;
import com.compression.huffman.wordhuffman.utils.TopNHelper;

public class ThreadExtender implements Runnable{
    private double percent1, percent2;
    public double[] bestResults;
    FrequencyMap frequencyMap;
    ThreadExtender(double percent1, double percent2, FrequencyMap frequencyMap) {
        this.percent1 = percent1;
        this.percent2 = percent2;
        this.frequencyMap = frequencyMap;
    }
    @Override
    public void run() {
        TopNHelper topNHelper = new TopNHelper(frequencyMap);
        this.bestResults = topNHelper.bestTopNFrequency(percent1, percent2);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Best Percentage of thread "+Thread.currentThread().getName() + " : "+this.bestResults[1]);
    }

}
