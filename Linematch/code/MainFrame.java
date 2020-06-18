/**
 * @Author: Willendless
 * @Date: 2020-06-17
 * @Description: Main frame class
 * @LastEditTime: 2020-06-18
 * @FilePath: \code\MainFrame.java
 */
package code;

import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.BorderLayout;
import java.awt.Font;

public class MainFrame extends JFrame {

    Controller controller;

    private static final int FRAMEX = 300; 
    private static final int FRAMEY = 300;
    private static final int FRAMEW = 650;
    private static final int FRAMEH = 600;

    JPanel topPanel = new JPanel();
    JPanel fileChoosePanel = new FileChoosePanel();
    LineSearchPanel lineSearchPanel = new LineSearchPanel();
    CardLayout cardLayout = new CardLayout();

    class FileChoosePanel extends JPanel {
        JPanel centerPanel = new JPanel();
        JLabel label = new JLabel("请选择要搜索的文本文件");
        JButton button = new JButton("搜索");
        FileDialog fileDialog = new FileDialog(MainFrame.this, "打开", FileDialog.LOAD);
        JLabel pathInput = new JLabel();

        FileChoosePanel() {
            setLayout(null);

            label.setBounds((20), 270, 200, 20);
            button.setBounds((220), 270, 100, 20);

            add(label);
            add(button);

            button.addActionListener((e) -> {
                fileDialog.setVisible(true);
                File f = new File(fileDialog.getDirectory() + fileDialog.getFile());
                if (f.exists()) {
                    cardLayout.next(topPanel);
                    controller.matcher.setFile(f);
                }
            });
        }
    }

    class LineSearchPanel extends JPanel {
        JPanel northPanel = new JPanel(),
               centerPanel = new JPanel();
        JLabel label1 = new JLabel("请输入搜索项"),
               label2 = new JLabel("搜索结果:");
        JButton button = new JButton("确定");
        JTextField inputTextField = new JTextField();
        JTextArea resultTextArea = new JTextArea();

        LineSearchPanel() {
            this.setLayout(null);
            this.add(northPanel);
            northPanel.setBounds(0, 0, FRAMEW, 60);
            northPanel.setLayout(null);
            northPanel.add(label1);
            northPanel.add(inputTextField);
            northPanel.add(button);
            label1.setBounds(30, 30, 120, 30);
            inputTextField.setBounds(160, 30, 350, 30);
            button.setBounds(530, 30, 60, 30);

            button.addActionListener((e) -> {
                controller.tokenizer.setText(inputTextField.getText());
                controller.process();
            });

            this.add(centerPanel);
            centerPanel.setBounds(0, 40, FRAMEW, FRAMEH - 100);
            centerPanel.setLayout(null);
            centerPanel.add(label2);
            centerPanel.add(resultTextArea);
            label2.setBounds(30, 60, 120, 20);
            resultTextArea.setBounds(30, 90, 560, 400);
            resultTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }

    }

    public MainFrame(Controller controller) {
        this();
        this.controller = controller;
    }

    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setTitle("全文搜索系统");
        setBounds(FRAMEX, FRAMEY, FRAMEW, FRAMEH);
        setResizable(false);
        
        add(topPanel);
        topPanel.setLayout(cardLayout);
        topPanel.add(fileChoosePanel);
        topPanel.add(lineSearchPanel);
    }

    public static void main(String[] args) {
        JFrame f = new MainFrame();
        f.setFont(new Font("TimesRoman", Font.ITALIC, 15));
        f.setVisible(true);
    }
    
}