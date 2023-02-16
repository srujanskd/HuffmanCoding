package com.compression.huffman.utils;

import java.util.List;

public interface iHuffmanTree<T>{
    List<Integer> getCode(T symbol);
}
