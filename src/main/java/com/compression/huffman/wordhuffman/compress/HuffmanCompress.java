package com.compression.huffman.wordhuffman.compress;

import com.compression.Compressable;
import com.compression.file.FileWrite;
import com.compression.huffman.utils.FrequencyMap;
import com.compression.huffman.wordhuffman.utils.HuffmanTree;
import com.compression.huffman.wordhuffman.utils.TopNFrequency;
import com.compression.huffman.wordhuffman.utils.TopNHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.*;

public class HuffmanCompress implements Compressable<File> {
    @Override
    public void compressFile(File inputFile, File outputFile) {
        Objects.requireNonNull(inputFile);
        Objects.requireNonNull(outputFile);

        if(!inputFile.exists())
            throw new IllegalArgumentException("Input file does not exist");

        if(outputFile.exists())
            throw new IllegalArgumentException("Output file already exists, Please provide a new file");

        FrequencyMap frequencyMap = new FrequencyMap();
        try {
            InputStream freqInput = new FileInputStream(inputFile);
            frequencyMap.buildFrequencyTable(freqInput);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //perAndHeadLen will have [percentage, header length]

        double[] perAndHeadLen = getBestPercentAndHeaderLenUsingThreads(frequencyMap);

        double percent = perAndHeadLen[0];
        System.out.println("Header Length : "+ (perAndHeadLen[1]) / 1000.0 + "KB");

        System.out.println("Top N percent : " + percent + "%");
        TopNFrequency topNFrequency = new TopNFrequency();
        frequencyMap.setFrequencyMap((HashMap<String, Integer>) topNFrequency.getTopNFrequencyMap(frequencyMap, percent));
        frequencyMap.increment("256");
        HuffmanTree huffTree = HuffmanTree.buildHuffmanTree(frequencyMap);
        try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile))) {
            try (FileWrite out = new FileWrite(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                writeKey(frequencyMap, out);
                compress(huffTree, in, out);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Average Huffman Bits : "  + huffTree.averageHuffmanBits(frequencyMap));

    }
    double[] getBestPercentAndHeaderLenUsingThreads(FrequencyMap frequencyMap) {
        Temperature temp = new Temperature(10000, 0.2);
//        ThreadExtender threadExtender1 = new ThreadExtender(0, 50, frequencyMap);
//        ThreadExtender threadExtender2 = new ThreadExtender(50, 100, frequencyMap);

        int[] startAndMid = temp.getStartAndMidIndex();
        int[] midAndEnd = temp.getMidAndEndIndex();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        TopNHelper tn = new TopNHelper(frequencyMap);
        TopNHelper tn2 = new TopNHelper(frequencyMap);

        Future<double[]> ans1 = executor.submit( () ->
                tn.bestTopNFrequency(startAndMid[0], startAndMid[1], temp.temperatureArray)
        );
        Future<double[]> ans2 = executor.submit( () ->
                tn2.bestTopNFrequency(midAndEnd[0], midAndEnd[1], temp.temperatureArray)
        );
        double[] ans = new double[2];
        try {
            double[] a = ans1.get();
            double[] b = ans2.get();
            if(a[0] < b[0]){
                ans[0] = a[1];
                ans[1] = a[2];
            }
            else {
                ans[0] = b[1];
                ans[0] = b[2];
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        executor.shutdown();
        return ans;

//        ThreadExtender threadExtender1 = new ThreadExtender(startAndMid[0], startAndMid[1], temp.temperatureArray, frequencyMap);
//        ThreadExtender threadExtender2 = new ThreadExtender(midAndEnd[0], midAndEnd[1], temp.temperatureArray, frequencyMap);
//        Thread t1 = new Thread(threadExtender1);
//        Thread t2 = new Thread(threadExtender2);
//        t1.start();
//        t2.start();
//        try {
//            t1.join();
//            t2.join();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        ThreadExtender per1 = threadExtender1.bestResults[0] < threadExtender2.bestResults[0] ? threadExtender1: threadExtender2;
//        return new double[] {per1.bestResults[1], per1.bestResults[2]};
    }
    private void writeKey(FrequencyMap freqTable, FileWrite output) throws IOException {
        Objects.requireNonNull(freqTable);
        Objects.requireNonNull(output);
        output.writeObject(freqTable);
    }

    void compress(HuffmanTree code, InputStream input, FileWrite out) throws IOException {
        Objects.requireNonNull(input);
        Objects.requireNonNull(out);
        Objects.requireNonNull(code);
        StringBuilder sb = new StringBuilder();
        while (true) {
            int b = input.read();
            if(b != -1) {
                Character c = (char) b;
                if (Character.isLetter(c)) {
                    sb.append(c);
                } else {
                    if(sb.length() > 0)
                        write(sb.toString(), code, out);
                    write(String.valueOf(c), code, out);
                    sb.setLength(0);
                }
            }
            else {
                if(sb.length() > 0)
                    write(sb.toString(), code, out);
                break;
            }
        }
        write("256", code, out);  // EOF
    }

    // Helper function for writing into output file
    private void write(String symbol, HuffmanTree code, FileWrite out) throws IOException {
        if(code == null) {
            throw new NullPointerException("Huffman Tree is null");
        }
        ArrayList<Integer> bits = (ArrayList<Integer>) code.getCode(symbol);

        for(int bit : bits) {
            out.write(bit);
        }
    }
}

//    Average Huffman Bits : 2.747370720860601
//        Input file size :10555654Bytes
//        Compressed file size :4333788Bytes
//        Compression Percentage :58.94344395903845%
//        Execution time : 3993ms
//        Total memory Used : 145MB
