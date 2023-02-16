package com.compression.huffman.canonical.compress;

import com.compression.Compressable;
import com.compression.file.FileWrite;
import com.compression.huffman.canonical.utils.CanonicalCode;
import com.compression.huffman.utils.FrequencyTable;
import com.compression.huffman.utils.HuffmanTree;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
        CanonicalCode canonicalCode = new CanonicalCode(huffTree, 257);
        huffTree = canonicalCode.toCodeTree();
        List<Integer> prefix = new ArrayList<>();
        huffTree.buildCodeList(huffTree.root, prefix);

        try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile))) {
            try (FileWrite out = new FileWrite(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                writeKey(out, canonicalCode);
                compress(huffTree, in, out);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void writeKey(FileWrite out, CanonicalCode canonCode) throws IOException {
        for (int i = 0; i < canonCode.getSymbolLimit(); i++) {
            int val = canonCode.getCodeLength(i);
            // For this file format, we only support codes up to 255 bits long
            if (val >= 256)
                throw new RuntimeException("The code for a symbol is too long");

            // Write value as 8 bits in big endian
            for (int j = 7; j >= 0; j--)
                out.write((val >>> j) & 1);
        }

    }

    void compress(HuffmanTree code, InputStream input, FileWrite out) throws IOException {
        Objects.requireNonNull(input);
        Objects.requireNonNull(out);
        Objects.requireNonNull(code);

        while (true) {
            int b = input.read();
            if (b != -1)
                write(b, code, out);
            else {
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
