package huffman.decompress;

import java.io.File;

public interface HuffmanDecompressable {
    //Method to decompress any Huffman encoded file given input, output and a key file.
    void compressFile(File inputFile, File outputFile, String keyFile);
}
