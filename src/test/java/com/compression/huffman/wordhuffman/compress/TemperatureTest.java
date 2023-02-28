package com.compression.huffman.wordhuffman.compress;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TemperatureTest {
    Temperature t;

    @Test
    public void splitArray() {
        t = new Temperature(1000, 0.3, 4);
        List<List<Double>> a = new ArrayList<>();
        a.add(new ArrayList<>(List.of(1000.0)));
        a.add(new ArrayList<>(List.of(3.3222591362126246)));
        a.add(new ArrayList<>(List.of(1.663893510815308)));
        a.add(new ArrayList<>(List.of(1.109877913429523)));

        Assert.assertArrayEquals(a.toArray(), t.splitArray.toArray());

    }
}