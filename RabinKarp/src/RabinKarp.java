import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RabinKarp {
    private HashMap<Character, Integer> codes;
    private int charCount = 0;
    private long[] powers;
    private static final int MOD = 2137;

    public List<Integer> findOccurrence(String text, String pattern) {
        List<Integer> result = new ArrayList<>();

        if (pattern.length() > text.length()) {
            return result;
        }

        char[] textArray = text.toCharArray();
        char[] patternArray = pattern.toCharArray();

        codes = new HashMap<>();
        initCodes(textArray);
        initCodes(patternArray);
        initPowers(charCount);

        int i = 0;
        int j = pattern.length();

        long patternHash = getHash(patternArray);
        long hash = getHash(Arrays.copyOfRange(textArray, 0, j));

        if (patternHash == hash) {
            result.add(0);
        }

        while (j < text.length()) {
            hash -= codes.get(textArray[i]) * powers[pattern.length() - 1];
            hash *= charCount;
            hash = hash % MOD;
            hash += (codes.get(textArray[j])) % MOD;
            if (hash == patternHash) {
                result.add(i);
            }
            i++;
            j++;
        }
        return result;
    }

    private void initCodes(char[] chars) {
        for (char c : chars) {
            if (codes.get(c) == null) {
                codes.put(c, ++charCount);
            }
        }
    }

    private void initPowers(int n) {
        powers = new long[n];
        powers[0] = 1;
        for (int i = 1; i < n; i++) {
            powers[i] = (powers[i - 1] * charCount) % MOD;
        }
    }

    private long getHash(char[] chars) {
        int hash = 0;
        int n = chars.length;
        for (char c : chars) {
            hash += (codes.get(c) * Math.pow(charCount, --n)) % MOD;
        }
        return hash;
    }

}
