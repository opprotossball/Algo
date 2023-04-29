package pl.edu.pw.ee;

import java.util.List;

public class HuffByte {
    private byte value;
    private static final int[] bits = {0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01};

    public HuffByte() {
        this.value = (byte) 0x00;
    }

    public HuffByte(char character) {
        this.value = (byte) character;
    }

    public void setBit(int index) {
        if (index > 7 || index < 0) {
            throw new IllegalArgumentException("Bit index must be between 0 and 8");
        }
        value = (byte) (value | bits[index]);
    }

    public boolean getBit(int index) {
        if (index > 7 || index < 0) {
            throw new IllegalArgumentException("Bit index must be between 0 and 8");
        }
        return ((value & bits[index]) != 0);
    }

    public char toChar() {
        return (char) (value & 0xFF);
    }

    public void addToListAsBooleans(List<Boolean> resultList) {
        for (int i = 7; i >= 0; i--) {
            byte valueCopy = value;
            Boolean b = ((valueCopy >> i) & 1) == 1;
            resultList.add(b);
        }
    }

}