package com.compression.huffman.statichuff.compress;

import com.compression.Compressable;
import com.compression.huffman.utils.FrequencyTable;
import com.compression.huffman.utils.HuffmanTree;
import com.compression.file.FileWrite;

import java.io.*;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Uses Static Huffman Compression algorithm to compress a given file.
 */
public class HuffmanCompress implements Compressable {

    // Compresses input file, stores in output file and stores the Huffman Tree in Key file.
    public void compressFile(File inputFile, File outputFile) {

        Objects.requireNonNull(inputFile);
        Objects.requireNonNull(outputFile);

        if(!inputFile.exists())
            throw new IllegalArgumentException("Input file does not exist");

        if(outputFile.exists())
            throw new IllegalArgumentException("Output file already exists, Please provide a new file");

        FrequencyTable frequencyTable = new FrequencyTable(new int[257]);
        try {
            InputStream freqInput = new FileInputStream(inputFile);
            frequencyTable.buildFrequencyTable(freqInput);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        frequencyTable.increment(256);

        HuffmanTree huffTree = HuffmanTree.buildHuffmanTree(frequencyTable);

        try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile))) {
            try (FileWrite out = new FileWrite(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                writeKey(frequencyTable, out);
                compress(huffTree, in, out);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("Average Huffman Bits : "  + huffTree.averageHuffmanBits(frequencyTable));
    }

    // Serializes and writes the Huffman coded tree into the key file
    private void writeKey(FrequencyTable freqTable, FileWrite output) throws IOException {
        Objects.requireNonNull(freqTable);
        Objects.requireNonNull(output);

        for(int i = 0; i < freqTable.size(); i++){
            int freq = freqTable.get(i);
            output.write32(freq);
        }
    }

    //Reads the input file and compresses each character and writes to the output file
     void compress(HuffmanTree code, InputStream input, FileWrite out) throws IOException {
        Objects.requireNonNull(input);
        Objects.requireNonNull(out);
        Objects.requireNonNull(code);

        while (true) {
            int b = input.read();
            if (b != -1)
                write(b, code, out);
            else if(b == -1) {
                break;
            }
        }
        write(256, code, out);  // EOF
    }

    // Helper function for writing into output file
    private void write(int symbol, HuffmanTree code, FileWrite out) throws IOException {
        if(code == null) {
            throw new NullPointerException("Huffman Tree is null");
        }

        ArrayList<Integer> bits = code.getCode(symbol);
        for(int bit : bits) {
            out.write(bit);
        }
    }
}
