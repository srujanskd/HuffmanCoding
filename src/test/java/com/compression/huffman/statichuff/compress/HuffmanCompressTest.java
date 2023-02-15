package com.compression.huffman.statichuff.compress;

import com.compression.huffman.statichuff.decompress.HuffmanDecompress;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HuffmanCompressTest {
    @Test
    public void whenCompressAndDecompressIsCalled_thenBothShouldBeOfSameSize() {
        File inp = new File("test.txt");
        File out = new File("output.huf");
        try {
            if(inp.createNewFile()) {
                FileWriter fw = new FileWriter(inp);
                fw.write("Hello, This is a test file!! %%$$!=");
                fw.close();
            }
            else {
                System.out.println("File already exists");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HuffmanCompress compress = new HuffmanCompress();
        compress.compressFile(inp, out);
        File input = new File("output.huf");
        File output = new File("output.txt");
        HuffmanDecompress decompress = new HuffmanDecompress();
        decompress.decompressFile(input, output);
        Assert.assertEquals(inp.length(), output.length());
        input.delete();
        output.delete();
        inp.delete();
        out.delete();

    }

    @Test(expected = NullPointerException.class)
    public void whenCompressFileIsCalled_thenExpectError() {
        HuffmanCompress compress = new HuffmanCompress();
        compress.compressFile(null, null);

    }


    @Test(expected = NullPointerException.class)
    public void whenCompressFileIsCalled_thenExpectNullPointerException() {
        HuffmanCompress compress = new HuffmanCompress();
        compress.compressFile(null, null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCompressFileIsCalled_thenExpectIllegalArgumentException() {
        File inp = new File("notExisting.txt");
        File out = new File("output.huf");
        HuffmanCompress compress = new HuffmanCompress();
        compress.compressFile(inp, out);

    }
}