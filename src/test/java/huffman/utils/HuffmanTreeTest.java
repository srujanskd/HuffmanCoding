package huffman.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class HuffmanTreeTest {

    @Test
    public void whenBuildTreeIsCalled_thenTreeShouldBeSame() {
        String testString = "Hello";
        InputStream testStream = new ByteArrayInputStream(testString.getBytes());
        FrequencyTable freq = new FrequencyTable(new int[257]);
        freq.buildFrequencyTable(testStream);

        HuffmanTree huffTree = HuffmanTree.buildHuffmanTree(freq);
        Assert.assertEquals(5, huffTree.root.getFrequency());
        Assert.assertEquals(72, huffTree.root.leftNode.leftNode.getSymbol());
    }

    @Test
    public void whenBuildTreeIsCalledWithZeroFreq_thenTreeShouldBeValid() {
        String testString = "";
        InputStream testStream = new ByteArrayInputStream(testString.getBytes());
        FrequencyTable freq = new FrequencyTable(new int[257]);
        freq.buildFrequencyTable(testStream);

        HuffmanTree huffTree = HuffmanTree.buildHuffmanTree(freq);
        Assert.assertEquals(0, huffTree.root.getFrequency());
    }

    @Test(expected = NullPointerException.class)
    public void whenBuildTreeIsCalledWithNullObj_thenShouldReturnException() {
        HuffmanTree huffTree = HuffmanTree.buildHuffmanTree(null);
    }
}