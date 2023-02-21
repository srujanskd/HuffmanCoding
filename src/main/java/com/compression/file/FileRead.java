package com.compression.file;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Objects;

/**
 * Read each character from given input stream
 */
public class FileRead implements FIleReadable, AutoCloseable{
    private final InputStream input;
    ObjectInputStream objectInputStream = null;
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
    public int read32() throws IOException {
        int c1 = input.read();
        int c2 = input.read();
        int c3 = input.read();
        int c4 = input.read();
        if((c1 | c2 | c3 | c4) < 0) {
            throw new EOFException();
        }
        return ((c1 << 24) | (c2 << 16) | (c3 << 8) | c4);
    }

    public int readNoEof() throws IOException {
        int result = read();
        if (result != -1)
            return result;
        else
            throw new EOFException();
    }

    public Object readObj() throws IOException, ClassNotFoundException {
        objectInputStream = new ObjectInputStream(input);
        return objectInputStream.readObject();

    }

    @Override
    public void close() throws Exception {
        input.close();
        curByte = -1;
        numBitsRem = 0;
        if(objectInputStream == null)
            objectInputStream.close();
    }
}
