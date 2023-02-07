package huffman.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HuffmanTreeTest {

    @Test
    public void getCode() {
    }

    @Test
    public void whenBuildTreeIsCalled_thenTreeShouldBeSame() {
        File input = new File("testFile.txt");
        try {
            if(input.createNewFile()) {
                FileWriter fw = new FileWriter(input);
                fw.write("Hello");
                fw.close();
            }
            else {
                System.out.println("File already exists");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FrequencyTable freq = new FrequencyTable(new int[257]);
        freq = FrequencyTable.buildFrequencyTable(input, freq);

        HuffmanTree huffTree = HuffmanTree.buildHuffmanTree(freq);
        Assert.assertEquals(huffTree.root.getFrequency(), 5);
        input.delete();
    }

    @Test
    public void buildCodeList() {
    }
}