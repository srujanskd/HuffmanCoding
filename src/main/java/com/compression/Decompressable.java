package com.compression;

public interface Decompressable<T>{
    //Method to decompress any Huffman encoded file given input, output and a key file.
    void decompressFile(T inputFile, T outputFile);

}