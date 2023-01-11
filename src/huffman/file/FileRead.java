package huffman.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Read each character from given input stream
 */
public class FileRead implements AutoCloseable{
    private InputStream input;
    private int curByte;
    private int numBitsRem;

    // Constructor
    public FileRead(InputStream inp) {
        input = Objects.requireNonNull(inp);
        curByte = 0;
        numBitsRem = 0;
    }

    //Reads each character from input stream and returns it
    public int read() throws IOException {
        if (curByte == -1)
            return -1;
        if (numBitsRem == 0) {
            curByte = input.read();
            if (curByte == -1)
                return -1;
            numBitsRem = 8;
        }
        if (numBitsRem <= 0)
            throw new AssertionError();
        numBitsRem--;
        return (curByte >>> numBitsRem) & 1;
    }

    @Override
    public void close() throws Exception {
        input.close();
        curByte = -1;
        numBitsRem = 0;
    }
}
