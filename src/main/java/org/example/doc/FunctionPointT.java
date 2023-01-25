package org.example.doc;

public class FunctionPointT {
    private final int columnCount = 2;
    private Double X;
    private Double Y;

    public FunctionPointT(Double x, Double y) {
        X = x;
        Y = y;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public Double getX() {
        return X;
    }

    public Double getY() {
        return Y;
    }

    public void setX(Double x) {
        X = x;
    }

    public void setY(Double y) {
        Y = y;
    }
}
