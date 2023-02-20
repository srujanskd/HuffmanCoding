package com.compression.huffman.utils;

import com.compression.huffman.node.HuffmanNode;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HuffmanTreeTest {
    @Mock
    private FrequencyTable freq;
    private final HuffmanNode root = new HuffmanNode(12,12, null, null);
    @InjectMocks
    HuffmanTree huffTree = new HuffmanTree(root);
    @Test
    public void whenBuildTreeIsCalled_thenTreeShouldBeSame() {
        when(freq.size()).thenReturn(257);
        when(freq.get(eq(72))).thenReturn(1);
        when(freq.get(eq(101))).thenReturn(1);
        when(freq.get(eq(108))).thenReturn(2);
        when(freq.get(eq(111))).thenReturn(1);

        huffTree = HuffmanTree.buildHuffmanTree(freq);
        Assert.assertEquals(Optional.of(5), Optional.of(huffTree.root.getFrequency()));
        Assert.assertEquals(Optional.of(111), Optional.of(huffTree.root.rightNode.leftNode.getSymbol()));
    }

    @Test
    public void whenBuildTreeIsCalledWithZeroFreq_thenTreeShouldBeValid() {
        when(freq.get(anyInt())).thenReturn(0);
        when(freq.size()).thenReturn(257);
        huffTree = HuffmanTree.buildHuffmanTree(freq);
        Assert.assertEquals(Optional.of(0), Optional.of(huffTree.root.getFrequency()));
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

        List<Integer> list = new ArrayList<>();
        huffTree.buildCodeList(root, list);

        Assert.assertArrayEquals(new ArrayList<>(List.of(1)).toArray(), huffTree.getCode(66).toArray());
        Assert.assertArrayEquals(new ArrayList<>(List.of(0)).toArray(), huffTree.getCode(45).toArray());
    }
    @Test(expected = NullPointerException.class)
    public void whenBuildCodeListIsCalledWithNullObj_thenShouldReturnException() {
        HuffmanTree tree = new HuffmanTree(root);
        List<Integer> list = new ArrayList<>();
        tree.buildCodeList(null, list);
    }
}