import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * @Author: Willendless
 * @Date: 2020-06-25
 * @Description: Do not edit
 * @LastEditTime: 2020-06-26
 * @FilePath: \code\Main.java
 */

public class Main {

    public static void main(String[] args) {
        process("small", new LinearHash(), "smallLinear");
        process("large", new LinearHash(), "largeLinear");

        process("small", new CuckooHash(), "smallCuckoo");
        process("large", new CuckooHash(), "largeCuckoo");
    }

    static void process(String name, Hash hash, String outputName) {
        try {
            String out = System.getProperty("user.dir") + "\\data\\" + outputName + ".out";
            File in1 = new File(System.getProperty("user.dir") + "\\data\\" + name + ".in");
            BufferedWriter writer = new BufferedWriter(new FileWriter(out));
            Scanner scanner = new Scanner(in1);
            while (scanner.hasNextLine()) {
                String l = scanner.nextLine();
                Scanner s = new Scanner(l);
                String command = s.next();
                if (command.equals("Set")) {
                    long key = s.nextLong();
                    long value = s.nextLong();
                    hash.set(key, value);
                } else if (command.equals("Get")) {
                    long key = s.nextLong();
                    writer.write(hash.get(key) + "");
                    writer.newLine();
                } else if (command.equals("Del")) {
                    long key = s.nextLong();
                    hash.delete(key);
                }
            }
            scanner.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
}
