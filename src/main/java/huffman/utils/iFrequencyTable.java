package huffman.utils;

import java.io.File;

public interface iFrequencyTable {
    int get(int symbol);
    void set(int symbol, int frequency);
    void increment(int symbol);
    void buildFrequencyTable(File input);

}
