package com.compression.huffman.wordhuffman.decompress;

import com.compression.Decompressable;
import com.compression.file.FileRead;
import com.compression.huffman.node.HuffmanNode;
import com.compression.huffman.utils.FrequencyMap;
import com.compression.huffman.wordhuffman.utils.HuffmanTree;

import java.io.*;
import java.util.Objects;

public class HuffmanDecompress implements Decompressable<File> {
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
                FrequencyMap frequencyMap = readKey(inp);
                HuffmanTree huffTree = HuffmanTree.buildHuffmanTree(frequencyMap);
                decompress(huffTree, inp, out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FrequencyMap readKey(FileRead inp) throws IOException, ClassNotFoundException {
        return (FrequencyMap) inp.readObj();
    }

    private void decompress(HuffmanTree code, FileRead input, OutputStream output) throws IOException {
        Objects.requireNonNull(code);
        Objects.requireNonNull(input);
        Objects.requireNonNull(output);

        HuffmanNode node = code.root;

        while (true) {
            int b = input.read();
            String sym;
            if (b == -1) break;
            if (b == 0)
                node = node.leftNode;
            else if (b == 1)
                node = node.rightNode;
            if (node.isLeafNode()) {
                sym = (String) node.getSymbol();
                if (sym.equals("256")) break;
                for(int i = 0; i < sym.length(); i++)
                    output.write(sym.charAt(i));
                node = code.root;
            }
        }
        output.close();
    }
}
