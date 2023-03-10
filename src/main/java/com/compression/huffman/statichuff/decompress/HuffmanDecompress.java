package com.compression.huffman.statichuff.decompress;

import com.compression.Decompressable;
import com.compression.file.FileRead;
import com.compression.huffman.node.HuffmanNode;
import com.compression.huffman.utils.FileCompare;
import com.compression.huffman.utils.FrequencyTable;
import com.compression.huffman.utils.HuffmanTree;

import java.io.*;
import java.util.Objects;

public class HuffmanDecompress implements Decompressable<File> {
    public void decompressFile(File inputFile, File outputFile) {

        Objects.requireNonNull(inputFile);
        Objects.requireNonNull(outputFile);

        if(!inputFile.exists())
            throw new IllegalArgumentException("Encoded file does not exist");
        if(outputFile.exists())
            throw new IllegalArgumentException("Output file already exists");

        try (FileRead inp = new FileRead(new BufferedInputStream(new FileInputStream(inputFile))) ){
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))){
                FrequencyTable frequencyTable = readKey(inp);
                HuffmanTree huffTree = HuffmanTree.buildHuffmanTree(frequencyTable);
                decompress(huffTree, inp, out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FrequencyTable readKey(FileRead input) throws IOException, ClassNotFoundException {
        Objects.requireNonNull(input);
        FrequencyTable freqTable = new FrequencyTable(new int[257]);
        for(int i = 0; i < freqTable.size(); i++) {
            int freq = input.read32();
            freqTable.set(i, freq);
        }
        FileCompare.inputDigest = (String) input.readObj();
        return freqTable;
    }

    private void decompress(HuffmanTree code, FileRead input, OutputStream output) throws IOException {
        Objects.requireNonNull(code);
        Objects.requireNonNull(input);
        Objects.requireNonNull(output);

        HuffmanNode node = code.root;

        while (true) {
            int b = input.read();
            int sym;
            if (b == -1) break;
            if (b == 0)
                node = node.leftNode;
            else if (b == 1)
                node = node.rightNode;
            if (node.isLeafNode()) {
                sym = (int) node.getSymbol();
                if (sym > 255 || sym == -1) break;

                output.write(sym);
                node = code.root;
            }
        }
        output.close();
    }
}
