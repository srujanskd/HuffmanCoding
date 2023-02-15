package com.compression.huffman.canonical.utils;

import com.compression.huffman.node.HuffmanNode;
import com.compression.huffman.utils.HuffmanTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


    public final class CanonicalCode {

    private int[] codeLengths;

    public CanonicalCode(int[] codeLens) {
        // Check basic validity
        Objects.requireNonNull(codeLens);
        if (codeLens.length < 2)
            throw new IllegalArgumentException("At least 2 symbols needed");
        for (int cl : codeLens) {
            if (cl < 0)
                throw new IllegalArgumentException("Illegal code length");
        }

        codeLengths = codeLens.clone();
//        System.arraycopy(codeLens, 0, codeLengths, 0, codeLens.length);
    }

    public CanonicalCode(HuffmanTree tree, int symbolLimit) {
        Objects.requireNonNull(tree);
        if (symbolLimit < 2)
            throw new IllegalArgumentException("At least 2 symbols needed");
        codeLengths = new int[symbolLimit];
        buildCodeLengths(tree.root, 0);
    }


    // Recursive helper method for the above constructor.
    private void buildCodeLengths(HuffmanNode node, int depth) {
        if (!node.isLeafNode()) {
            buildCodeLengths(node.leftNode, depth + 1);
            buildCodeLengths(node.rightNode, depth + 1);
        } else if (node.isLeafNode()) {
            int symbol = node.getSymbol();
            if (symbol >= codeLengths.length)
                throw new IllegalArgumentException("Symbol exceeds symbol limit");
            // Note: CodeTree already has a checked constraint that disallows a symbol in multiple leaves
            if (codeLengths[symbol] != 0)
                throw new AssertionError("Symbol has more than one code");
            codeLengths[symbol] = depth;
        } else {
            throw new AssertionError("Illegal node type");
        }
    }



    /*---- Various methods ----*/

    /**
     * Returns the symbol limit for this canonical Huffman code.
     * Thus this code covers symbol values from 0 to symbolLimit&minus;1.
     * @return the symbol limit, which is at least 2
     */
    public int getSymbolLimit() {
        return codeLengths.length;
    }


    /**
     * Returns the code length of the specified symbol value. The result is 0
     * if the symbol has node code; otherwise the result is a positive number.
     * @param symbol the symbol value to query
     * @return the code length of the symbol, which is non-negative
     * @throws IllegalArgumentException if the symbol is out of range
     */
    public int getCodeLength(int symbol) {
        if (symbol < 0 || symbol >= codeLengths.length)
            throw new IllegalArgumentException("Symbol out of range");
        return codeLengths[symbol];
    }


    /**
     * Returns the canonical code tree for this canonical Huffman code.
     * @return the canonical code tree
     */
    public HuffmanTree toCodeTree() {
        List<HuffmanNode> nodes = new ArrayList<HuffmanNode>();
        for (int i = max(codeLengths); i >= 0; i--) {  // Descend through code lengths
            if (nodes.size() % 2 != 0)
                throw new AssertionError("Violation of canonical code invariants");
            List<HuffmanNode> newNodes = new ArrayList<HuffmanNode>();

            // Add leaves for symbols with positive code length i
            if (i > 0) {
                for (int j = 0; j < codeLengths.length; j++) {
                    if (codeLengths[j] == i)
                        newNodes.add(new HuffmanNode(j, 0, null, null));
                }
            }

            // Merge pairs of nodes from the previous deeper layer
            for (int j = 0; j < nodes.size(); j += 2)
                newNodes.add(new HuffmanNode(Math.min(nodes.get(j).getSymbol(), nodes.get(j+1).getSymbol()),
                        0, nodes.get(j), nodes.get(j + 1)));
            nodes = newNodes;
        }

        if (nodes.size() != 1)
            throw new AssertionError("Cannot Build Tree");
        return new HuffmanTree(nodes.get(0));
    }


    // Returns the maximum value in the given array, which must have at least 1 element.
    private static int max(int[] array) {
        int result = array[0];
        for (int x : array)
            result = Math.max(x, result);
        return result;
    }

}
