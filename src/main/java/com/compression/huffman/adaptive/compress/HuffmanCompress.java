package com.compression.huffman.adaptive.compress;

import com.compression.Compressable;
import com.compression.file.FileWrite;
import com.compression.huffman.utils.FrequencyTable;
import com.compression.huffman.utils.HuffmanTree;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class HuffmanCompress implements Compressable {
    @Override
    public void compressFile(File inputFile, File outputFile) {
        Objects.requireNonNull(inputFile);
        Objects.requireNonNull(outputFile);

        if(!inputFile.exists())
            throw new IllegalArgumentException("Input file does not exist");

        if(outputFile.exists())
            throw new IllegalArgumentException("Output file already exists, Please provide a new file");
        try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile))) {
            try (FileWrite out = new FileWrite(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                compress(in, out);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    void compress(InputStream in, FileWrite out) throws IOException {
        int[] initFreqs = new int[257];
        Arrays.fill(initFreqs, 1);

        FrequencyTable freqs = new FrequencyTable(initFreqs);
        HuffmanTree huffTree = HuffmanTree.buildHuffmanTree(freqs);
        int count = 0;  // Number of bytes read from the input file
        while (true) {
            // Read and encode one byte
            int symbol = in.read();
            if (symbol == -1)
                break;
            write(symbol, huffTree, out);
            count++;

            // Update the frequency table and possibly the code tree
            freqs.increment(symbol);
            if (count < 262144 && isPowerOf2(count) || count % 262144 == 0)  // Update code tree
                huffTree = HuffmanTree.buildHuffmanTree(freqs);
            if (count % 262144 == 0)  // Reset frequency table
                freqs = new FrequencyTable(initFreqs);
        }
        write(256, huffTree, out);  // EOF
    }

    private void write(int symbol, HuffmanTree code, FileWrite out) throws IOException {
        if(code == null) {
            throw new NullPointerException("Huffman Tree is null");
        }

        ArrayList<Integer> bits = code.getCode(symbol);
        for(int bit : bits) {
            out.write(bit);
        }
    }
    private boolean isPowerOf2(int x) {
        return x > 0 && Integer.bitCount(x) == 1;
    }
}
