package com.compression.huffman;

import com.compression.huffman.wordhuffman.compress.HuffmanCompress;
import com.compression.huffman.wordhuffman.decompress.HuffmanDecompress;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main Class for the Application
 * For Compression:
 *  Usage : java HuffmanZip -compress input_file output_file
 *          java HuffmanZip -decompress input_file output_file
 */
public class WordHuffman {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String[] args) {
//        LogManager.getLogManager().reset();
        Runtime runtime = Runtime.getRuntime();
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
            LOGGER.log(Level.INFO, "Input file size : {0} Bytes", inLen);
            LOGGER.log(Level.INFO, "Compressed file size : {0} Bytes", outLen);
            LOGGER.log(Level.INFO, "Compression Percentage : {0} %", (100 - ((double)outLen / inLen * 100)));

        }
        else if(args[0].equals("-decompress")) {
            File inp = new File(args[1]);
            File out = new File((args[2]));
            HuffmanDecompress huffDecomp = new HuffmanDecompress();
            huffDecomp.decompressFile(inp, out);
            LOGGER.log(Level.INFO, "Input file size : {0} Bytes", inp.length());
            LOGGER.log(Level.INFO, "Output file size : {0} Bytes", out.length());

        }

        Long endTime = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "Execution time : {0} ms", (endTime - starTime));
        LOGGER.log(Level.INFO, "Total memory Used : {0} MB" ,((runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)));

    }
}
