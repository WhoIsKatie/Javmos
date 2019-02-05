package javmos;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import javmos.exceptions.PolynomialException;

public class Polynomial {

    public final int ATTEMPTS = 15;
    private java.lang.String polynomial;
    private JavmosGUI gui;
    private RootType rootType;
    public double[] coefficients;
    public int[] degrees;
    public ArrayList<String> termArr;
    public String fD;
    public String sD;
    public String poly = null;
    HashSet<Point> pointSet = new HashSet<>();

    public Polynomial(JavmosGUI gui, String polynomial) throws PolynomialException {
        try {
            this.gui = gui;
            this.polynomial = polynomial;
            
            if (this.polynomial.contains("=")) {
                poly = this.polynomial.substring(this.polynomial.indexOf('=') + 1, this.polynomial.length());
            } else {
                poly = this.polynomial;
            }
            
            if (poly.contains("x") == false) {
                this.degrees = new int[1];
                degrees[0] = 0;
                this.coefficients = new double[1];
                coefficients[0] = Integer.parseInt(poly);
            } else {
                String replacePoly = null;
                if (poly.charAt(0) == '-') {
                    replacePoly = "-" + poly.substring(1).replaceAll("-", "+-");
                } else {
                    replacePoly = poly.replaceAll("-", "+-");
                }
                String[] arr = replacePoly.split("\\+");
                termArr = new ArrayList<>(Arrays.asList(arr));
                this.degrees = new int[termArr.size()];
                this.coefficients = new double[termArr.size()];
                for (int k = 0; k < termArr.size(); k++) {
                    String term = termArr.get(k);
                    int temp = term.indexOf('x');
                    if (temp != -1) {
                        if (temp != 0) {
                            if (term.indexOf('-') == 0 && temp == 1) {
                                coefficients[k] = -1;
                            } else {
                                coefficients[k] = Double.parseDouble(term.substring(0, temp));
                            }
                        } else {
                            coefficients[k] = 1;
                        }
                        if (term.contains("^")) {
                            degrees[k] = Integer.parseInt(term.substring(temp + 2));
                        } else {
                            degrees[k] = 1;
                        }
                    } else {
                        coefficients[k] = Integer.parseInt(term);
                        degrees[k] = 0;
                    }
                }
            }
        } catch (Exception exception) {
            throw new PolynomialException(polynomial + " is not a valid polynomial!");
        }
    }

    public Polynomial(JavmosGUI gui, double[] coefficients, int[] degrees) {
        this.gui = gui;
        this.coefficients = coefficients;
        this.degrees = degrees;
    }

    public void drawPolynomial(Graphics2D graphics2D) {
        double zoom = gui.getZoom();
        for (int i = 0; i <= 800; i++) {
            double y1 = 0;
            double y2 = 0;
            double x1 = (i - 400) / zoom * gui.getDomainStep();
            double x2 = ((i + 1 - 400) / zoom * gui.getDomainStep());
            graphics2D.setStroke(new BasicStroke(2.5f));
            graphics2D.setColor(new Color(34, 18, 144));
            y1 += getValueAt(x1);
            y2 += getValueAt(x2);
            if (x1 >= gui.getMinDomain() && x2 <= gui.getMaxDomain() && y1 >= gui.getMinRange() && y2 <= gui.getMaxRange()) {
                graphics2D.drawLine(i, (int) (-y1 * zoom / gui.getRangeStep() + 400), i + 1, (int) (-y2 * zoom / gui.getRangeStep() + 400));

            }
            graphics2D.setColor(new Color(34, 18, 144));
        }
    }

    private int getDegree() {
        int max = 0;
        for (int i = 1; i < degrees.length; i++) {
            if (degrees[i] > max) {
                max = degrees[i];
            }
        }
        return max;
    }

    private Polynomial getDerivative() {
        double[] coef = new double[degrees.length];
        int[] exp = new int[degrees.length];
        for (int i = 0; i < degrees.length; i++) {
            if (degrees[i] == 0) {
                coef[i] = 0;
                exp[i] = 0;
            } else {
                coef[i] = coefficients[i] * degrees[i];
                exp[i] = degrees[i] - 1;
            }
        }
        return new Polynomial(gui, coef, exp);
    }

