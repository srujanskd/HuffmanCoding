package com.compression.huffman;
import com.compression.huffman.wordhuffman.compress.HuffmanCompress;
import com.compression.huffman.wordhuffman.decompress.HuffmanDecompress;


import java.io.File;

/**
 * Main Class for the Application
 * For Compression:
 *  Usage : java HuffmanZip -compress input_file output_file
 *          java HuffmanZip -decompress input_file output_file
 */
public class WordHuffman {
    public static void main(String[] args) {
        Long starTime = System.currentTimeMillis();
        if(args.length != 3) {
            System.err.println("Usage: java HuffmanZip -compress input_file output_file");
            System.err.println("Usage: java HuffmanZip -decompress input_file output_file");
            System.exit(1);
            return;
        }
        if(args[0].equals("-compress")){
            File inp = new File(args[1]);
            File out = new File((args[2]));
            HuffmanCompress huffComp = new HuffmanCompress();
            huffComp.compressFile(inp, out);
            long inLen = inp.length();
            long outLen = out.length();
            System.out.println("Input file size :" + inLen + "Bytes");
            System.out.println("Compressed file size :" + outLen + "Bytes");
            System.out.println("Compression Percentage :" + (100 - ((double)outLen / inLen * 100)) + "%");

        }
        else if(args[0].equals("-decompress")) {
            File inp = new File(args[1]);
            File out = new File((args[2]));
            HuffmanDecompress huffDecomp = new HuffmanDecompress();
            huffDecomp.decompressFile(inp, out);
            System.out.println("Input file size :" + inp.length() + "Bytes");
            System.out.println("Output file size :" + out.length() + "Bytes");
        }

        Long endTime = System.currentTimeMillis();
        System.out.println("Execution time : " + (endTime - starTime) + "ms");

    }
}
