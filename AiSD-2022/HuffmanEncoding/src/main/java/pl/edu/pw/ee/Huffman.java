package pl.edu.pw.ee;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Huffman {
    private HashMap<Character, Integer> counts;
    private HuffmanTree tree;
    private Dictionary codes;
    private static final Charset CHARSET = StandardCharsets.ISO_8859_1;

    public int huffman(String pathToRootDir, boolean compress) {
        if (compress) {
            return (int) compress(pathToRootDir);
        } else {
            return (int) decompress(pathToRootDir);
        }
    }

    private long compress(String pathToRootDir) {
        try {
            countOccurrence(pathToRootDir);
        } catch (IOException e) {
            throw new IllegalArgumentException("File in given directory cannot be opened");
        }

        tree = new HuffmanTree(8);
        addNodesToTree();
        tree.buildTreeFromQueue();
        codes = tree.getCodes();

        try {
            return writeFromOriginToCompressed(pathToRootDir);
        } catch (IOException e) {
            throw new IllegalArgumentException("File in given directory cannot be opened");
        }
    }

    private long decompress(String pathToRootDir) {
        tree = new HuffmanTree();
        int charsRead = tree.buildTreeFromFileHeader(pathToRootDir + "/compressed.brrt", CHARSET);
        codes = tree.getCodes();
        try {
            return writeFromCompressedToDecompressed(pathToRootDir, charsRead);
        } catch (IOException e) {
            throw new RuntimeException("Compressed or decompressed file cannot be opened");
        }
    }

    private void countOccurrence(String pathToRootDir) throws IOException {
        File origin = new File(pathToRootDir + "/origin.txt");
        InputStreamReader reader = new InputStreamReader(new FileInputStream(origin), CHARSET);
        counts = new HashMap<>();
        int i;
        char c;

        while ((i = reader.read()) != -1) {
            c = (char) i;
            Integer count = counts.remove(c);
            if (count != null) {
                counts.put(c, count + 1);
            } else {
                counts.put(c, 1);
            }
        }
    }

    private void addNodesToTree() {
        for (Map.Entry<Character, Integer> entry : counts.entrySet()) {
            Character character = entry.getKey();
            Integer count = entry.getValue();
            tree.addNodeToQueue(new Node(character, count));
        }
    }

    private long writeFromCompressedToDecompressed(String pathToRootDir, int charsRead) throws IOException {
        File compressed = new File(pathToRootDir + "/compressed.brrt");
        File decompressed = new File(pathToRootDir + "/decompressed.txt");
        InputStreamReader reader = new InputStreamReader(new FileInputStream(compressed), CHARSET);
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(decompressed), CHARSET);

        int previous, current, next;
        int usefulBits = 8;
        int index = 0;
        Node node = tree.getRoot();

        current = reader.read();
        next = reader.read();

        do {
            previous = current;
            current = next;
            next = reader.read();
            index++;
            if (index <= charsRead) {
                continue;
            }
            HuffByte huffByte = new HuffByte((char) previous);
            if (next == -1) {
                usefulBits = current;
            }
            for (int i = 0; i < Math.min(usefulBits, 8); i++) {
                if (node.isLeaf()) {
                    writer.write(node.getCharacter());
                    node = tree.getRoot();
                }
                if (huffByte.getBit(i)) {
                    node = node.getRight();
                } else {
                    node = node.getLeft();
                }
                if (node == null) {
                    throw new InvalidFileContentException("File does not contain properly compressed data");
                }
            }
        } while (next != -1);

        if (node.isLeaf()) {
            writer.write(node.getCharacter());
        }

        reader.close();
        writer.close();
        return decompressed.length();
    }

    private long writeFromOriginToCompressed(String pathToRootDir) throws IOException {
        File origin = new File(pathToRootDir + "/origin.txt");
        File compressed = new File(pathToRootDir + "/compressed.brrt");
        InputStreamReader reader = new InputStreamReader(new FileInputStream(origin), CHARSET);
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(compressed), CHARSET);

        writer.write(tree.encodeTree());

        int i;
        char c;
        HuffByte encoding = new HuffByte();
        int bitIndex = 0;

        while ((i = reader.read()) != -1) {
            c = (char) i;
            for (Boolean bit : codes.getCode(c)) {
                if (bitIndex == 8) {
                    writer.write(encoding.toChar());
                    encoding = new HuffByte();
                    bitIndex = 0;
                }
                if (bit) {
                    encoding.setBit(bitIndex);
                }
                bitIndex++;
            }
        }
        writer.write(encoding.toChar());
        writer.write((char) (bitIndex));

        writer.close();
        reader.close();
        return compressed.length() * 8;
    }

}