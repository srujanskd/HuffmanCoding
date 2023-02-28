package com.compression.huffman.wordhuffman.compress;

import com.compression.Compressable;
import com.compression.file.FileWrite;
import com.compression.huffman.utils.FileCompare;
import com.compression.huffman.utils.FrequencyMap;
import com.compression.huffman.utils.iHuffmanTree;
import com.compression.huffman.wordhuffman.utils.HuffmanTree;
import com.compression.huffman.wordhuffman.utils.TopNFrequency;
import com.compression.huffman.wordhuffman.utils.TopNHelper;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HuffmanCompress implements Compressable<File> {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public void compressFile(File inputFile, File outputFile) {

        Objects.requireNonNull(inputFile);
        Objects.requireNonNull(outputFile);

        if (!inputFile.exists())
            throw new IllegalArgumentException("Input file does not exist");

        if (outputFile.exists())
            throw new IllegalArgumentException("Output file already exists, Please provide a new file");
        String digest = (new FileCompare()).generateChecksum(inputFile);
        LOGGER.log(Level.INFO, "Message Digest = {0}", digest);
        FrequencyMap frequencyMap = new FrequencyMap();

        try {
            InputStream freqInput = new FileInputStream(inputFile);
            frequencyMap.buildFrequencyTable(freqInput);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        TopNHelper tn = new TopNHelper(frequencyMap);
        //perAndHeadLen will have [percentage, header length]
//        double[] perAndHeadLen1 = tn.bestTopNFrequency(0, 100);
        TopNFrequency topNFrequency = new TopNFrequency();
        ArrayList<AbstractMap.SimpleImmutableEntry<String, Integer>> sortMap = (ArrayList<AbstractMap.SimpleImmutableEntry<String, Integer>>) topNFrequency.getSortedMap(frequencyMap);

        double[] perAndHeadLen = getBestPercentAndHeaderLenUsingThreads(frequencyMap, sortMap);
        double percent = perAndHeadLen[0];
//
        LOGGER.log(Level.INFO, "Header Length {0} KB",(perAndHeadLen[1]) / 1000.0);
        LOGGER.log(Level.INFO, "Top N percent : {0} %", percent);

        frequencyMap.setFrequencyMap((HashMap<String, Integer>) topNFrequency.getTopNFrequencyMap(frequencyMap, sortMap, percent));
        frequencyMap.increment("~~");

        iHuffmanTree huffTree = HuffmanTree.buildHuffmanTree(frequencyMap);

        try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile))) {
            try (FileWrite out = new FileWrite(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                writeKey(frequencyMap, digest, out);
                compress((HuffmanTree) huffTree, in, out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        LOGGER.log(Level.INFO, "Average Huffman Bits : " + ((HuffmanTree) huffTree).averageHuffmanBits(frequencyMap));


    }

    double[] getBestPercentAndHeaderLenUsingThreads(FrequencyMap frequencyMap, ArrayList<?> sortedMap) {

        Temperature temp = new Temperature(1000, 0.2, 5);

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

        int n = temp.splitArray.size();
        ArrayList<Future<List<Double>>> bestFreq = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int finalI = i;
            bestFreq.add(executor.submit(() ->
                    (new TopNHelper(frequencyMap)).bestTopNFrequency(temp.splitArray.get(finalI), sortedMap))
            );
        }

        List<List<Double>> ans = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Future<List<Double>> f = bestFreq.get(i);
            List<Double> d;
            try {
                d = f.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            ans.add(d);
        }
        executor.shutdown();
        Collections.sort(ans, Comparator.comparing(d -> d.get(0)));
        double[] bestPer = new double[2];
        bestPer[0] = ans.get(0).get(1);
        bestPer[1] = ans.get(0).get(2);

        return bestPer;
    }

    private void writeKey(FrequencyMap freqTable, String digest, FileWrite output) throws IOException {
        Objects.requireNonNull(freqTable);
        Objects.requireNonNull(output);
        output.writeObject(freqTable);
        output.writeObject(digest);
    }

    void compress(HuffmanTree code, InputStream input, FileWrite out) throws IOException {
        Objects.requireNonNull(input);
        Objects.requireNonNull(out);
        Objects.requireNonNull(code);
        StringBuilder sb = new StringBuilder();
        while (true) {
            int b = input.read();
            if (b != -1) {
                Character c = (char) b;
                if (Character.isLetterOrDigit(c)) {
                    sb.append(c);
                } else {
                    if (sb.length() > 0)
                        write(sb.toString(), code, out);
                    write(String.valueOf(c), code, out);
                    sb.setLength(0);
                }
            } else {
                if (sb.length() > 0)
                    write(sb.toString(), code, out);
                break;
            }
        }
        write("~~", code, out);  // EOF
    }

    // Helper function for writing into output file
    private void write(String symbol, HuffmanTree code, FileWrite out) throws IOException {
        if (code == null) {
            throw new NullPointerException("Huffman Tree is null");
        }
        ArrayList<Integer> bits = (ArrayList<Integer>) code.getCode(symbol);

        for (int bit : bits) {
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
