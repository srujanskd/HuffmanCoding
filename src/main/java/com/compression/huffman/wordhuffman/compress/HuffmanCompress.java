package com.compression.huffman.wordhuffman.compress;

import com.compression.Compressable;
import com.compression.file.FileWrite;
import com.compression.huffman.utils.FrequencyMap;
import com.compression.huffman.wordhuffman.utils.HuffmanTree;
import com.compression.huffman.wordhuffman.utils.TopNFrequency;
import com.compression.huffman.wordhuffman.utils.TopNHelper;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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
        ThreadExtender o1 = new ThreadExtender(0, 25, frequencyMap);
        ThreadExtender o2 = new ThreadExtender(25, 50, frequencyMap);
        ThreadExtender o3 = new ThreadExtender(50, 75, frequencyMap);
        ThreadExtender o4 = new ThreadExtender(75, 100, frequencyMap);

        Thread t1 = new Thread(o1);
        Thread t2 = new Thread(o2);
        Thread t3 = new Thread(o3);
        Thread t4 = new Thread(o4);
        t1.start();

        t2.start();
        t3.start();
        t4.start();
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        double percent;
        ThreadExtender per1 = o1.bestResults[0] < o1.bestResults[0] ? o1: o2;
        ThreadExtender per2 = o3.bestResults[0] < o4.bestResults[0] ? o1 : o2;
        percent = per1.bestResults[0] < per2.bestResults[0] ? per1.bestResults[1] : per2.bestResults[1];

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
    private void writeKey(FrequencyMap freqTable, FileWrite output) throws IOException {
        Objects.requireNonNull(freqTable);
        Objects.requireNonNull(output);
        byte[] b = SerializationUtils.serialize(freqTable);
        output.writeObject(freqTable);
        System.out.println("Header Length : "+(b.length)/1000.0 + "KB");
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
