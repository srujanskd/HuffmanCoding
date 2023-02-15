package com.compression.huffman.adaptive.decompress;

import com.compression.Decompressable;
import com.compression.file.FileRead;
import com.compression.huffman.node.HuffmanNode;
import com.compression.huffman.utils.FrequencyTable;
import com.compression.huffman.utils.HuffmanTree;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

public class HuffmanDecompress implements Decompressable {
    @Override
    public void decompressFile(File inputFile, File outputFile) {
        Objects.requireNonNull(inputFile);
        Objects.requireNonNull(outputFile);

        if(!inputFile.exists())
            throw new IllegalArgumentException("Encoded file does not exist");
        if(outputFile.exists())
            throw new IllegalArgumentException("Output file already exists");

        try (FileRead inp = new FileRead(new BufferedInputStream(new FileInputStream(inputFile))) ){
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))){
                decompress(inp, out);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void decompress(FileRead input, OutputStream output) throws IOException {
        int[] initFreqs = new int[257];
        Arrays.fill(initFreqs, 1);

        FrequencyTable freqs = new FrequencyTable(initFreqs);
        HuffmanTree huffTree = HuffmanTree.buildHuffmanTree(freqs);// Use same algorithm as the compressor
        int count = 0;  // Number of bytes written to the output file
        while (true) {
            // Decode and write one byte
            int symbol = read(huffTree, input);
            if (symbol == 256)  // EOF symbol
                break;
            output.write(symbol);
            count++;

            // Update the frequency table and possibly the code tree
            freqs.increment(symbol);
            if (count < 262144 && isPowerOf2(count) || count % 262144 == 0)  // Update code tree
                huffTree = HuffmanTree.buildHuffmanTree(freqs);
            if (count % 262144 == 0)  // Reset frequency table
                freqs = new FrequencyTable(initFreqs);
        }
    }

    public int read(HuffmanTree huffTree, FileRead input) throws IOException {
        HuffmanNode node = huffTree.root;
        while (true) {
            int b = input.readNoEof();
            if (b == -1) break;
            if (b == 0)
                node = node.leftNode;
            else if (b == 1)
                node = node.rightNode;
            if (node.isLeafNode()) {
                return node.getSymbol();
            }
        }
        return -1;
    }
    private static boolean isPowerOf2(int x) {
        return x > 0 && Integer.bitCount(x) == 1;
    }
}
