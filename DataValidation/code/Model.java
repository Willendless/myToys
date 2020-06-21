/**
 * @Author: Willendless
 * @Date: 2020-06-20
 * @Description: Do not edit
 * @LastEditTime: 2020-06-20
 * @FilePath: \code\Model.java
 */

public class Model {

    final SimilarityMatrix[] directMatrixs = new SimilarityMatrix[4];

    final SimilarityMatrix[] indirectSimilarityMatrixs = new SimilarityMatrix[4];

    public void setDirectMatrixs(SimilarityMatrix[] m) {
        for (int i = 0; i < directMatrixs.length; i++) {
            directMatrixs[i] = m[i];
        }
    }

    public void setIndirectMatrixs(SimilarityMatrix[] m) {
        for (int i = 0; i < indirectSimilarityMatrixs.length; i++) {
            indirectSimilarityMatrixs[i] = m[i];
        }
    }
    
}
