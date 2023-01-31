import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class myTest {
    protected static final Charset CHARSET = StandardCharsets.ISO_8859_1;

    protected boolean compareFiles(String path1, String path2) {
        try {
            File f1 = new File(path1);
            File f2 = new File(path2);
            InputStreamReader reader1 = new InputStreamReader(new FileInputStream(f1), CHARSET);
            InputStreamReader reader2 = new InputStreamReader(new FileInputStream(f2), CHARSET);
            int c1, c2;
            int i = 0;
            boolean equals = true;
            do {
                c1 = reader1.read();
                c2 = reader2.read();
                i++;
                if (c1 != c2) {
                    equals = false;
                    System.out.println("Files differ at position: " + i);
                    break;
                }
            } while (c1 != -1);
            reader1.close();
            reader2.close();
            return equals;
        } catch (IOException e) {
            throw new RuntimeException("Cannot compare files");
        }
    }
}
