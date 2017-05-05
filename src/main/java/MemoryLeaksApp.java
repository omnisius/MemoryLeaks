import java.io.BufferedReader;
import java.io.FileReader;

public class MemoryLeaksApp {

    public static void main(String[] args) {
        MemoryLeaksApp memoryLeaksApp = new MemoryLeaksApp();
        memoryLeaksApp.getLongFile();
    }

    private String getLongFile() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            BufferedReader br = new BufferedReader(new FileReader(classLoader.getResource("longText.txt").getFile()));
            String str = br.readLine();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
