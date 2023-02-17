package com.compression.huffman.node;

/**
 * Represents a Huffman Node
 * Each node contains;
 *      ->Symbol
 *      ->Frequency
 *      ->Left Node
 *      ->Right Node
 */
public class HuffmanNode implements iHuffmanNode<Object>, Comparable<HuffmanNode> {
    private final Object symbol;
    private final Integer frequency;
    public final HuffmanNode leftNode;
    public final HuffmanNode rightNode;

    public HuffmanNode(final Object symbol,
                       final Integer frequency,
                       final HuffmanNode leftNode,
                       final HuffmanNode rightNode) {
        this.symbol = symbol;
        this.frequency = frequency;
        this.leftNode = leftNode;
        this.rightNode = rightNode;

    }

    public Integer getFrequency() {
        if(this.frequency < 0) {
            throw new IllegalArgumentException("Frequency cannot be negative");
        }
        return this.frequency;
    }
    public Object getSymbol() {
        if(this.symbol instanceof Integer) {
            if ((Integer)this.symbol < 0) {
                throw new IllegalArgumentException("Illegal Value for symbol");
            }
        }

        return this.symbol;
    }

    //checks weather the current node is leaf node or not
    public boolean isLeafNode() {
        return this.leftNode == null && this.rightNode == null;
    }

    @Override
    public int compareTo(HuffmanNode that) {
        final int comp = Integer.compare(this.frequency, that.frequency);
        if(comp != 0) return comp;
        else {
            if(this.symbol instanceof Integer)
                return Integer.compare((Integer)this.symbol, (Integer)that.symbol);
            else return ((String)this.symbol).compareTo((String) that.symbol);
        }
    }

}
