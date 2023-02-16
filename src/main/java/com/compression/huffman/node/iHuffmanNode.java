package com.compression.huffman.node;

public interface iHuffmanNode<S, F>{
    F getFrequency();
    S getSymbol();
    boolean isLeafNode();


}
