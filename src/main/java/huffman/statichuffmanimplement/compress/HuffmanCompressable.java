package huffman.statichuffmanimplement.compress;

import java.io.File;


public interface HuffmanCompressable {

    //Method to compress any File given input, output and a key file to store the Huffman Tree
    void compressFile(File inputFile, File outputFile);
}
