package com.compression.huffman.node;

public interface iHuffmanNode<S>{
    Integer getFrequency();
    S getSymbol();
    boolean isLeafNode();
}
