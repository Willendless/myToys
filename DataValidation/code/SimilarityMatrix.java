/**
 * @Author: Willendless
 * @Date: 2020-06-19
 * @Description: Do not edit
 * @LastEditTime: 2020-06-21
 * @FilePath: \code\SimilarityMatrix.java
 */
public class SimilarityMatrix {

    int[][] similarityMatrix;
    int xBegIndex;
    int xEndIndex;
    int yBegIndex;
    int yEndIndex;
    int cnt;

    SimilarityMatrix(int[][] m, int begIndex) {
        setSimilarityMatrix(m);
        setIndex(begIndex);
    }

    private void setSimilarityMatrix(int[][] matrix) {
        cnt = 0;
        similarityMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            similarityMatrix[i] = matrix[i];
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] >= 5)
                    cnt++;
            }
        }
    }

    private void setIndex(int begIndex) {
        xBegIndex = begIndex;
        xEndIndex = xBegIndex + similarityMatrix.length;
        yBegIndex = xEndIndex;
        yEndIndex = yBegIndex + similarityMatrix.length;
    }

}
