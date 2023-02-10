package huffman.utils;

import java.io.*;
import java.util.Objects;

public class FrequencyTable implements iFrequencyTable {
    private int[] frequencies;
    public FrequencyTable(int[] frequencies){
        try {
            Objects.requireNonNull(frequencies);
        }
        catch (NullPointerException npe) {
            System.err.println("Frequencies can't be null");
            System.exit(1);
        }
        if (frequencies.length < 2)
            throw new IllegalArgumentException("At least 2 symbols needed");
        if(frequencies.length > 257) {
            throw new IllegalArgumentException("Currently we only support 8bit ASCII characters");
        }
        this.frequencies = frequencies.clone();
        for (int i = 0; i < this.frequencies.length; i++) {
            if (this.frequencies[i] < 0)
                throw new IllegalArgumentException("Negative frequency");
        }
    }

    // Returns the frequency of the specified symbol in this frequency table.
    public int get(int symbol) {
        if(symbol < 0 || symbol >= this.frequencies.length)
            throw new IllegalArgumentException("Symbol should be between the range 0 - 255");
        return this.frequencies[symbol];
    }

    public void set(int symbol, int frequency) {
        if(symbol < 0 || symbol >= this.frequencies.length)
            throw new IllegalArgumentException("Symbol should be between the range 0 - 255");
        if(frequency < 0) {
            throw new IllegalArgumentException("Negetive frequency is not allowed");
        }
        this.frequencies[symbol] = frequency;
    }

    public int size() {

        return this.frequencies.length;
    }
    public void increment(int symbol) {
        if(symbol < 0 || symbol >= this.frequencies.length)
            throw new IllegalArgumentException("Symbol should be between the range 0 - " + this.frequencies.length);
        if(this.frequencies[symbol] == Integer.MAX_VALUE){
            throw new IllegalArgumentException("Cannot increment since symbol frequency is already at Integer Max value");
        }
        this.frequencies[symbol] += 1; // increment the frequency of symbol by one
    }

    public void buildFrequencyTable(File input) {
        if(!input.exists())
            throw new IllegalArgumentException("Input file is not present");
        boolean loop = true;
        try(InputStream inp = new BufferedInputStream(new FileInputStream(input))) {
            while(loop) {
                int b = inp.read();
                if(b != -1) {
                    increment(b);

                }
                else if(b == -1) {
                    loop = false;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
