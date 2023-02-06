package huffman.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class HuffmanTreeTest {

    @Test
    public void getCode() {
    }

    @Test
    public void whenBuildTreeIsCalled_thenTreeShouldBeSame() {
        File input = new File("/home/srujankashyap/Maven_Test/HuffmanCoding/test.txt");
        FrequencyTable freq = new FrequencyTable(new int[257]);
        freq = FrequencyTable.buildFrequencyTable(input, freq);
        HuffmanTree.buildHuffmanTree(freq);

        HuffmanTree huffTree = HuffmanTree.buildHuffmanTree(freq);
        Assert.assertEquals(huffTree.root.getFrequency(), 6);
    }

    @Test
    public void buildCodeList() {
    }
}