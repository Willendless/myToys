/**
 * @Author: Willendless
 * @Date: 2020-06-19
 * @Description: MainFrame class
 * @LastEditTime: 2020-06-20
 * @FilePath: \code\MainFrame.java
 */

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;
import java.awt.GridLayout;

import java.io.File;

public class MainFrame extends JFrame {

    private static final int FRAMEW = 1200;
    private static final int FRAMEH = 800;

    Controller controller;

    JPanel northPanel = new JPanel(),
           southPanel = new JPanel();
    JPanel[] tablePanels = new JPanel[] {
        new JPanel(),
        new JPanel()
    };

    JTextField textField = new JTextField("输入文件名");
    JButton button1 = new JButton("选择"),
            button2 = new JButton("检测");
    JLabel[] resultLabels = new JLabel[] {
        new JLabel("直接复制"),
        new JLabel("逆序复制")
    };

    JLabel thresholdLabel = new JLabel("阈值：5");

    HeatMap[] directHeatMaps = new HeatMap[Controller.BLOCKS],
              indirectHeatmaps = new HeatMap[Controller.BLOCKS];

    public void setDirectHeatMaps(HeatMap[] m) {
        for (int i = 0; i < Controller.BLOCKS; i++) {
            directHeatMaps[i] = m[i];
        }
    }

    public void setIndirectHeatMaps(HeatMap[] m) {
        for (int i = 0; i < Controller.BLOCKS; i++) {
            indirectHeatmaps[i] = m[i];
        }
    }

    public MainFrame(Controller controller) {
        this.controller = controller;
        // for (int i = 0; i < Controller.BLOCKS; i++) {
        //     directHeatMaps[i] = new HeatMap();
        //     indirectHeatmaps[i] = new HeatMap();
        // }
    }

    private void paintFrame() {
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(300, 150, FRAMEW, FRAMEH);
        setResizable(false);
        setTitle("数据真实性检测工具");
        add(northPanel);
        add(tablePanels[0]);
        add(tablePanels[1]);
        add(southPanel);
        setVisible(true);

        // north panel
        JLabel fileLabel = new JLabel("数据文件");
        northPanel.setBounds(0, 0, FRAMEW, 40);
        northPanel.setLayout(null);
        northPanel.add(fileLabel);
        northPanel.add(textField);
        northPanel.add(button1);
        button1.addActionListener((e) -> {
            JFileChooser c = new JFileChooser("W:\\myToys\\DataValidation");
            c.setFileSelectionMode(JFileChooser.FILES_ONLY);
            c.setAcceptAllFileFilterUsed(false);
            c.setFileFilter(new FileNameExtensionFilter(".csv", "csv"));
            c.showOpenDialog(this);
            textField.setText(c.getSelectedFile().toString());
        });
        northPanel.add(button2);
        button2.addActionListener((e) -> {
            System.out.println("> debug: 点击检测");
            File f = new File(textField.getText());
            if (f.exists())
                controller.process(f);
            else
                System.out.println("file does not exists");
        
        });
        fileLabel.setBounds(350, 10, 60, 30);
        textField.setBounds(420, 10, 170, 30);
        button1.setBounds(610, 10, 60, 30);
        button2.setBounds(680, 10, 60, 30);

        // tablePanels

        // south panel
        southPanel.setBounds(0, 670, FRAMEW, 40);
        southPanel.setLayout(null);
        southPanel.add(thresholdLabel);
        southPanel.setBackground(Color.CYAN);
        thresholdLabel.setBounds(20, 10, 100, 30);

    }

    public void paintDirectMap() {
        paintTablePanel(0, "直接二分", 0, 50, directHeatMaps);
    }

    public void paintIndirectMap() {
        paintTablePanel(1, "对称二分", 0, 360, indirectHeatmaps);
    }

    private void paintTablePanel(int i, String labelName, int x, int y, HeatMap[] maps) {
        JLabel label = new JLabel(labelName);
        JPanel heatMapPanel = new JPanel();
        tablePanels[i].setLayout(null);
        tablePanels[i].setBounds(x, y, FRAMEW, 300);
        tablePanels[i].setBackground(Color.YELLOW);
        tablePanels[i].add(label);
        label.setBounds(20, 0, 70, 20);
        tablePanels[i].add(heatMapPanel);
        heatMapPanel.setLayout(null);
        heatMapPanel.setBounds(20, 30, FRAMEW - 40, 230);
        // heatMapPanel.setBackground(Color.BLACK);
        for (int j = 0; j < maps.length; j++) {
            System.out.println("> debug: heat map length " + maps.length);
            heatMapPanel.add(maps[j]);
            maps[j].setBounds(0 + 200 * j, 0, 200, 200);
            maps[j].setBorder(BorderFactory.createLineBorder(Color.GREEN));
        }
        tablePanels[i].add(resultLabels[i]);
        resultLabels[i].setBounds(20, 260, 100, 20);
    }

    public void paint() {
        SwingUtilities.invokeLater(() -> {
            this.paintFrame();
        });
    }

    public static void main(String[] args) {
        new MainFrame(null).paint();
    }
    
}
