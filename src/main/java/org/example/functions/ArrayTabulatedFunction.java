package org.example.functions;

import java.io.Serializable;
import java.util.Objects;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable, Cloneable {
    private FunctionPoint[] mass;
    private int pointsCount;

    public void print() {
        for(int i = 0; i < pointsCount; i++) {
            System.out.println("(" + mass[i].getX() + ";" + mass[i].getY() + ")");
        }
        System.out.println();
    }

    public ArrayTabulatedFunction(FunctionPoint[] massPoints) throws IllegalArgumentException {
        if (massPoints.length < 2 || checkAbscissa(massPoints)) {
            throw new IllegalArgumentException();
        } else {
            mass = new FunctionPoint[massPoints.length];
            for (int i = 0; i < massPoints.length; ++i) {
                mass[i] = massPoints[i];
                ++pointsCount;
            }
        }
    }

    public static boolean checkAbscissa(FunctionPoint[] massPoints) {
        for(int i = 0; i < massPoints.length - 1; ++i) {
            if(massPoints[i].getX() >= massPoints[i + 1].getX()){
                return true;
            }
        }

        return false;
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        this.pointsCount = pointsCount;
        if (leftX >= rightX || pointsCount < 2) {
            throw new IllegalArgumentException();
        } else {
            mass = new FunctionPoint[pointsCount];
            double interval = (rightX - leftX) / (pointsCount - 1);
            for (int i = 0; i < pointsCount; ++i) {
                mass[i] = new FunctionPoint(leftX + interval * i, 0.0);
            }
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException {
        if (leftX >= rightX || values.length < 2) {
            throw new IllegalArgumentException();
        } else {
            mass = new FunctionPoint[values.length];
            double interval = (rightX - leftX) / (values.length - 1);
            for (int i = 0; i < values.length; ++i) {
                mass[i] = new FunctionPoint(leftX + interval * i, values[i]);
                pointsCount += 1;
            }
        }
    }

    public double getLeftDomainBorder() {
        return getPointX(0);
    }

    public double getRightDomainBorder() {
        return getPointX(getPointsCount() - 1);
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder())
            return Double.NaN;

        for (int i = 0; i < getPointsCount(); ++i) {
            if (mass[i].getX() == x) {
                return mass[i].getY();
            }
        }
        if (getLeftDomainBorder() <= x && getRightDomainBorder() >= x) {
            double leftY = getPointY(0);
            double rightY = getPointY(getPointsCount() - 1);
            double k = (rightY - leftY) / (getRightDomainBorder() - getLeftDomainBorder()) ;
            double b = rightY - k * getRightDomainBorder();
            return k * x + b;
        }

        return Double.NaN;
    }

    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (correctIndex(index)) {
            return mass[index];
        } else {
            throw new FunctionPointIndexOutOfBoundsException();
        }
    }

    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (!correctIndex(index)) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        if (getLeftDomainBorder() > point.getX() || getRightDomainBorder() < point.getX()) {
            throw new InappropriateFunctionPointException();
        }

        mass[index] = point;
    }

    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (correctIndex(index)) {
            return getPoint(index).getX();
        } else {
            throw new FunctionPointIndexOutOfBoundsException();
        }
    }

    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (!correctIndex(index)) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        if(getLeftDomainBorder() > x || getRightDomainBorder() < x) {
            throw new InappropriateFunctionPointException();
        }

        getPoint(index).x = x;
    }

    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        if (correctIndex(index)) {
            return getPoint(index).getY();
        } else {
            throw new FunctionPointIndexOutOfBoundsException();
        }

    }

    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (correctIndex(index)) {
            getPoint(index).y = y;
        } else {
            throw new FunctionPointIndexOutOfBoundsException();
        }
    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (!correctIndex(index)) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        if (getPointsCount() < 3) {
            throw new InappropriateFunctionPointException();
        }

        pointsCount = getPointsCount() - 1;
        System.arraycopy(mass, index + 1, mass, index, getPointsCount() - index);
    }

    public void  addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        for (int i = 0; i < getPointsCount(); ++i) {
            if(getPointX(i) == point.getX()) {
                throw new InappropriateFunctionPointException();
            }
        }

        for (int i = 0; i < getPointsCount(); ++i) {
            if (point.getX() >= getPointX(i) && point.getX() <= getPointX(i + 1)) {
                if (getPointsCount() == mass.length) {
                    FunctionPoint[] old = new FunctionPoint[mass.length];
                    System.arraycopy(mass, 0, old, 0, mass.length);

                    mass = new FunctionPoint[mass.length + 1];
                    System.arraycopy(old, 0, mass, 0, old.length);
                }
                System.arraycopy(mass, i + 1, mass, i + 2, getPointsCount() - i - 1);
                setPoint(i + 1, point);
                pointsCount = getPointsCount() + 1;

                return;
            }
        }

        if (getPointsCount() == 0) {
            mass[0] = point;
            pointsCount = 1;
        }
    }

    public boolean correctIndex(int index) {
        return index >= 0 && index < getPointsCount();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{").append(mass[0].toString());
        for (int i = 1; i < this.pointsCount; ++i) {
            builder.append(", ").append(mass[i].toString());
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof TabulatedFunction)) return false;

        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction ao = (ArrayTabulatedFunction) o;
            if (ao.pointsCount != this.pointsCount) return false;
            for (int i = 0; i < this.pointsCount; ++i) {
                if (!ao.mass[i].equals(this.mass[i])) return false;
            }
            return true;
        } else {
            if (((TabulatedFunction) o).getPointsCount() != this.getPointsCount()) return false;
            for (int i = 0; i < this.pointsCount; ++i) {
                if (!((TabulatedFunction) o).getPoint(i).equals(this.mass[i])) return false;
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(pointsCount);
        for (int i = 0; i < pointsCount; ++i) {
            result += Objects.hashCode(mass[i]);
        }
        return result;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}