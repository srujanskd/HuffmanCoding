package com.compression;

import java.io.File;

public interface Decompressable {
    //Method to decompress any Huffman encoded file given input, output and a key file.
    void decompressFile(File inputFile, File outputFile);
}
