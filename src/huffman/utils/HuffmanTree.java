package huffman.utils;

import huffman.file.FileRead;
import huffman.node.HuffmanNode;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

public class HuffmanTree {
    public final HuffmanNode root;
    private List<List<Integer>> codeList;

    public HuffmanTree(HuffmanNode root) {
        Objects.requireNonNull(root);
        this.root = root;
        codeList = new ArrayList<>();
        // Initially Fill codes with null
        for (int i = 0; i < 257; i++)
            codeList.add(null);
    }

    public ArrayList<Integer> getCode(int symbol) {
        if(symbol < 0) {
            throw new IllegalArgumentException("Illegal symbol");
        }
        else if(codeList.get(symbol) == null) {
            throw new IllegalArgumentException("No code for given symbol:" + symbol);
        }
        else {
            return (ArrayList<Integer>) codeList.get(symbol);
        }
    }

    public static HuffmanTree buildHuffmanTree(FrequencyTable frequency) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<HuffmanNode>();
        int size = frequency.size();
        for(int i = 0; i < size; i++){
            int freq = frequency.get(i);
            if(freq > 0) {
                priorityQueue.add(new HuffmanNode(i, freq, null, null));
            }
        }

        for (int i = 0; i < frequency.size() && priorityQueue.size() < 2; i++){
            int freq = frequency.get(i);
            if(freq == 0) {
                priorityQueue.add(new HuffmanNode(i, 0, null, null));
            }
        }

        if(priorityQueue.size() < 2) {
            throw new AssertionError("Not enough nodes in priority queue");
        }

        while(priorityQueue.size() > 1) {
            HuffmanNode nodeX = priorityQueue.remove();
            HuffmanNode nodeY = priorityQueue.remove();
            HuffmanNode node = new HuffmanNode(300,
                    nodeX.getFrequency() + nodeY.getFrequency(),
                    nodeX, nodeY);
            priorityQueue.add(node);
        }
        HuffmanNode rt = priorityQueue.remove();

        HuffmanTree huffTree = new HuffmanTree(rt);
        List<Integer> prefix = new ArrayList<>();
        huffTree.buildCodeList(rt, prefix);
        return huffTree;
    }

    public void buildCodeList(HuffmanNode node, List<Integer> prefix) {
        if(node.isLeafNode()) {
            ArrayList<Integer>prefixArrayList =  new ArrayList<>(prefix);
            this.codeList.set(node.getSymbol(), prefixArrayList);
        }
        else {
            prefix.add(0);
            buildCodeList(node.leftNode, prefix);
            prefix.remove(prefix.size() - 1);

            prefix.add(1);
            buildCodeList(node.rightNode, prefix);
            prefix.remove(prefix.size() - 1);
        }
    }
}
