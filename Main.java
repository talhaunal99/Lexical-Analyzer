import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        analyzer.inputFile = new File(args[0]);
        analyzer.outputFile = new File(args[1]);
        analyzer.lexemeList = new ArrayList<>();
        analyzer.readFile();
        analyzer.traverseList();
    }
}
