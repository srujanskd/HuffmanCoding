package huffman.file;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class FileWriteTest {
    ByteArrayOutputStream byteArrayOutputStream;
    FileWrite output;

    @Test
    public void when8bitIsWrittenIntoFile_expectSuccessfulWrite() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream(0);
        output = new FileWrite(byteArrayOutputStream);
        int[] a = new int[] {0, 1, 1, 1, 0, 1, 1, 1};
        for(int i = 0; i < 8; i++) {
            output.write(a[i]);
        }
        Assert.assertEquals("w", byteArrayOutputStream.toString());
    }

    @Test
    public void whenLessThan8bitIsWrittenIntoFile_expectSuccessfulWrite() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream(0);
        output = new FileWrite(byteArrayOutputStream);
        int[] a = new int[] {0, 0, 1, 1, 0};
        for(int i = 0; i < 5; i++) {
            output.write(a[i]);
        }
        Assert.assertEquals("", byteArrayOutputStream.toString());
        output.write(1);
        output.write(1);
        output.write(0);
        Assert.assertEquals("6", byteArrayOutputStream.toString());
    }

    @Test
    public void whenMoreThan8bitIsWrittenIntoFile_expectSuccessfulWrite() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream(0);
        output = new FileWrite(byteArrayOutputStream);
        int[] a = new int[] {0, 0, 1, 1, 0, 1, 1, 0, 0, 1};
        for(int i = 0; i < 10; i++) {
            output.write(a[i]);
        }
        Assert.assertEquals("6", byteArrayOutputStream.toString());
        int[] b = new int[] {1, 1, 1, 0, 1, 0};
        for(int i = 0; i < 6; i++) {
            output.write(b[i]);
        }
        Assert.assertEquals("6z", byteArrayOutputStream.toString());
    }
    @Test
    public void whenLessThan8bitIsWrittenIntoFileAtTheEnd_expectSuccessfulWriteWithPadding() throws Exception {
        byteArrayOutputStream = new ByteArrayOutputStream(0);
        output = new FileWrite(byteArrayOutputStream);
        int[] a = new int[] {0, 1, 1, 1, 0, 1};
        for(int i = 0; i < 6; i++) {
            output.write(a[i]);
        }
        output.close();
        Assert.assertEquals("t", byteArrayOutputStream.toString());
    }



}
