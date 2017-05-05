import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.Scanner;

public class MemoryLeaksApp {

    private static final String FILE_NAME = "longFile.txt";

    public static void main(String[] args) {
        MemoryLeaksApp memoryLeaksApp = new MemoryLeaksApp();
        //push string in memory pool
        memoryLeaksApp.getLongFile().intern();

        memoryLeaksApp.createLeakWithBadKey();
    }

    private void createLeakWithBadKey() {
        Map map = System.getProperties();
        map.put(new BadKey("key"), "value");
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

    class BadKey {
        // no hashCode or equals
        final String key;

        public BadKey(String key) {
            this.key = key;
        }
    }
}
