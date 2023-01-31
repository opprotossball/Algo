import org.junit.Test;

public class LZWTest extends myTest {

    @Test
    public void shouldCompressAndDecompressCorrectlyEnglishLetters() {
        //given
        String path = "src/test/resources/englishLetters/";
        LZW lzw = new LZW();

        //when
        lzw.compress(path + "englishLetters.txt");
        lzw.decompress(path + "compressed.trrk");

        //then
        assert(compareFiles(path + "englishLetters.txt", path + "decompressed.txt"));
    }

    @Test
    public void shouldCompressAndDecompressCorrectlyTwoLetters() {
        //given
        String path = "src/test/resources/twoLetters/";
        LZW lzw = new LZW();

        //when
        lzw.compress(path + "twoLetters.txt");
        lzw.decompress(path + "compressed.trrk");

        //then
        assert(compareFiles(path + "twoLetters.txt", path + "decompressed.txt"));
    }

    @Test
    public void shouldCompressAndDecompressCorrectlyPolishCharacters() {
        //given
        String path = "src/test/resources/polish/";
        LZW lzw = new LZW();

        //when
        lzw.compress(path + "polish.txt");
        lzw.decompress(path + "compressed.trrk");

        //then
        assert(compareFiles(path + "polish.txt", path + "decompressed.txt"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenCompressEmptyFile() {
        //given
        String path = "src/test/resources/emptyFiles/";
        LZW lzw = new LZW();

        //when
        lzw.compress(path + "empty.txt");
        lzw.decompress(path + "compressed.trrk");

        //then
        assert(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenCompressNonExistingFile() {
        //given
        String path = "src/test/resources/not/existing/path/";
        LZW lzw = new LZW();

        //when
        lzw.compress(path + "xd.txt");

        //then
        assert(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenDecompressNonExistingFile() {
        //given
        String path = "src/test/resources/not/existing/path/";
        LZW lzw = new LZW();

        //when
        lzw.decompress(path + "compressed.trrk");

        //then
        assert(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenCompressPathNull() {
        //given
        String path = null;
        LZW lzw = new LZW();

        //when
        lzw.compress(path);

        //then
        assert(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenDecompressPathNull() {
        //given
        String path = null;
        LZW lzw = new LZW();

        //when
        lzw.decompress(path);

        //then
        assert(false);
    }

}