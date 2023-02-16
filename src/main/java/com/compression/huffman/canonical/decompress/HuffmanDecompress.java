package com.compression.huffman.canonical.decompress;

import com.compression.Decompressable;
import com.compression.file.FileRead;
import com.compression.huffman.canonical.utils.CanonicalCode;
import com.compression.huffman.node.HuffmanNode;
import com.compression.huffman.utils.HuffmanTree;

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
                CanonicalCode canonicalCode = readKey(inp);
                HuffmanTree huffTree = canonicalCode.toCodeTree();
                decompress(huffTree, inp, out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

     CanonicalCode readKey(FileRead in) throws IOException {
        int[] codeLengths = new int[257];
        for (int i = 0; i < codeLengths.length; i++) {
            // For this file format, we read 8 bits in big endian
            int val = 0;
            for (int j = 0; j < 8; j++)
                val = (val << 1) | in.readNoEof();
            codeLengths[i] = val;
        }
        return new CanonicalCode(codeLengths);
    }

    void decompress(HuffmanTree code, FileRead input, OutputStream output) throws IOException {
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
                sym = node.getSymbol();
                if (sym > 255 || sym == -1) break;

                output.write(sym);
                node = code.root;
            }
        }
        output.close();
    }
}
