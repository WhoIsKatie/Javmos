
package javmos;

import java.awt.geom.Ellipse2D;
import java.util.HashSet;
import java.util.Set;

public class Point {

    public final int RADIUS = 5;
    public final JavmosGUI gui;
    public Ellipse2D.Double point;
    public final RootType rootType;
    public double x;
    public double y;
    Set<Point> pointSet = new HashSet<>();
    
    public Point(JavmosGUI gui, RootType type, double x, double y){
        this.gui = gui;
        this.rootType = type;
        this.x = x;
        this.y = y;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public RootType getRootType() {
        return rootType;
    }

    @Override
    public int hashCode() {
        int hash = 5 + rootType.ordinal();
        for (int j = 0; j < toString().length(); j++) {
            hash += toString().charAt(j) * x;
        }
        return hash;
    }

   @Override
    public boolean equals(Object object) {     
        return getClass().hashCode() == object.getClass().hashCode();

    }

    @Override
    public String toString() {
        String rType = rootType.getPointName();
        String p = rType + ": (" + x + ", " + y + ")";
        return p;
    }
    
    public Ellipse2D.Double getPoint() {
        double zoom = gui.getZoom();
        point = new Ellipse2D.Double(x * zoom / gui.getDomainStep() + 396,
                y * -zoom / gui.getRangeStep() + 396, 2 * RADIUS, 2 * RADIUS);
        return point;
    }
    
    public void drawPoint(java.awt.Graphics2D graphics2D) {
        graphics2D.setColor(rootType.getPointColor());
        graphics2D.fill(getPoint());
    }
}