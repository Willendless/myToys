/**
 * @Author: Willendless
 * @Date: 2020-06-19
 * @Description: Do not edit
 * @LastEditTime: 2020-06-20
 * @FilePath: \code\HeatMap.java
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class HeatMap extends JPanel {

    private static final int HEIGHT = 200;

    private static final int WIDTH = 200;

    private SimilarityMatrix m;

    private static Color[] colors = new Color[21];

    int offset;

    static {
        Color c1 = Color.BLACK;
        Color c2 = Color.ORANGE;

        int r1 = c1.getRed();
        int g1 = c1.getGreen();
        int b1 = c1.getBlue();
        int a1 = c1.getAlpha();

        int r2 = c2.getRed();
        int g2 = c2.getGreen();
        int b2 = c2.getBlue();
        int a2 = c2.getAlpha();

        for (int i = 0; i < 8; i++) {
            double norm = i / (double)20;
            int newR = (int)(r1 + (r2 - r1) * norm);
            int newG = (int)(g1 + (g2 - g1) * norm);
            int newB = (int)(b1 + (b2 - b1) * norm);
            int newA = (int)(a1 + (a2 - a1) * norm);
            colors[i] = new Color(newR, newG, newB, newA);
        }

    }

    HeatMap(SimilarityMatrix m) {
        super();
        this.m = m;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        System.out.println("> debug: paint " + offset / WIDTH);
        // draw heatmap
        int length = m.similarityMatrix.length;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                int degree = m.similarityMatrix[i][j];
                degree = degree > 20 ? 20 : degree;
                g2d.setColor(colors[degree]);
                int x = i << 1;
                int y = (length - 1 - j) << 1;
                g2d.fillRect(x, y, 2, 2);
            }
        }

        // x axis


        // y axis


    }

    

}
