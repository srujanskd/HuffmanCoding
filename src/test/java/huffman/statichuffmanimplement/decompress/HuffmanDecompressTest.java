package huffman.statichuffmanimplement.decompress;

import huffman.statichuffmanimplement.compress.HuffmanCompress;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class HuffmanDecompressTest {

    @Test(expected = NullPointerException.class)
    public void whenDecompressFileIsCalled_thenExpectNullPointerException() {
        HuffmanDecompress decompress = new HuffmanDecompress();
        decompress.decompressFile(null, null);

    }
    @Test(expected = IllegalArgumentException.class)
    public void whenDecompressFileIsCalled_thenExpectIllegalArgumentException() {
        File inp = new File("notExisting.txt");
        File out = new File("output.txt");
        HuffmanDecompress decompress = new HuffmanDecompress();
        decompress.decompressFile(inp, out);

    }
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
}