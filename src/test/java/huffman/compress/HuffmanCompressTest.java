package huffman.compress;

import org.junit.Test;

import java.io.File;

public class HuffmanCompressTest {

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