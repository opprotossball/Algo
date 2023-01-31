import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class MethodsTest extends myTest {

    @Test
    public void readCodesFromCompressedFileTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        String path = "src/test/resources/MethodsTests/methodTest0/compressed.trrk";
        LZW lzw = new LZW();
        Method method = LZW.class.getDeclaredMethod("readCodesFromCompressedFile", String.class);
        method.setAccessible(true);
        List<Integer> expected = Arrays.asList(1558, 611, 1558, 611);

        //when
        List<Integer> result = (List<Integer>) method.invoke(lzw, path);


        //then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void writesAndReadsCodesCorrectly() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        String path = "src/test/resources/MethodsTests/methodTest1/compressed.trrk";
        LZW lzw = new LZW();
        Method writeCodesToCompressed = LZW.class.getDeclaredMethod("writeCodesToCompressed", String.class, List.class);
        Method readCodesFromCompressedFile = LZW.class.getDeclaredMethod("readCodesFromCompressedFile", String.class);
        writeCodesToCompressed.setAccessible(true);
        readCodesFromCompressedFile.setAccessible(true);
        List<Integer> codes = Arrays.asList(1558, 611);

        //when
        writeCodesToCompressed.invoke(lzw, path, codes);
        List<Integer> result = (List<Integer>) readCodesFromCompressedFile.invoke(lzw, path);

        //then
        Assert.assertEquals(codes, result);
    }

    @Test
    public void encodesFileCorrectly() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        String path = "src/test/resources/MethodsTests/methodTest2/origin.txt";
        LZW lzw = new LZW();
        Method encodedAsList = LZW.class.getDeclaredMethod("encodedAsList", String.class);
        encodedAsList.setAccessible(true);
        List<Integer> codes = Arrays.asList(97, 98, 32, 256, 258, 257, 97);

        //when
        List<Integer> result = (List<Integer>) encodedAsList.invoke(lzw, path);

        //then
        Assert.assertEquals(codes, result);
    }

    @Test
    public void writesToDecompressedCorrectly() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        String path = "src/test/resources/MethodsTests/methodTest3/decompressed.txt";
        LZW lzw = new LZW();
        Method writeToDecompressed = LZW.class.getDeclaredMethod("writeToDecompressed", String.class, List.class);
        writeToDecompressed.setAccessible(true);
        List<Integer> codes = Arrays.asList(97, 98, 32, 256, 258, 257, 97);

        //when
        writeToDecompressed.invoke(lzw, path, codes);

        //then
        assert(compareFiles(path + "/../expected.txt", path));
    }

}
