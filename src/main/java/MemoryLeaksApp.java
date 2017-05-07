import org.eclipse.jetty.server.Server;
import sun.misc.Unsafe;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.Scanner;

public class MemoryLeaksApp {

    private static final String FILE_NAME = "longFile.txt";

    public static void main(String[] args) throws Exception{
        MemoryLeaksApp memoryLeaksApp = new MemoryLeaksApp();
        //push string in memory pool
        memoryLeaksApp.getLongFile().intern();

        memoryLeaksApp.getOOMwithUnsafe();

        startJetty();
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
            return FILE_NAME;
        }
    }

    class BadKey {
        // no hashCode or equals
        final String key;

        BadKey(String key) {
            this.key = key;
        }
    }

    private void getOOMwithUnsafe() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class unsafeClass = Class.forName("sun.misc.Unsafe");
        Field f = unsafeClass.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);
        try {
            unsafe.allocateMemory(1024 * 1024);
        } catch(Throwable e) {
            e.printStackTrace();
        }
    }

    private static void startJetty(){
        Server server = new Server(2017);

        try
        {
            server.start();
            server.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            server.destroy();
        }
    }
}
