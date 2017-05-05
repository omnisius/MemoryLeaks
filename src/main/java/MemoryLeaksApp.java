import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class MemoryLeaksApp {

    private static final String FILE_NAME = "longFile.txt";

    public static void main(String[] args) {
        MemoryLeaksApp memoryLeaksApp = new MemoryLeaksApp();
        //push string in memory pool
        memoryLeaksApp.getLongFile().intern();
    }

    private String getLongFile() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            //not closed BR
            BufferedReader br = new BufferedReader(new FileReader(classLoader.getResource(FILE_NAME).getFile()));
            //not closed scanner
            Scanner scanner = new Scanner(br);
            String str = FILE_NAME;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                str = str + line;
            }
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
