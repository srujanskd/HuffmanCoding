package huffman.node;

/**
 * Represents a Huffman Node
 * Each node contains;
 *      ->Symbol
 *      ->Frequency
 *      ->Left Node
 *      ->Right Node
 */
public class HuffmanNode implements Comparable<HuffmanNode> {
    private final int symbol;
    private final int frequency;
    public final HuffmanNode leftNode;
    public final HuffmanNode rightNode;

    public HuffmanNode(final int symbol,
                       final int frequency,
                       final HuffmanNode leftNode,
                       final HuffmanNode rightNode) {
        this.symbol = symbol;
        this.frequency = frequency;
        this.leftNode = leftNode;
        this.rightNode = rightNode;

    }

    public int getFrequency() {
        if(this.frequency < 0) {
            throw new IllegalArgumentException("Frequency cannot be negative");
        }
        return this.frequency;
    }
    public int getSymbol() {
        if(this.symbol < 0) {
            throw new IllegalArgumentException("Illegal Value for symbol");
        }

        return this.symbol;
    }

    //checks weather the current node is leaf node or not
    public boolean isLeafNode() {
        if(this.leftNode == null && this.rightNode == null) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(HuffmanNode that) {
        final int comp = Integer.compare(this.frequency, that.frequency);
        if(comp != 0) return comp;
        else return Integer.compare(this.symbol, that.symbol);
    }

}
