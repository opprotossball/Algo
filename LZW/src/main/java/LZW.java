import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LZW {
    private static final int MAP_INITIAL_SIZE = 256;
    private static final int MAP_MAX_SIZE = 4096;
    private static final int ENCODING_BITS = 12;
    private static final Charset CHARSET = StandardCharsets.ISO_8859_1;
    private HashMap<String, Integer> compressDict;
    private HashMap<Integer, String> decompressDict;

    public void compress(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        try {
            List<Integer> codes = encodedAsList(path);
            writeCodesToCompressed(path + "/../compressed.trrk", codes);
        } catch (IOException e) {
            throw new IllegalArgumentException("File " + path + " cannot be opened!");
        }
    }

    public void decompress(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        try {
            List<Integer> codes = readCodesFromCompressedFile(path);
            writeToDecompressed(path + "/../decompressed.txt", codes);
        } catch (IOException e) {
            throw new IllegalArgumentException("File " + path + " cannot be opened!");
        }
    }

    private List<Integer> encodedAsList(String path) throws IOException {
        File input = new File(path);
        InputStreamReader streamReader = new InputStreamReader(new FileInputStream(input), CHARSET);
        BufferedReader reader = new BufferedReader(streamReader);

        initMap(true);
        List<Integer> result = new ArrayList<>();

        int c = reader.read();

        if (c == -1) {
            throw new IllegalArgumentException("File " + path + " is empty!");
        }

        String word = String.valueOf((char) c);
        Integer code;
        int i = MAP_INITIAL_SIZE - 1;

        while ((c = reader.read()) != -1) {
            code = compressDict.get(word + (char) c);
            if (code != null) {
                word += (char)c;
            } else {
                code = compressDict.get(word);
                result.add(code);
                if (++i < MAP_MAX_SIZE) {
                    compressDict.put(word + (char) c, i);
                }
                word = String.valueOf((char) c);
            }
        }

        code = compressDict.get(word);
        result.add(code);
        return result;
    }

    private void writeToDecompressed(String path, List<Integer> codes) throws IOException {
        if (codes.isEmpty()) {
            throw new RuntimeException("File " + path + " is not compressed correctly!");
        }
        initMap(false);
        int n = MAP_INITIAL_SIZE;
        int lastCode = codes.get(0);
        String first = decompressDict.get(lastCode);
        if (first == null) {
            throw new RuntimeException("File " + path + " is not compressed correctly!");
        }
        StringBuilder decoded = new StringBuilder(first);

        for (int i=1; i<codes.size(); i++) {
            int currentCode = codes.get(i);
            String word = decompressDict.get(currentCode);
            if (word == null) {
                String oldWord = decompressDict.get(lastCode);
                word =  oldWord + oldWord.charAt(0);
            }
            decoded.append(word);
            decompressDict.put(n++, decompressDict.get(lastCode) + word.charAt(0));
            lastCode = currentCode;
        }

        File output = new File(path);
        OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(output), CHARSET);
        BufferedWriter writer = new BufferedWriter(streamWriter);
        writer.write(decoded.toString());
        writer.close();
    }

    private List<Integer> readCodesFromCompressedFile(String path) throws IOException {
        File input = new File(path);
        InputStreamReader streamReader = new InputStreamReader(new FileInputStream(input));
        BufferedReader reader = new BufferedReader(streamReader);
        List<Integer> codes = new ArrayList<>();
        int inputBitIndex = 7;
        int outputBitIndex = ENCODING_BITS - 1;
        int code = 0;
        int c = reader.read();

        while(true) {
            int currentBit = ((c>>inputBitIndex) & 1);
            if (currentBit == 1) {
                code = (code | (1<<outputBitIndex));            }
            inputBitIndex--;
            outputBitIndex--;
            if (inputBitIndex == -1) {
                if ((c = reader.read()) == -1) {
                    break;
                }
                inputBitIndex = 7;
            }
            if (outputBitIndex == -1) {
                codes.add(code);
                outputBitIndex = ENCODING_BITS - 1;
                code = 0;
            }
        }
        if (outputBitIndex == -1) {
            codes.add(code);
        }
        reader.close();
        return codes;
    }

    private void writeCodesToCompressed(String path, List<Integer> codes) throws IOException {
        File output = new File(path);
        OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(output));
        BufferedWriter writer = new BufferedWriter(streamWriter);

        int currentByte = 0;
        int bitIndex = 7;
        String encoded = "";

        for (Integer code : codes) {
            for (int i = ENCODING_BITS - 1; i >= 0; i--) {
                if (bitIndex == -1) {
                    bitIndex = 7;
                    encoded += (char) currentByte;
                    currentByte = 0;
                }
                if (((code>>i) & 1) == 1) {
                   currentByte = currentByte | (1<<bitIndex);
                }
                bitIndex--;
            }
        }

        encoded += (char) currentByte;
        writer.write(encoded);
        writer.close();
    }

    private void initMap(boolean compress) {
        if (compress) {
            compressDict = new HashMap<String, Integer>();
            for (int i = 0; i < MAP_INITIAL_SIZE; i++) {
                compressDict.put(String.valueOf((char) i), i);
            }
        } else {
            decompressDict = new HashMap<Integer, String>();
            for (int i = 0; i < MAP_INITIAL_SIZE; i++) {
                decompressDict.put(i, String.valueOf((char) i));
            }
        }
    }

}
