/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azureapplet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 *
 * @author tanelvir
 */
class PadDraw extends JComponent {
//this is gonna be your image that you draw on

    Image image;
//this is what we'll be using to draw on
    Graphics2D graphics2D;
//these are gonna hold our mouse coordinates
    int currentX, currentY, oldX, oldY;
    int thickness;

    public PadDraw() {
        thickness = 8;
        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter() {
//if the mouse is pressed it sets the oldX & oldY
//coordinates as the mouses x & y coordinates
            @Override
            public void mousePressed(MouseEvent e) {
                oldX = e.getX();
                oldY = e.getY();
                graphics2D.fillOval(oldX, oldY, thickness, thickness);
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
//while the mouse is dragged it sets currentX & currentY as the mouses x and y
//then it draws a line at the coordinates
//it repaints it and sets oldX and oldY as currentX and currentY
            @Override
            public void mouseDragged(MouseEvent e) {
                currentX = e.getX();
                currentY = e.getY();

                //drawLinePlz(oldX, oldY, currentX, currentY, thickness);
                graphics2D.fillOval(oldX, oldY, thickness, thickness);


                repaint();

                oldX = currentX;
                oldY = currentY;
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {

        if (image == null) {
            image = createImage(getSize().width, getSize().height);
            graphics2D = (Graphics2D) image.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            clear();
        }

        g.drawImage(image, 0, 0, null);
    }

    public void clear() {

        Color color = graphics2D.getColor();
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(color);
        repaint();
    }

    public void changeColor(Color theColor) {
        graphics2D.setPaint(theColor);
        repaint();
    }

    public void drawLinePlz(int oldX, int oldY, int currentX, int currentY, int thickness) {

        int dX = currentX - oldX;
        int dY = currentY - oldY;
        // line length
        double lineLength = Math.sqrt(dX * dX + dY * dY);

        double scale = (double) (thickness) / (2 * lineLength);

        // The x,y increments from an endpoint needed to create a rectangle...
        double ddx = -scale * (double) dY;
        double ddy = scale * (double) dX;
        ddx += (ddx > 0) ? 0.5 : -0.5;
        ddy += (ddy > 0) ? 0.5 : -0.5;
        int dx = (int) ddx;
        int dy = (int) ddy;

        // Now we can compute the corner points...
        int xPoints[] = new int[4];
        int yPoints[] = new int[4];

        xPoints[0] = oldX + dx;
        yPoints[0] = oldY + dy;
        xPoints[1] = oldX - dx;
        yPoints[1] = oldY - dy;
        xPoints[2] = currentX - dx;
        yPoints[2] = currentY - dy;
        xPoints[3] = currentX + dx;
        yPoints[3] = currentY + dy;

        graphics2D.fillPolygon(xPoints, yPoints, 4);
    }

    public void changeBrushSize(int size) {
        thickness = size;
    }

    void setNewImage(BufferedImage tempImage) {
        image = tempImage;
        graphics2D = (Graphics2D) image.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        getGraphics().drawImage(image, 0, 0, null);

    }
}
