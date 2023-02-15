package com.compression.file;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class FileReadTest {
    byte[] buf;
    FileRead fin;
    @Test(expected = NullPointerException.class)
    public void whenReadIsCalled_thenExpectException() {
        fin = new FileRead(null);
        try {
            fin.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void whenReadIsCalled_thenExpectFirstBit() {
        buf = new byte[]  {(byte) 0b10110000};
        fin = new FileRead(new ByteArrayInputStream(buf));
        try {
            Assert.assertEquals(1, fin.read());
            Assert.assertEquals(0, fin.read());
            Assert.assertEquals(1, fin.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void whenReadIsCalled_thenExpectNegativeOne() {
        buf = new byte[0];
        fin = new FileRead(new ByteArrayInputStream(buf));
        try {
            Assert.assertEquals(-1, fin.read());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void whenRead32Called_thenExpectInteger() {
        buf = new byte[] {(byte) 0xF, (byte) 0xA, (byte) 0x4, (byte) 0x0};
        fin = new FileRead(new ByteArrayInputStream(buf));
        try {
            Assert.assertEquals(252314624, fin.read32());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}