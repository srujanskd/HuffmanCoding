package huffman.decompress;

import huffman.file.FileRead;
import huffman.node.HuffmanNode;
import huffman.utils.FrequencyTable;
import huffman.utils.HuffmanTree;

import java.io.*;
import java.util.Objects;

public class HuffmanDecompress implements HuffmanDecompressable{

    public void decompressFile(File inputFile, File outputFile) {
        try {
            Objects.requireNonNull(inputFile);
            Objects.requireNonNull(outputFile);
        }
        catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        try (FileRead inp = new FileRead(new BufferedInputStream(new FileInputStream(inputFile))) ){
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))){
                FrequencyTable frequencyTable = readKey(inp);
                HuffmanTree huffTree = HuffmanTree.buildHuffmanTree(frequencyTable);
                decompress(huffTree, inp, out);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FrequencyTable readKey(FileRead input) throws IOException {
        FrequencyTable freqTable = new FrequencyTable(new int[257]);
        for(int i = 0; i < freqTable.size(); i++) {
            int freq = input.read32();
            freqTable.set(i, freq);
        }
        return freqTable;
    }

    private void decompress(HuffmanTree code, FileRead input, OutputStream output) throws IOException {
        HuffmanNode node = code.root;

        while (true) {
            int b = input.read();
            int sym = -1;
            if(b == -1) break;
            if(b == 0) {
                node = node.leftNode;
                if(node.isLeafNode()) {
                    sym = node.getSymbol();
                    if(sym > 255) break;
                    if(sym == -1) break;

                    output.write(sym);
                    node = code.root;
                }
            }
            else if(b == 1) {
                node = node.rightNode;
                if(node.isLeafNode()) {
                    sym = node.getSymbol();
                    if(sym == -1) break;

                    output.write(sym);
                    node = code.root;
                }
            }
        }
        output.close();
    }
}
