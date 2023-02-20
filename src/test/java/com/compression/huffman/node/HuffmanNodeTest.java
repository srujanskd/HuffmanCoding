package com.compression.huffman.node;

import org.junit.Assert;

import java.util.Optional;

public class HuffmanNodeTest {
    private final HuffmanNode leafNode1 = new HuffmanNode(78, 66, null, null);
    private final HuffmanNode leafNode2 = new HuffmanNode(101, 4, null, null);

    private final HuffmanNode node = new HuffmanNode(65, 3, leafNode1, leafNode2);
    private final HuffmanNode illegalNode = new HuffmanNode(-1, -2, null, null);


    @org.junit.Test
    public void whenGetFrequencyIsCalled_ThenFrequencyShouldMatch() {
        int nodeFreq = node.getFrequency();
        Assert.assertEquals(nodeFreq, 3);
        Assert.assertEquals(Optional.ofNullable(leafNode2.getFrequency()), Optional.ofNullable(4));
        Assert.assertEquals(Optional.ofNullable(leafNode1.getFrequency()), Optional.of(66));
    }
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void whenGetFrequencyIsCalled_thenExceptionExpected() {
        illegalNode.getFrequency();
    }

    @org.junit.Test
    public void whenGetSymbolIsCalled_ThenSymbolShouldMatch() {
        int nodeSymbol = (int) node.getSymbol();
        Assert.assertEquals(nodeSymbol, 65);
        Assert.assertEquals(Optional.ofNullable(leafNode1.getSymbol()), Optional.ofNullable(78));
        Assert.assertEquals(Optional.ofNullable(leafNode2.getSymbol()), Optional.ofNullable(101));
    }
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void whenGetSymbolIsCalled_thenExceptionExpected() {
        illegalNode.getSymbol();
    }

    @org.junit.Test
    public void whenIsLeafNodeIsCalled_ThenLeafNodeShouldReturnTrue() {
        Assert.assertFalse(node.isLeafNode());
        Assert.assertTrue(leafNode1.isLeafNode());
        Assert.assertTrue(leafNode2.isLeafNode());
    }
}