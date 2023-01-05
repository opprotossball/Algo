public class Main {

    public static void main(String[] args) {
        PrimeFinder pf = new PrimeFinder();
        for (Integer i : pf.findPrimes(100)) {
            System.out.println(i);
        }
    }

}
