package pl.edu.pw.ee;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HuffByteTest {

    @Test
    public void shouldSetBitesAndGetCorrectChar() {
        //given
        HuffByte huffByte = new HuffByte();

        //when
        huffByte.setBit(1);
        huffByte.setBit(7);
        char c = huffByte.toChar();

        //then
        assertEquals('A', c);
    }

    @Test
    public void shouldCreateFromCharAndAddToListCorrectly() {
        //given
        HuffByte huffByte = new HuffByte('a');
        List<Boolean> result = new ArrayList<Boolean>();
        List<Boolean> expected = Arrays.asList(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);

        //when
        huffByte.addToListAsBooleans(result);

        //then
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenIncorrectSetIndexGiven() {
        //given
        HuffByte huffByte = new HuffByte('a');

        //when
        huffByte.setBit(8);

        //then
        assert (false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenIncorrectGetIndexGiven() {
        //given
        HuffByte huffByte = new HuffByte('a');

        //when
        huffByte.getBit(8);

        //then
        assert (false);
    }

}
