/**
 * @Author: Willendless
 * @Date: 2020-06-19
 * @Description: MainFrame class
 * @LastEditTime: 2020-06-19
 * @FilePath: \code\MainFrame.java
 */

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.Color;

public class MainFrame extends JFrame {

    private static final int FRAMEW = 1200;
    private static final int FRAMEH = 800;

    JPanel northPanel = new JPanel(),
           panel1 = new JPanel(),
           panel2 = new JPanel(),
           southPanel = new JPanel();
    JLabel label = new JLabel("数据文件");
    JTextField textField = new JTextField("输入文件名");
    JButton button1 = new JButton("选择"),
            button2 = new JButton("检测");

    
    MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(300, 150, FRAMEW, FRAMEH);
        setResizable(false);
        setTitle("数据真实性检测工具");
        add(northPanel);
        northPanel.setBounds(0, 0, FRAMEW, 40);
        add(panel1);
        panel1.setBounds(0, 50, FRAMEW, 300);
        panel1.setBackground(Color.BLUE);
        add(panel2);
        panel2.setBounds(0, 360, FRAMEW, 300);
        panel2.setBackground(Color.YELLOW);
        add(southPanel);
        southPanel.setBounds(0, 670, FRAMEW, 40);

        // north panel
        northPanel.setLayout(null);
        northPanel.add(label);
        northPanel.add(textField);
        northPanel.add(button1);
        northPanel.add(button2);
        label.setBounds(350, 10, 60, 30);
        textField.setBounds(420, 10, 170, 30);
        button1.setBounds(610, 10, 60, 30);
        button2.setBounds(680, 10, 60, 30);

        // center panel

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
    
}
