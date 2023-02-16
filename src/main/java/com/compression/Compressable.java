package com.compression;


public interface Compressable<T> {

    //Method to compress any File given input, output and a key file to store the Huffman Tree
    void compressFile(T inputFile, T outputFile);
}
