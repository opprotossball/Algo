import org.junit.Test;
import java.io.File;

public class PerformanceTest extends myTest {

    @Test
    public void worstCasePerformanceTest() {
        //given
        String path = "src/test/resources/PerformanceTests/worstCasePerformance/";
        LZW lzw = new LZW();

        //when
        lzw.compress(path + "worstCasePerformance.txt");
        lzw.decompress(path + "compressed.trrk");
        File origin = new File(path + "worstCasePerformance.txt");
        File compressed = new File(path + "compressed.trrk");
        double compression = ((double) compressed.length() / (double) origin.length()) * 100;

        //then
        System.out.println("Compressed large file weights " + String.format("%.2f", compression) + "% of original file");
        assert(compareFiles(path + "worstCasePerformance.txt", path + "decompressed.txt"));
    }

    @Test
    public void mediumPerformanceTest() {
        //given
        String path = "src/test/resources/PerformanceTests/mediumPerformance/mediumPerformance.txt";
        LZW lzw = new LZW();

        //when
        lzw.compress(path);
        lzw.decompress(path + "/../compressed.trrk");
        File origin = new File(path);
        File compressed = new File(path + "/../compressed.trrk");
        double compression = ((double) compressed.length() / (double) origin.length()) * 100;

        //then
        System.out.println("Compressed large file weights " + String.format("%.2f", compression) + "% of original file");
        assert(compareFiles(path, path + "/../decompressed.txt"));
    }

    @Test
    public void largePerformanceTest() {
        //given
        String path = "src/test/resources/PerformanceTests/largePerformance/largePerformance.txt";
        LZW lzw = new LZW();

        //when
        lzw.compress(path);
        lzw.decompress(path + "/../compressed.trrk");
        File origin = new File(path);
        File compressed = new File(path + "/../compressed.trrk");
        double compression = ((double) compressed.length() / (double) origin.length()) * 100;

        //then
        System.out.println("Compressed large file weights " + String.format("%.2f", compression) + "%  of original file");
        assert(compareFiles(path, path + "/../decompressed.txt"));
    }

}
