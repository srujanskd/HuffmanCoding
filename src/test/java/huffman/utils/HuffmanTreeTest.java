package huffman.utils;

import huffman.node.HuffmanNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HuffmanTreeTest {

    @Test
    public void whenBuildTreeIsCalled_thenTreeShouldBeSame() {
        String testString = "Hello";
        InputStream testStream = new ByteArrayInputStream(testString.getBytes());
        FrequencyTable freq = new FrequencyTable(new int[257]);
        freq.buildFrequencyTable(testStream);

        HuffmanTree huffTree = HuffmanTree.buildHuffmanTree(freq);
        Assert.assertEquals(5, huffTree.root.getFrequency());
        Assert.assertEquals(111, huffTree.root.rightNode.leftNode.getSymbol());
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
        HuffmanTree.buildHuffmanTree(null);
    }

    @Test
    public void whenBuildCodeListIsCalledWithNullObj_thenShouldAssignCodeList() {
        HuffmanNode root = new HuffmanNode(98, 3,
                new HuffmanNode(45, 2, null, null),
                new HuffmanNode(66, 3, null, null));
        HuffmanTree tree = new HuffmanTree(root);
        List<Integer> list = new ArrayList<>();
        tree.buildCodeList(root, list);

        Assert.assertArrayEquals(new ArrayList<Integer>(List.of(1)).toArray(), tree.getCode(66).toArray());
        Assert.assertArrayEquals(new ArrayList<Integer>(List.of(0)).toArray(), tree.getCode(45).toArray());
    }
    @Test(expected = NullPointerException.class)
    public void whenBuildCodeListIsCalledWithNullObj_thenShouldReturnException() {
        HuffmanNode root = new HuffmanNode(98, 3,
                new HuffmanNode(45, 2, null, null),
                new HuffmanNode(66, 3, null, null));
        HuffmanTree tree = new HuffmanTree(root);
        List<Integer> list = new ArrayList<>();
        tree.buildCodeList(null, list);
    }
}