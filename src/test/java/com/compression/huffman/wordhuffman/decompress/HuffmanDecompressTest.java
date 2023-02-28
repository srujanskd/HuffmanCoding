package com.compression.huffman.wordhuffman.decompress;

import com.compression.file.FileRead;
import com.compression.huffman.node.HuffmanNode;
import com.compression.huffman.wordhuffman.utils.HuffmanTree;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HuffmanDecompressTest {
    @Mock
    FileRead fileRead;
    @Mock
    HuffmanNode node;

    @Mock
    HuffmanTree huffmanTree;


    @InjectMocks
    HuffmanDecompress huffDecomp;
    OutputStream out;
    @Test
    public void decompress() throws IOException {
        out = new ByteArrayOutputStream();

        huffDecomp = new HuffmanDecompress();
        huffmanTree = new HuffmanTree(new HuffmanNode("~~", null, null, null));
        when(fileRead.read()).thenReturn(2);
        when(node.isLeafNode()).thenReturn(true);
        when(node.getSymbol()).thenReturn("~~");
//        when(huffmanTree.root).thenReturn(new HuffmanNode(null, null, null, null));
        huffDecomp.decompress(huffmanTree, fileRead, out);
        Assert.assertEquals("", out.toString());

    }
}