    public String getEquation() {
        String equation = "";
        if (coefficients[0] != 0) {
            equation += coefficients[0];
            if (degrees[0] > 1) {
                equation += "x^" + degrees[0];
            } else if (degrees[0] == 1) {
                equation += "x";
            }
        }
        
        for (int i = 1; i < coefficients.length; i++) {
            if (coefficients[i] != 0) {
                if (degrees[i] > 1) {
                    if (coefficients[i] > 0) {
                        equation += "+" + coefficients[i] + "x^" + degrees[i];
                    } else {
                        equation += coefficients[i] + "x^" + degrees[i];
                    }
                } else if (degrees[i] == 1) {
                    if (coefficients[i] > 0) {
                        equation += "+" + coefficients[i] + "x";
                    } else {
                        equation += coefficients[i] + "x";
                    }
                } else {
                    if (coefficients[i] > 0) {
                        equation += "+" + coefficients[i];
                    } else {
                        equation += coefficients[i];
                    }
                }
            }
        }
        
        if (degrees.length == 1 && degrees[0] == 0) {
            equation += "0";
        }
        
        return equation;
    }

    public String getFirstDerivative() {
        return "f'(x)=" + this.getDerivative().getEquation();
    }

    public String getSecondDerivative() throws PolynomialException {
        return "f''(x)=" + this.getDerivative().getDerivative().getEquation();
    }

    public HashSet<Point> getRoots(RootType rootType, double minDomain, double maxDomain) {
        double y = 0;
        double step = (gui.getDomainStep() < 0) ? -gui.getDomainStep() : gui.getDomainStep();
        
        //loops through the domain to find the roots
        for (double k = minDomain; k <= maxDomain; k += 0.1 * step) {
            DecimalFormat df = new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.HALF_DOWN);
            Double x = newtonsMethod(rootType, k, ATTEMPTS);
            if (x != null) {
                x = Double.parseDouble(df.format(x));
                y = Double.parseDouble(new DecimalFormat("#.###").format(getValueAt(x)));
                if (rootType.equals(RootType.X_INTERCEPT)) {
                    y = 0;
                }
                pointSet.add(new Point(gui, rootType, x + 0, y + 0));
            }
        }
        
        return pointSet;
    }

    private double getValueAt(double x) {
        double y = 0;
        
        for (int i = 0; i < degrees.length; i++) {
            y += coefficients[i] * Math.pow(x, degrees[i]);
        }
        
        return y;
    }

    private Double newtonsMethod(RootType rootType, double guess, int attempts) {
        this.rootType = rootType;
        double f = 0;
        double d = 0;
        double x;
        
        if (degrees.length != 0 && degrees[0] != 0) {
            x = guess;
            if (rootType == RootType.X_INTERCEPT) {
                for (int i = 0; i < degrees.length; i++) {
                    f += coefficients[i] * Math.pow(x, degrees[i]);
                    d += coefficients[i] * degrees[i] * Math.pow(x, degrees[i] - 1);
                }
            } else if (rootType == RootType.CRITICAL_POINT) {
                for (int i = 0; i < degrees.length; i++) {
                    f += coefficients[i] * degrees[i] * Math.pow(x, degrees[i] - 1);
                    d += coefficients[i] * degrees[i] * (degrees[i] - 1) * Math.pow(x, degrees[i] - 2);
                }
            } else if (rootType == RootType.INFLECTION_POINT) {
                for (int i = 0; i < degrees.length; i++) {
                    f += coefficients[i] * degrees[i] * (degrees[i] - 1) * Math.pow(x, degrees[i] - 2);
                    d += coefficients[i] * degrees[i] * (degrees[i] - 1) * (degrees[i] - 2) * Math.pow(x, degrees[i] - 3);
                }
            }
            guess = x - (f / d);
        } else {
            return null;
        }
        
        if (Math.abs(x - guess) < 0.000001) {
            return guess;
        }
        
        if (attempts == 0) {
            return null;
        }
        
        return newtonsMethod(rootType, guess, attempts - 1);
    }
}