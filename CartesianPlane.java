package javmos;

import java.awt.Graphics2D;

public class CartesianPlane {

    private final JavmosGUI gui;

    public CartesianPlane(JavmosGUI gui) {
        this.gui = gui;
    }

    public void drawPlane(Graphics2D graphics2D) {

        double zoom = gui.getZoom();
        int n = 0;

        //x axis
        graphics2D.drawLine(0, 399, 800, 399);
        graphics2D.drawLine(0, 401, 800, 401);
        //y axis
        graphics2D.drawLine(399, 0, 399, 800);
        graphics2D.drawLine(401, 0, 401, 800);

        for (int i = 0; i <= 400; i += zoom, n++) {
            for (int j = -1; j <= 1; j += 2) {
                int p = 400 + j * i;
                
                //horizontal lines
                graphics2D.drawLine(0, p, 800, p);
                //markings on x axis
                graphics2D.drawLine(p - 1, 395, p - 1, 405);
                graphics2D.drawLine(p + 1, 395, p + 1, 405);
                //vertical lines
                graphics2D.drawLine(p, 0, p, 800);
                //markings on y axis
                graphics2D.drawLine(395, p - 1, 405, p - 1);
                graphics2D.drawLine(395, p + 1, 405, p + 1);

                //draw numbers
                graphics2D.drawString(Integer.toString(0), 385, 420);

                if (n != 0) {
                    if (gui.getDomainStep() % 1 == 0) {
                        graphics2D.drawString(Integer.toString(n * j * (int) gui.getDomainStep()), p - 9, 420);
                    } else {
                        graphics2D.drawString(Double.toString(n * j * gui.getDomainStep()), p - 11, 420);
                    }
                }

                if (n != 0) {
                    if (gui.getRangeStep() % 1 == 0) {
                        graphics2D.drawString(Integer.toString(-n * j * (int) gui.getRangeStep()), 410, p + 7);
                    } else {
                        graphics2D.drawString(Double.toString(-n * j * gui.getRangeStep()), 410, p + 7);
                    }
                }
            }
        }
    }
}
