package huffman;

import huffman.compress.HuffmanCompress;

import java.io.File;

/**
 * Main Class for the Application
 * For Compression:
 *  Usage : java HuffmanZip -compress input_file output_file key_file
 */
public class HuffmanZip {
    public static void main(String[] args) {
        if(args.length != 4) {
            System.err.println("Usage: java HuffmanZip -compress input_file output_file key_file");
            System.exit(1);
            return;
        }
        if(args[0].equals("-compress")){
            File inp = new File(args[1]);
            File out = new File((args[2]));
            HuffmanCompress huffComp = new HuffmanCompress();
            huffComp.compressFile(inp, out, args[3]);
        }

    }
}
