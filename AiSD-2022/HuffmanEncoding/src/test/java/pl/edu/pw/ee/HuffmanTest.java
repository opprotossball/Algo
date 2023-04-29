package pl.edu.pw.ee;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HuffmanTest {
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
    public void shouldCompressCorrectly() throws IOException {
        //given
        String path = "src/test/resources/ShouldCompressCorrectly";
        int[] expectedCodes = {8, 176, 218, 192, 166, 128};

        //when
        huffman.huffman(path, true);

        //then
        File compressed = new File(path + "/compressed.brrt");
        InputStreamReader streamReader = new InputStreamReader(new FileInputStream(compressed), CHARSET);
        BufferedReader reader = new BufferedReader(streamReader);
        String line = reader.readLine();
        assertEquals(expectedCodes.length + 1, compressed.length());
        for (int i = 0; i < expectedCodes.length; i++) {
            int expected = expectedCodes[i];
            int result = (int) line.charAt(i);
            assertEquals(expected, result);
        }
        reader.close();
    }

    @Test
    public void shouldCompressThenDecompressCorrectly() {
        //given
        String path = "src/test/resources/niemanie_refren_censored";

        //when
        int compressedLength = huffman.huffman(path, true);
        int decompressedLength = huffman.huffman(path, false);

        //then
        File origin = new File(path + "/origin.txt");
        File compressed = new File(path + "/compressed.brrt");
        File decompressed = new File(path + "/decompressed.txt");
        assertTrue(compareFiles(origin, decompressed));
        assertEquals(compressedLength, 8 * compressed.length());
        assertEquals(decompressedLength, decompressed.length());
    }

    @Test
    public void shouldCompressThenDecompressCorrectlyWithPolishCharacters() {
        //given
        String path = "src/test/resources/PolishCharacters";

        //when
        huffman.huffman(path, true);
        huffman.huffman(path, false);

        //then
        File origin = new File(path + "/origin.txt");
        File decompressed = new File(path + "/decompressed.txt");
        assertTrue(compareFiles(origin, decompressed));
    }

    @Test
    public void shouldCompressAndDecompressFileWith1LetterCorrectly() {
        //given
        String path = "src/test/resources/OneLetter";

        //when
        int compressedLength = huffman.huffman(path, true);
        int decompressedLength = huffman.huffman(path, false);

        //then
        File origin = new File(path + "/origin.txt");
        File compressed = new File(path + "/compressed.brrt");
        File decompressed = new File(path + "/decompressed.txt");
        assertTrue(compareFiles(origin, decompressed));
        assertEquals(compressedLength, 8 * compressed.length());
        assertEquals(decompressedLength, decompressed.length());
    }

    @Test(expected = RuntimeException.class)
    public void throwsCorrectExceptionWhenCompressedFileIsCut() {
        //given
        String path = "src/test/resources/CorruptedCompressed";

        //when
        huffman.huffman(path, false);

        //then
        assert (false);
    }

    @Test(expected = RuntimeException.class)
    public void throwsCorrectExceptionWhenCompressNotExistingFile() {
        //given
        String path = "src/test/resources/AbsolutelyNotExistingPath";

        //when
        huffman.huffman(path, false);

        //then
        assert (false);
    }

    @Test(expected = RuntimeException.class)
    public void throwsCorrectExceptionWhenDecompressNotExistingFile() {
        //given
        String path = "src/test/resources/AbsolutelyNotExistingPath";

        //when
        huffman.huffman(path, false);

        //then
        assert (false);
    }

    @Test(expected = InvalidFileContentException.class)
    public void throwsCorrectExceptionWhenCompressEmptyFile() {
        //given
        String path = "src/test/resources/EmptyFile";

        //when
        huffman.huffman(path, true);

        //then
        assert (false);
    }

    @Test(expected = InvalidFileContentException.class)
    public void throwsCorrectExceptionWhenDecompressEmptyFile() {
        //given
        String path = "src/test/resources/EmptyFile";

        //when
        huffman.huffman(path, false);

        //then
        assert (false);
    }

    @Test(expected = InvalidFileContentException.class)
    public void decompressesFileWithOnlyTreeCorrectly() {
        //given
        String path = "src/test/resources/CompressedWithTreeOnly";

        //when
        huffman.huffman(path, false);

        //then
        assert (false);
    }

}