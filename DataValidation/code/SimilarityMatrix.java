/**
 * @Author: Willendless
 * @Date: 2020-06-19
 * @Description: Do not edit
 * @LastEditTime: 2020-06-20
 * @FilePath: \code\SimilarityMatrix.java
 */
public class SimilarityMatrix {

    int[][] similarityMatrix;
    int xBegIndex;
    int xEndIndex;
    int yBegIndex;
    int yEndIndex;

    SimilarityMatrix(int[][] m, int begIndex) {
        setSimilarityMatrix(m);
        setIndex(begIndex);
    }

    private void setSimilarityMatrix(int[][] matrix) {
        similarityMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            similarityMatrix[i] = matrix[i];
        }
    }

    private void setIndex(int begIndex) {
        xBegIndex = begIndex;
        xEndIndex = xBegIndex + similarityMatrix.length;
        yBegIndex = xEndIndex;
        yEndIndex = yBegIndex + similarityMatrix.length;
    }

}
