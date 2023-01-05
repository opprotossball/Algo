import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrimeFinder {
    private boolean[] numbers;

    public List<Integer> findPrimes(int max) {
        numbers = new boolean[max + 1];
        Arrays.fill(numbers, 2, numbers.length, true);
        for (int i = 2; i < max + 1; i++) {
            if (!numbers[i]) {
                continue;
            }
            for (int j = i + 1; j < max + 1; j++) {
                if (j % i == 0) {
                    numbers[j] = false;
                }
            }
        }
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i < max + 1; i++) {
            if (numbers[i]) {
                primes.add(i);
            }
        }
        return primes;
    }

}
