package com.compression.huffman.wordhuffman.utils;

import com.compression.huffman.node.HuffmanNode;
import com.compression.huffman.utils.FrequencyMap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HuffmanTreeTest {
    HuffmanNode root = new HuffmanNode(98, 33, null, null);
    @Mock
    FrequencyMap frequencyMap;
    @InjectMocks
    HuffmanTree huffmanTree = new HuffmanTree(root);
    @Test
    public void getCode() {
        Map.Entry<String, Integer> entry = Map.entry("foo", 10);
        Set<Map.Entry<String, Integer>> s = new HashSet<>();
        s.add(entry);
        when(frequencyMap.getKeyValues()).thenReturn(s);

        huffmanTree = HuffmanTree.buildHuffmanTree(frequencyMap);
        Assert.assertArrayEquals(new ArrayList<>(List.of(1)).toArray(), huffmanTree.getCode("foo").toArray());

    }

    @Test
    public void buildHuffmanTree() {
        Map.Entry<String, Integer> entry = Map.entry("foo", 10);
        Set<Map.Entry<String, Integer>> s = new HashSet<>();
        s.add(entry);
        when(frequencyMap.getKeyValues()).thenReturn(s);

        huffmanTree = HuffmanTree.buildHuffmanTree(frequencyMap);
        Assert.assertEquals(Optional.of(10), Optional.of(huffmanTree.root.getFrequency()));

    }
}