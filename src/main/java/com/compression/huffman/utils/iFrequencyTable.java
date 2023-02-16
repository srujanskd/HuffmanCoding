package com.compression.huffman.utils;
import java.io.InputStream;

public interface iFrequencyTable<T>{
    int get(T symbol);
    void set(T symbol, int frequency);
    void increment(T symbol);
    void buildFrequencyTable(InputStream input);

}
