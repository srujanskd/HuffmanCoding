package huffman.compress;

import java.io.File;


public interface HuffmanCompressable {

    //Method to compress any File given input, output and a key file to store the Huffman Tree
    public void compressFile(File inputFile, File outputFile, String keyFile);
}
