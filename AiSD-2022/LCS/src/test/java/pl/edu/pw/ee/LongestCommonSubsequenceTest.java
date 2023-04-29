package pl.edu.pw.ee;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.BufferedReader;

public class LongestCommonSubsequenceTest {
    private LongestCommonSubsequence finder;
    private final PrintStream standardOut = System.out;

    @AfterEach
    public void cleanUp() {
        System.setOut(standardOut);
    }

    @Test
    public void returnsEmptyStringWhenTopStringIsEmpty() {
        //given
        String str1 = "";
        String str2 = "why are you reading me?";

        //when
        finder = new LongestCommonSubsequence(str1, str2);
        String result = finder.findLCS();

        //then
        assertEquals("", result);
    }

    @Test
    public void returnsEmptyStringWhenLeftStringIsEmpty() {
        //given
        String str1 = "Why are you reading me?";
        String str2 = "";

        //when
        finder = new LongestCommonSubsequence(str1, str2);
        String result = finder.findLCS();

        //then
        assertEquals("", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenOneOfGivenStringsIsNull() {
        //given
        String str1 = "";
        String str2 = null;

        //when
        finder = new LongestCommonSubsequence(str1, str2);

        //then
        finder.display();
        assert (true);
    }

    @Test
    public void findsLCSProperly() {
        //given
        String topStr = "często_z_odkrywaniem";
        String leftStr = "rzeczy_nie_trzeba\n_się_spieszyć";
        String expectedLCS = "cz__raie";

        //when
        finder = new LongestCommonSubsequence(topStr, leftStr);
        String result = finder.findLCS();

        //then
        assertEquals(expectedLCS, result);
    }

    @Test
    public void displayTest1() {
        //given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        String topStr = "ćw\nik";
        String leftStr = "wł\nok";
        String expectedDisplay;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/displayTestExpected1"));
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            expectedDisplay = stringBuilder.toString().trim().replaceAll("\\r\\n", "\n");;
        } catch (IOException e) {
            throw new RuntimeException("File with expected display result cannot be opened");
        }

        //when
        finder = new LongestCommonSubsequence(topStr, leftStr);
        finder.findLCS();
        finder.display();
        String result = outputStream.toString().trim().replaceAll("\\r\\n", "\n");

        //then
        assertEquals(expectedDisplay, result);
    }

    @Test
    public void displayTest2() {
        //given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        String topStr = "często_z_odkrywaniem";
        String leftStr = "rzeczy_nie_trzeba\n_się_spieszyć";
        String expectedDisplay;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/displayTestExpected2"));
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            expectedDisplay = stringBuilder.toString().trim().replaceAll("\\r\\n", "\n");
        } catch (IOException e) {
            throw new RuntimeException("File with expected display result cannot be opened");
        }

        //when
        finder = new LongestCommonSubsequence(topStr, leftStr);
        finder.findLCS();
        finder.display();
        String result = outputStream.toString().trim().replaceAll("\\r\\n", "\n");

        //then
        assertEquals(expectedDisplay, result);
    }

    @Test
    public void findsLCSCorrectlyWhenStringHaveSpecialCharacters() {
        //given
        String topStr = "\t \' \" \r \\ \n \f \b";
        String leftStr = "\t \' \" \r \\ \n \f \b";
        String expectedLCS = "\t \' \" \r \\ \n \f \b";

        //when
        finder = new LongestCommonSubsequence(topStr, leftStr);
        String result = finder.findLCS();

        //then
        assertEquals(expectedLCS, result);
    }

    @Test
    public void isCaseSensitive() {
        //given
        String topStr = "MAGNIFICENT UPPER CASE";
        String leftStr = "agile lower case";
        String expectedLCS = "  ";

        //when
        finder = new LongestCommonSubsequence(topStr, leftStr);
        String result = finder.findLCS();

        //then
        assertEquals(expectedLCS, result);
    }

    @Test
    public void findLCSCorrectlyWhenGivenStringsAreEqual() {
        //given
        String topStr = "En mas vagyok";
        String leftStr = "En mas vagyok";
        String expectedLCS = "En mas vagyok";

        //when
        finder = new LongestCommonSubsequence(topStr, leftStr);
        String result = finder.findLCS();

        //then
        assertEquals(expectedLCS, result);
    }

    @Test
    public void findLCSCorrectlyWhenGivenStringContainOther() {
        //given
        String topStr = "---> En mas vagyok <---";
        String leftStr = "En mas vagyok";
        String expectedLCS = "En mas vagyok";

        //when
        finder = new LongestCommonSubsequence(topStr, leftStr);
        String result = finder.findLCS();

        //then
        assertEquals(expectedLCS, result);
    }

    @Test(expected = IllegalStateException.class)
    public void throwsExceptionWhenDisplayOrderedWithoutFind() {
        //given
        String topStr = "some meaningless content";
        String leftStr = "a really useful information";

        //when
        finder = new LongestCommonSubsequence(topStr, leftStr);
        finder.display();

        //then
        assert (false);
    }

}
