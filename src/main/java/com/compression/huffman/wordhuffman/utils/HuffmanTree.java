package com.compression.huffman.wordhuffman.utils;

import com.compression.huffman.utils.FrequencyMap;
import com.compression.huffman.node.HuffmanNode;
import com.compression.huffman.utils.iHuffmanTree;

import java.util.*;


public class HuffmanTree implements iHuffmanTree<String>{

    public final HuffmanNode root;
    private final Map<String , ArrayList<Integer>> codeList;
    public HuffmanTree(HuffmanNode root) {
        Objects.requireNonNull(root);
        this.root = root;
        codeList = new HashMap<>();
    }
    @Override
    public List<Integer> getCode(String symbol) {
        if(codeList.containsKey(symbol)) {
            return codeList.get(symbol);
        }
        else {
            throw new IllegalArgumentException("Symbol could not be found in CodeList");
        }
    }

    public static HuffmanTree buildHuffmanTree(FrequencyMap frequency) {
        Objects.requireNonNull(frequency);
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
        for(Map.Entry<String, Integer> i :  frequency.getKeyValues()){
            int freq = i.getValue();
            if(freq > 0) {
                priorityQueue.add(new HuffmanNode(i.getKey(), freq, null, null));
            }
        }
        String[] extra = new String[] {"$EXTRA1$", "$EXTR21$", "$EXTRA3$"};
        int i = 0;
        while(priorityQueue.size() < 2) {
            priorityQueue.add(new HuffmanNode(extra[i++], 0, null, null));
        }

        while(priorityQueue.size() > 1) {
            HuffmanNode nodeX = priorityQueue.remove();
            HuffmanNode nodeY = priorityQueue.remove();

            HuffmanNode node = new HuffmanNode(min((String) nodeX.getSymbol(), (String) nodeY.getSymbol()),
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

    private void buildCodeList(HuffmanNode node, List<Integer> prefix) {
        Objects.requireNonNull(node);
        Objects.requireNonNull(prefix);

        if(node.isLeafNode()) {
            ArrayList<Integer>prefixArrayList =  new ArrayList<>(prefix);
            this.codeList.put((String) node.getSymbol(), prefixArrayList);
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

    public double averageHuffmanBits(FrequencyMap frequency) {
        double avg = 0;
        long totalFreq = 0;
        for(Map.Entry<String, Integer> i :  frequency.getKeyValues()) {
            if(i.getValue() != 0) {
                avg += ((i.getValue() * this.codeList.get(i.getKey()).size())/i.getKey().length());
                totalFreq += i.getValue();
            }

        }
        return avg / totalFreq;
    }

    private static String min(String s1, String s2) {
        int ans = s1.compareTo(s2);
        if(ans <= 0) {
            return s1;
        }
        else {
            return s2;
        }

    }
}
