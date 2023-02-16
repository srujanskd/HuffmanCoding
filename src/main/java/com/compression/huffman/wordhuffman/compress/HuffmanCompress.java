package com.compression.huffman.wordhuffman.compress;

import com.compression.Compressable;
import com.compression.file.FileWrite;
import com.compression.huffman.utils.FrequencyMap;
import com.compression.huffman.wordhuffman.utils.HuffmanTree;

import java.io.*;
import java.util.ArrayList;
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
            else if(b == -1) {
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
