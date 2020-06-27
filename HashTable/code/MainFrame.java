/**
 * @Author: Willendless
 * @Date: 2020-06-26
 * @Description: Do not edit
 * @LastEditTime: 2020-06-27
 * @FilePath: \code\MainFrame.java
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {

    Controller controller;

    JTextField keyField = new JTextField();
    JTextField valueField = new JTextField();

    JButton insertButton = new JButton("插入");
    JButton deleteButton = new JButton("删除");


    MainFrame(Controller controller) {
        this.controller = controller;
    }

    private void initComponent() {
        setTitle("Hashing");
        setBounds(550, 200, 800, 800);
        JPanel panel = new JPanel();
        add(panel);

        panel.setLayout(null);

        // top panel
        JPanel topPanel = new JPanel();
        JLabel label1 = new JLabel("键"),
               label2 = new JLabel("值");
        panel.add(topPanel);
        topPanel.setLayout(null);
        topPanel.setBounds(0, 0, 800, 70);
        topPanel.add(label1);
        topPanel.add(label2);
        topPanel.add(keyField);
        topPanel.add(valueField);
        topPanel.add(insertButton);
        topPanel.add(deleteButton);
        label1.setBounds(120, 40, 20, 25);
        keyField.setBounds(150, 40, 150, 25);
        label2.setBounds(310, 40, 20, 25);
        valueField.setBounds(340, 40, 150, 25);
        insertButton.setBounds(500, 40, 70, 25);
        deleteButton.setBounds(580, 40, 70, 25);

        // mainPanel
        JPanel mainPanel = new JPanel();
        JLabel hashLabel1 = new JLabel("H1(x) = x mod 8");
        JLabel hashLabel2 = new JLabel("H2(x) = (x div 8) mod 8");
        JPanel hashPanel = new HashPanel();
        panel.add(mainPanel);
        mainPanel.setLayout(null);
        mainPanel.setBounds(0, 70, 800, 700);
        mainPanel.add(hashLabel1);
        mainPanel.add(hashLabel2);
        mainPanel.add(hashPanel);
        hashPanel.setBounds(290, 75, 200, 560);
        hashLabel1.setBounds(290, 50, 100, 25);
        hashLabel2.setBounds(410, 50, 180, 25);


        

        initEventActioner();
    }

    private void initEventActioner() {
        insertButton.addActionListener((e) -> {
            try {
                long key = Long.valueOf(keyField.getText());
                long value = Long.valueOf(valueField.getText());
                if (controller != null) {
                    controller.set(key, value);
                }
            } catch(NumberFormatException ex) {
                return;
            }
        });

        deleteButton.addActionListener((e) -> {
            try {
                long key = Long.valueOf(keyField.getText());
                if (controller != null) {
                    controller.delete(key);
                }
            } catch(NumberFormatException ex) {
                return;
            }
        });
    }

    class HashPanel extends JPanel {

        JPanel hash1 = new JPanel();
        JPanel hash2 = new JPanel();

        JLabel[][] key = new JLabel[2][8];

        {
            for (int i = 0; i < key[0].length; i++) {
                for (int j = 0; j < 2; j++) {
                    key[j][i] = new JLabel("", JLabel.CENTER);
                    key[j][i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }
            }
        }

        HashPanel() {
            super();
            setLayout(null);
            add(hash1);
            add(hash2);
            hash1.setLayout(new GridLayout(8, 1));
            hash1.setBounds(10, 0, 70, 560);
            for (int i = 0; i < 8; i++)
                hash1.add(key[0][i]);
            hash2.setLayout(new GridLayout(8, 1));
            hash2.setBounds(130, 0, 70, 560);
            for (int i = 0; i < 8; i++)
                hash2.add(key[1][i]);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();


            Long[][] keySet = controller.hash.getKeySet();
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 8; j++) {
                    if (keySet[i][j] != null)
                        key[i][j].setText(keySet[i][j].toString());
                    else
                        key[i][j].setText("");
                }
            }


        }
    }



    void init() {
        SwingUtilities.invokeLater(() -> {
            initComponent();
            setVisible(true);
        });
        
    }

}
