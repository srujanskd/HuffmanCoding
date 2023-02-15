package com.compression.file;

import java.io.IOException;

public interface FileWritable {
    void write(int b) throws IOException;
}
