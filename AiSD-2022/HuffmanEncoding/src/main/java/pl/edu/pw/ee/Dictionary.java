package pl.edu.pw.ee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Dictionary {
    private HashMap<Character, ArrayList<Boolean>> codes;

    public Dictionary(int size) {
        codes = new HashMap<>(size);
    }

    public List<Boolean> getCode(char c) {
        ArrayList<Boolean> code = codes.get(c);
        if (code == null) {
            throw new IllegalArgumentException("Dictionary does not contain char with code " + (int) (c));
        }
        return Collections.unmodifiableList(code);
    }

    public String getCodeAsString(char c) {
        ArrayList<Boolean> code = codes.get(c);
        if (code == null) {
            throw new IllegalArgumentException("Dictionary does not contain char with code " + (int) (c));
        }
        String r = "";
        for (Boolean b : code) {
            r += b ? "1" : "0";
        }
        return r;
    }

    public void addToCode(char c, boolean b) {
        ArrayList<Boolean> oldCode = codes.get(c);
        if (oldCode == null) {
            oldCode = new ArrayList<Boolean>();
        }
        oldCode.add(b);
        codes.put(c, oldCode);
    }

}
