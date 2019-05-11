public class App {

    public static void main(String [] args) {
        Parser parser = new Parser();
        long startTime = System.nanoTime();
        parser.parseForReduction("in.txt", "out.txt");

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in milliseconds: " + timeElapsed / 1000000);


    }
}
