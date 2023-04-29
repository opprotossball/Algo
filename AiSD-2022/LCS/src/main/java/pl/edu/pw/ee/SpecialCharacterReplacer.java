package pl.edu.pw.ee;

public class SpecialCharacterReplacer {
    private static final char[] specials = new char[]{'\t', '\'', '\"', '\r', '\\', '\n', '\f', '\b'};
    private static final String[] replacements = new String[]{"\\t", "\\'", "\\\"", "\\r", "\\\\", "\\n", "\\f", "\\b"};

    public static String getReplacement(char toReplace) {
        int i = 0;
        for (char c : specials) {
            if (toReplace == c) {
                return replacements[i];
            }
            i++;
        }
        return String.valueOf(toReplace);
    }

}
