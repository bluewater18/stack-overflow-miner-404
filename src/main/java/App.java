import models.Tags;
import parsing.Parser;

public class App {

    public static void main(String [] args) {
        Parser parser = new Parser();
        try {
            if(args[0].equals("reduce")){

                reduce(parser);
            } else if (args[0].equals("analyse")) {
                parser.setTagThreshold(Integer.parseInt(args[1]));
                analyse(parser);
            } else if (args[0].equals("reduce-analyse")) {
                parser.setTagThreshold(Integer.parseInt(args[1]));
                reduce(parser);
                analyse(parser);
            } else {
                throw new ArrayIndexOutOfBoundsException();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ERROR");
            System.out.println("-----");
            System.out.println("--Usage--");
            System.out.println();
            System.out.println("action [tagThreshold]");
            System.out.println();
            System.out.println("actions:");
            System.out.println("reduce-analyse");
            System.out.println("reduce");
            System.out.println("analyse");
            System.out.println();
            System.out.println("examples:");
            System.out.println("reduce-analyse 30");
            System.out.println("analyse 30");
            System.out.println("reduce");
        }
    }
    private static void reduce(Parser parser){
        System.out.println("reducing...");
        long startTime = System.nanoTime();
        parser.parseForReduction("in.txt", "filtered.txt", Tags.ARCHITECTURE);
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("Reduction time in milliseconds: " + timeElapsed / 1000000);
    }

    private static void analyse(Parser parser) {
        System.out.println("analysing...");
        long startTime = System.nanoTime();
        parser.parseForAnalysis("filtered.txt", "analysis.txt");
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("Analysis time in milliseconds: " + timeElapsed / 1000000);
    }

    private static boolean argCheck(String string) {
        return (string != null && string.length()>0 && !string.equals(""));
    }
}
