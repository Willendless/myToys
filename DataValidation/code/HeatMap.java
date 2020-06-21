/**
 * @Author: Willendless
 * @Date: 2020-06-19
 * @Description: Do not edit
 * @LastEditTime: 2020-06-21
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

    private static Color[] colors = new Color[251];

    int offset;

    static {
        Color c1 = Color.BLACK;
        Color c2 = Color.RED;
        Color c3 = Color.ORANGE;

        int r1 = c1.getRed();
        int g1 = c1.getGreen();
        int b1 = c1.getBlue();
        int a1 = c1.getAlpha();

        int r2 = (c2.getRed() + c3.getRed()) / 2;
        int g2 = (c2.getGreen() + c3.getGreen()) / 2;
        int b2 = (c2.getBlue() + c3.getBlue()) / 2;
        int a2 = (c2.getAlpha() + c3.getAlpha()) / 2;

        for (int i = 0; i < 251; i++) {
            double norm = i / (double)250;
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
                // degree = degree > 250 ? 250 : degree;
                // g2d.setColor(colors[degree]);
                g2d.setColor(getColor(degree));
                int x = (i * 2) + 20;
                int y = ((HEIGHT - 15) - (j + 1) * 2);
                g2d.fillRect(x, y, 2, 2);
            }
        }

        // x axis
        g2d.setColor(Color.BLACK);
        g2d.drawLine(22, HEIGHT - 15, WIDTH - 20, HEIGHT - 15);
        g2d.drawLine(WIDTH - 20, HEIGHT - 15, WIDTH - 25, HEIGHT - 20);
        int xStringX = 22;
        int xStringY = HEIGHT;
        int steps = length / 5;
        for (int i = 0; i < 6; i++) {
            int x = xStringX + i * steps * 2;
            g2d.drawString(m.xBegIndex + i * steps + "", x, xStringY);
        }
        g2d.drawString("X", WIDTH - 20, HEIGHT - 20);
        

        // y axis
        g2d.drawLine(22, HEIGHT - 15, 22, 15);
        g2d.drawLine(22, 15, 27, 20);
        int yStringX = 0;
        int yStringY = HEIGHT - 15;
        for (int i = 0; i < 6; i++) {
            int y = yStringY - i * steps * 2;
            g2d.drawString(m.yBegIndex + i * steps - 1 + "", yStringX, y);
        }
        g2d.drawString("Y", 15, 15);


    }

    private Color getColor(int degree) {
        if (degree == 0)
            return Color.BLACK;
        else if (degree <= 10)
            return Color.YELLOW;
        else if (degree <= 20)
            return Color.ORANGE;
        else
            return Color.RED;
    }

    

}
