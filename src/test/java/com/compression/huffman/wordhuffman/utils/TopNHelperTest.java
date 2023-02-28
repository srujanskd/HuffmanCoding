package com.compression.huffman.wordhuffman.utils;

import com.compression.huffman.utils.FrequencyMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TopNHelperTest {
    TopNHelper tp;
    @Mock
    FrequencyMap fm;

    @Test(expected = NullPointerException.class)
    public void bestTopNFrequencyException1() {
        tp = new TopNHelper(null);
    }
    @Test(expected = NullPointerException.class)
    public void bestTopNFrequencyException2() {
        tp = new TopNHelper(fm);
        tp.bestTopNFrequency(null, null);

    }
}