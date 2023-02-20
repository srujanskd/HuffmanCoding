package com.compression.huffman.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class FrequencyTableTest {
    FrequencyTable frequencyTable = new FrequencyTable(new int[256]);
     final int[] testArr = new int[256];

    public void fillFreq() {
        for(int i = 0; i < frequencyTable.size(); i++) {
            int randFreq = (int) Math.floor(Math.random() * Integer.MAX_VALUE);
            frequencyTable.set(i, randFreq);
            testArr[i] = randFreq;
        }
    }
    @Test
    public void whenGetIsCalled_thenSymbolFreqIsExpected() {
        fillFreq();
        Assert.assertEquals(testArr[5], frequencyTable.get(5));
        Assert.assertEquals(testArr[255], frequencyTable.get(255));

    }

    @Test(expected = IllegalArgumentException.class)
    public void whenGetIsCalled_thenExceptionIsExpected() {
        fillFreq();
        frequencyTable.get(256);
        frequencyTable.get(-8);
    }

    @Test
    public void whenSetIsCalled_thenValueShouldBeSet() {
        frequencyTable.set(45, 32);
        Assert.assertEquals(frequencyTable.get(45), 32);
        frequencyTable.set(255, 900);
        Assert.assertEquals(frequencyTable.get(255), 900);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenSetIsCalled_ExceptionIsExpected() {
        frequencyTable.set(344, -9);
        frequencyTable.set(-7, 122);
        frequencyTable.set(-265, -55);
    }

    @Test
    public void whenIncrementIsCalled_thenOneShouldBeAddedToSymbolFrequency() {
        int freq = frequencyTable.get(78);
        frequencyTable.increment(78);
        Assert.assertEquals(freq + 1, frequencyTable.get(78));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenIncrementIsCalled_thenExceptionExpected() {
        frequencyTable.set(91, Integer.MAX_VALUE);
        frequencyTable.increment(91);
    }

    @Test
    public void whenBuildFrequencyTableIsCalled_thenExpectCorrectFrequencyFromFile() {
        String testString = "Hello";
        InputStream testStream = new ByteArrayInputStream(testString.getBytes());
        frequencyTable.buildFrequencyTable(testStream);
        Assert.assertEquals(2, frequencyTable.get(108));
    }
    @Test(expected = NullPointerException.class)
    public void whenBuildFrequencyTableIsCalled_thenExpectException() {
        frequencyTable.buildFrequencyTable(null);
    }
}