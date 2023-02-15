package com.compression.huffman;

import com.compression.huffman.statichuffmanimplement.compress.HuffmanCompress;
import com.compression.huffman.statichuffmanimplement.decompress.HuffmanDecompress;

import java.io.File;

/**
 * Main Class for the Application
 * For Compression:
 *  Usage : java HuffmanZip -compress input_file output_file
 *          java HuffmanZip -decompress input_file output_file
 */
public class HuffmanZip {
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
        }
        else if(args[0].equals("-decompress")) {
            File inp = new File(args[1]);
            File out = new File((args[2]));
            HuffmanDecompress huffDecomp = new HuffmanDecompress();
            huffDecomp.decompressFile(inp, out);
        }

        Long endTime = System.currentTimeMillis();
        System.out.println("Execution time : " + (endTime - starTime) + "ms");

    }
}
