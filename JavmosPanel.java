package javmos;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JPanel;
import javmos.listeners.PointClickListener;

public class JavmosPanel extends JPanel {

    private final JavmosGUI gui;
    private CartesianPlane plane;
    private Polynomial polynomial;
    private Point point;
    private HashSet<Point> pointHash;
    private LinkedList<Point> points;
    private PointClickListener mListener ;
    private boolean polynomialChanged = false;

    public JavmosPanel(JavmosGUI gui) {
        this.gui = gui;
        this.plane = new CartesianPlane(gui);
        this.polynomial = null;
        this.point = null;
        this.points = new LinkedList<>();
    }

    public Polynomial getPolynomial() {
        return polynomial;
    }

    public void setPlane(CartesianPlane plane) {
        this.plane = plane;
    }

    public void setPolynomial(Polynomial polynomial) {
        this.polynomial = polynomial;
        polynomialChanged = true;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2d = (Graphics2D) graphics;
        points.clear();
        plane.drawPlane(g2d);
        if (polynomial != null) {
            polynomial.drawPolynomial(g2d);

            pointHash = polynomial.getRoots(RootType.X_INTERCEPT, gui.getMinDomain(), gui.getMaxDomain());
            pointHash = polynomial.getRoots(RootType.CRITICAL_POINT, gui.getMinDomain(), gui.getMaxDomain());
            pointHash = polynomial.getRoots(RootType.INFLECTION_POINT, gui.getMinDomain(), gui.getMaxDomain());

            Iterator<Point> hashItr = pointHash.iterator();
            while (hashItr.hasNext()) {
                points.add((Point) hashItr.next());
            }
            Iterator<Point> pointsItr = points.iterator();
            mListener = (PointClickListener) getListeners(MouseListener.class)[0];
            while (pointsItr.hasNext()) {
                point = pointsItr.next();
                if (point != null) {
                    point.drawPoint(g2d);
                    mListener.setPoints(points);
                }
            }
        }
    }
}