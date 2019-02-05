package javmos;

import java.awt.Color;

public enum RootType {

    X_INTERCEPT(new Color(255, 125, 26), "x-intercept"),
    CRITICAL_POINT(new Color(255, 215, 0), "critical point"),
    INFLECTION_POINT(new Color(204, 0, 102), "inflection point");

    public final Color color;
    public final String name;

    RootType(Color color, String name) {
        this.color = color;
        this.name = name;
    }

    public String getPointName() {
        return name;
    }

    public Color getPointColor() {
        return color;
    }

    public RootType valueOf(String color, String name) {
        switch (name) {
            case "X_INTERCEPT":
                return  X_INTERCEPT;
            case "CRITICAL_POINT":
                return CRITICAL_POINT;
            case "INFLECTION_POINT":
                return INFLECTION_POINT;
            default:
                return null;
        }
    }
}