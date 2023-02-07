package huffman.compress;

import huffman.decompress.HuffmanDecompress;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class HuffmanCompressTest {
    @Test
    public void whenCompressAndDecompressIsCalled_thenBothShouldBeOfSameSize() {
        File inp = new File("pg100.txt");
        File out = new File("output.huf");
        HuffmanCompress compress = new HuffmanCompress();
        compress.compressFile(inp, out);
        File input = new File("output.huf");
        File output = new File("output.txt");
        HuffmanDecompress decompress = new HuffmanDecompress();
        decompress.decompressFile(input, output);
        Assert.assertEquals(inp.length(), output.length());

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