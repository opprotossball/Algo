package pl.edu.pw.ee;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PerformanceTest {
    private Huffman huffman;
    private static final Charset CHARSET = StandardCharsets.ISO_8859_1;

    private boolean compareFiles(File f1, File f2) {
        try {
            InputStreamReader reader1 = new InputStreamReader(new FileInputStream(f1), CHARSET);
            InputStreamReader reader2 = new InputStreamReader(new FileInputStream(f2), CHARSET);
            int c1, c2;
            int i = 0;
            boolean equals = true;
            do {
                c1 = reader1.read();
                c2 = reader2.read();
                i++;
                if (c1 != c2) {
                    equals = false;
                    System.out.println("Files differ at position: " + i);
                    break;
                }
            } while (c1 != -1);
            reader1.close();
            reader2.close();
            return equals;
        } catch (IOException e) {
            throw new RuntimeException("Cannot compare files");
        }
    }

    @Before
    public void setup() {
        huffman = new Huffman();
    }

    @Test
    public void performanceTest() {
        //given
        String path = "src/test/resources/PerformanceTest";

        //when
        huffman.huffman(path, true);
        huffman.huffman(path, false);

        //then
        File origin = new File(path + "/origin.txt");
        File compressed = new File(path + "/compressed.brrt");
        File decompressed = new File(path + "/decompressed.txt");
        System.out.println("Original file size: " + origin.length());
        System.out.println("Compressed file size: " + compressed.length());
        System.out.println("Compression coefficient: " + String.format("%.2f", ((double)compressed.length() / (double)origin.length()) * 100) + "%");
        assert (compareFiles(origin, decompressed));
    }
}
