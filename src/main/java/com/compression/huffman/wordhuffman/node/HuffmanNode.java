package com.compression.huffman.wordhuffman.node;
import com.compression.huffman.node.iHuffmanNode;


public class HuffmanNode implements iHuffmanNode<String, Integer>, Comparable<HuffmanNode>{
    private final String symbol;
    private final Integer frequency;
    public final HuffmanNode leftNode;
    public final HuffmanNode rightNode;

    public HuffmanNode(String symbol, Integer frequency, HuffmanNode left, HuffmanNode right) {
        this.symbol = symbol;
        this.frequency = frequency;
        this.leftNode = left;
        this.rightNode = right;
    }

    @Override
    public Integer getFrequency() {
        if(this.frequency < 0) {
            throw new IllegalArgumentException("Frequency cannot be negative");
        }
        return this.frequency;
    }

    @Override
    public String getSymbol() {
        return this.symbol;
    }

    @Override
    public boolean isLeafNode() {
        return this.leftNode == null && this.rightNode == null;
    }

    @Override
    public int compareTo(HuffmanNode that) {
        final int comp = Integer.compare(this.frequency, that.frequency);
        if(comp != 0) return comp;
        else return this.symbol.compareTo(that.symbol);
    }
}
