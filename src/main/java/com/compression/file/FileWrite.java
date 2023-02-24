package com.compression.file;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Objects;

// Writes into output stream
public class FileWrite implements FileWritable, AutoCloseable{
    private final OutputStream output;
    ObjectOutputStream objectOutputStream = null;

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
    public void write32(int val) throws IOException {
        byte[] bytes = new byte[4];

        bytes[3] = (byte) (val & 0xFF);
        bytes[2] = (byte) (val >>> 8 & 0xFF);
        bytes[1] = (byte) (val >>> 16 & 0xFF);
        bytes[0] = (byte) (val >>> 24 & 0xFF);
        output.write(bytes);
    }

    public void writeObject(Object obj) throws IOException {
        objectOutputStream = new ObjectOutputStream(output);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();


    }
    @Override
    public void close() throws Exception {
        while (numBitsFilled != 0)
            write(0);
        output.close();
        if(objectOutputStream != null)
            objectOutputStream.close();

    }
}
