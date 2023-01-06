import java.util.List;

public class Main {

    public static void main(String[] args) {
        String text = "_abc__abc__abcabc__abc";
        String pattern = "abc";
        RabinKarp rb = new RabinKarp();
        List<Integer> occurrences = rb.findOccurrence(text, pattern);
        for (Integer i : occurrences) {
            System.out.println(i);
        }
    }

}
