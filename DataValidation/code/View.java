/**
 * @Author: Willendless
 * @Date: 2020-06-19
 * @Description: Do not edit
 * @LastEditTime: 2020-06-20
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
        for (int i = 0; i < directMaps.length; i++) {
            directMaps[i] = new HeatMap(model.directMatrixs[i]);
            indirectMaps[i] = new HeatMap(model.indirectSimilarityMatrixs[i]);
        }
        mainFrame.setDirectHeatMaps(directMaps);
        mainFrame.setIndirectHeatMaps(indirectMaps);
        mainFrame.paintDirectMap();
        mainFrame.paintIndirectMap();
    }
    
}
