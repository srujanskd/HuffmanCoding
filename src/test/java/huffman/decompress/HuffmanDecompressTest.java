package huffman.decompress;

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
}