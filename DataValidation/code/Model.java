/**
 * @Author: Willendless
 * @Date: 2020-06-20
 * @Description: Do not edit
 * @LastEditTime: 2020-06-20
 * @FilePath: \code\Model.java
 */

public class Model {

    SimilarityMatrix[] directMatrixs = new SimilarityMatrix[4];

    SimilarityMatrix[] indirectSimilarityMatrixs = new SimilarityMatrix[4];  

    public void setDirectMatrixs(SimilarityMatrix[] m) {
        directMatrixs = m;
    }

    public void setIndirectMatrixs(SimilarityMatrix[] m) {
        indirectSimilarityMatrixs = m;
    }

    
}
