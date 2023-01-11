package huffman.compress;

import huffman.file.FileWrite;
import huffman.utils.FrequencyTable;
import huffman.utils.HuffmanTree;

import java.io.*;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Uses Static Huffman Compression algorithm to compress a given file.
 */
public class HuffmanCompress implements HuffmanCompressable {

    // Compresses input file, stores in output file and stores the Huffman Tree in Key file.
    public void compressFile(File inputFile, File outputFile, String keyFile) {
        try {
            Objects.requireNonNull(inputFile);
            Objects.requireNonNull(outputFile);
            Objects.requireNonNull(keyFile);
        }
        catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        FrequencyTable frequencyTable = FrequencyTable.buildFrequencyTable(inputFile, new FrequencyTable(new int[257]));
        frequencyTable.increment(256);
        HuffmanTree huffTree = HuffmanTree.buildHuffmanTree(frequencyTable);

        try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile))) {
            try (FileWrite out = new FileWrite(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                writeKey(huffTree, keyFile);
                compress(huffTree, in, out);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // Serializes and writes the Huffman coded tree into the key file
    private void writeKey(HuffmanTree codeTree, final String filename) throws IOException {
        FileOutputStream keyFile = null;
        ObjectOutputStream out = null;
        try {
            keyFile = new FileOutputStream(filename);
            out = new ObjectOutputStream(keyFile);
            out.writeObject(codeTree);
        }
        finally {
            if(out != null) {
                out.close();
            }
            if(keyFile != null) {
                keyFile.close();
            }
        }
    }

    //Reads the input file and compresses each character and writes to the output file
    private void compress(HuffmanTree code, InputStream input, FileWrite out) throws IOException {
        boolean loop = true;

        while (loop) {
            int b = input.read();
            if (b != -1)
                write(b, code, out);
            else if(b == -1) {
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
