/**
 * @Author: Willendless
 * @Date: 2020-06-18
 * @Description: view class
 * @LastEditTime: 2020-06-18
 * @FilePath: \code\View.java
 */
package code;

import javax.swing.SwingUtilities;

public class View {

    MainFrame f;
    Controller controller;

    public View(Controller controller) {
        this.controller = controller;
        f = new MainFrame(controller);
    }

    public void show() {
        SwingUtilities.invokeLater(() -> {
            f.setVisible(true);
        });
    }

    public void update(MatchStat stat) {
        SwingUtilities.invokeLater(() -> {
            f.lineSearchPanel.resultTextArea.setText(stat.toString());
        });
    }
}