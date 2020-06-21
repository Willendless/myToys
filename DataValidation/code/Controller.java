
/**
 * @Author: Willendless
 * @Date: 2020-06-19
 * @Description: controller class
 * @LastEditTime: 2020-06-21
 * @FilePath: \code\Controller.java
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller {

    static final int BLOCKS = 4;

    Model model = new Model();

    View view = new View(this, model);

    public void run() {
        view.show();
    }

    public void process(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            List<String> lineLists = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lineLists.add(line);
            }
            int blockSize = lineLists.size() / BLOCKS;
            model.setDirectMatrixs(directBinary(lineLists, blockSize));
            model.setIndirectMatrixs(indirectBinary(lineLists, blockSize));
            view.update();
            reader.close();
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[][] calcEqualityMatrix(List<String> list) {
        int len = list.size() >> 1;
        int[][] re = new int[len][len];
        for (int i = 0; i < len; i++) {
            for (int j = len; j < len * 2; j++) {
                re[i][j - len] = list.get(i).equals(list.get(j)) ?
                                3 : -1;
            }
        }
        return re;
    }

    private SimilarityMatrix[] directBinary(List<String> list, int blockSize) {
        SimilarityMatrix[] m = new SimilarityMatrix[4];
        for (int i = 0; i < BLOCKS; i++) {
            int from = i * blockSize;
            int[][] eq = calcEqualityMatrix(list.subList(from, from + blockSize));
            m[i] = new SimilarityMatrix(dp(eq), from);
        }
        return m;
    }

    private SimilarityMatrix[] indirectBinary(List<String> list, int blockSize) {
        SimilarityMatrix[] m = new SimilarityMatrix[4];
        for (int i = 0; i < BLOCKS; i++) {
            int from = i * blockSize;
            int half = blockSize >> 1;
            Collections.reverse(list.subList(from, from + half));
            int[][] eq = calcEqualityMatrix(list.subList(from, from + blockSize));
            m[i] = new SimilarityMatrix(dp(eq), from);
        }
        return m;
    }

    private int[][] dp(int[][] eq) {
        int[][] re = new int[eq.length + 1][eq.length + 1];
        // System.out.println("========cacl matrix===========");
        for (int i = 1; i < re.length; i++) {
            for (int j = 1; j < re.length; j++) {
                re[i][j] = max(re[i-1][j-1] + eq[i-1][j-1],
                               re[i-1][j] - 1,
                               re[i][j-1] - 1,
                               0);
                // System.out.print(re[i][j] + "");
            }
            // System.out.println();
        }
        // System.out.println("=========one block finished=========");
        return re;
    }

    private int max(int... args) {
        int re = args[0];
        for (int a : args) {
            re = re > a ? re : a;
        }
        return re;
    }

    public static void main(String[] args) {
        new Controller().run();
    }
    
}
