package huffman.file;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

// Writes into output stream
public class FileWrite implements AutoCloseable{
    private OutputStream output;

    private int currentByte;

    // Number of accumulated bits in the current byte, always between 0 and 7 (inclusive).
    private int numBitsFilled;
    // Constructor
    public FileWrite(OutputStream out) {
        output = Objects.requireNonNull(out);
        currentByte = 0;
        numBitsFilled = 0;
    }

    /*
	 	Writes a bit(0 or 1) to the stream.
	 */
    public void write(int b) throws IOException {
        if (b != 0 && b != 1)
            throw new IllegalArgumentException("Argument must be 0 or 1");
        currentByte = (currentByte << 1) | b;
        numBitsFilled++;
        if (numBitsFilled == 8) {
            output.write(currentByte);
            currentByte = 0;
            numBitsFilled = 0;
        }
    }
    @Override
    public void close() throws Exception {
        while (numBitsFilled != 0)
            write(0);
        output.close();

    }
}
