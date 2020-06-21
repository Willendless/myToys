/**
 * @Author: Willendless
 * @Date: 2020-06-19
 * @Description: Do not edit
 * @LastEditTime: 2020-06-21
 * @FilePath: \code\View.java
 */
public class View {

    MainFrame mainFrame;

    Model model;

    public View(Controller controller, Model m) {
        this.mainFrame = new MainFrame(controller);
        this.model = m;
    }

    public void show() {
        if (mainFrame != null)
            mainFrame.paint();
    }

    public void update() {
        HeatMap[] directMaps = new HeatMap[Controller.BLOCKS];
        HeatMap[] indirectMaps = new HeatMap[Controller.BLOCKS];
        int directCnt = 0;
        int indirectCnt = 0;
        for (int i = 0; i < directMaps.length; i++) {
            directMaps[i] = new HeatMap(model.directMatrixs[i]);
            directCnt += model.directMatrixs[i].cnt;
            indirectMaps[i] = new HeatMap(model.indirectSimilarityMatrixs[i]);
            indirectCnt += model.indirectSimilarityMatrixs[i].cnt;
        }
        mainFrame.setDirectHeatMaps(directMaps);
        mainFrame.setIndirectHeatMaps(indirectMaps);
        mainFrame.paintDirectMap();
        mainFrame.paintIndirectMap();
        mainFrame.resultLabels[0].setText("直接复制 " + directCnt + " 处");
        mainFrame.resultLabels[1].setText("对称复制" + indirectCnt + " 处");
        mainFrame.thresholdLabel.setVisible(true);
    }
    
}
