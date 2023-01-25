package org.example.functions;

import java.util.Objects;

public class LinkedListTabulatedFunction implements TabulatedFunction, Cloneable {

    private static class FunctionNode {
        private FunctionPoint point;
        private FunctionNode prev;
        private FunctionNode next;
    }

    private int pointsCount;
    private final FunctionNode head;

    public LinkedListTabulatedFunction(FunctionPoint[] massPoints) throws IllegalArgumentException, InappropriateFunctionPointException {
        if(massPoints.length < 2 || checkAbscissa(massPoints)){
            throw new IllegalArgumentException();
        } else {
            head = new FunctionNode();
            head.prev = head;
            head.next = head;

            for (int i = 0; i < massPoints.length; ++i)
                addPoint(new FunctionPoint(massPoints[i].getX(), massPoints[i].getY()));
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

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException, InappropriateFunctionPointException {
        if (leftX >= rightX || pointsCount < 2) {
            throw new IllegalArgumentException();
        } else {
            head = new FunctionNode();
            head.prev = head;
            head.next = head;

            double interval = (rightX - leftX) / (pointsCount - 1);
            for (int i = 0; i < pointsCount; ++i)
                addPoint(new FunctionPoint(leftX + interval * i, 0.0));
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException, InappropriateFunctionPointException {
        if (leftX >= rightX || values.length < 2) {
            throw new IllegalArgumentException();
        } else {
            head = new FunctionNode();
            head.prev = head;
            head.next = head;

            double interval = (rightX - leftX) / (values.length - 1);
            for (int i = 0; i < values.length; ++i)
                addPoint(new FunctionPoint(leftX + interval * i, values[i]));
        }
    }

    protected FunctionNode getNodeByIndex(int index) {
        FunctionNode current = head;
        while (index-- >= 0) current = current.next;
        return current;
    }

    protected FunctionNode addNodeToTail() {
        FunctionNode node = new FunctionNode();

        node.next = head;
        node.prev = head.prev;
        head.prev.next = node;
        head.prev = node;

        ++pointsCount;
        return node;
    }

    protected FunctionNode addNodeByIndex(int index) {
        FunctionNode node = new FunctionNode();
        FunctionNode current = getNodeByIndex(index);

        node.next = current;
        node.prev = current.prev;
        current.prev.next = node;
        current.prev = node;

        ++pointsCount;
        return node;
    }

    protected FunctionNode deleteNodeByIndex(int index) {
        FunctionNode node = getNodeByIndex(index);

        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.prev = null;
        node.next = null;

        --pointsCount;
        return node;
    }

    public void print() {
        FunctionNode node = head.next;

        for(int i = 0; i < pointsCount; i++) {
            System.out.println("(" + node.point.getX() + ";" + node.point.getY() + ")");
            node = node.next;
        }
        System.out.println();
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public double getLeftDomainBorder() {
        return head.next.point.getX();
    }

    public double getRightDomainBorder() {
        return head.prev.point.getX();
    }

    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (correctIndex(index)) {
            return getNodeByIndex(index).point;
        } else {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
    }

    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (!correctIndex(index))
            throw new FunctionPointIndexOutOfBoundsException(index);

        FunctionNode node = getNodeByIndex(index);
        if (!checkLies(node, point.getX()))
            throw new InappropriateFunctionPointException();

        node.point = point;
    }

    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (correctIndex(index)) {
            return getPoint(index).getX();
        } else {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
    }

    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (!correctIndex(index))
            throw new FunctionPointIndexOutOfBoundsException(index);

        FunctionNode node = getNodeByIndex(index);
        if (!checkLies(node, x))
            throw new InappropriateFunctionPointException();

        node.point.x = x;
    }

    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        if (correctIndex(index)) {
            return getPoint(index).getY();
        } else {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
    }

    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (correctIndex(index)) {
            getPoint(index).y = y;
        } else {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder())
            return Double.NaN;

        FunctionNode node = head;
        while ((node = node.next) != head) {
            if (node.point.getX() == x)
                return node.point.getY();

            if (x >= node.point.getX() && x <= node.next.point.getX()) {

                FunctionPoint left = node.point;
                FunctionPoint right = node.next.point;

                double k = (right.getY() - left.getY()) / (right.getX() - left.getX());
                double b = right.getY() - k * right.getX();
                return k * x + b;

            }
        }

        return Double.NaN;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode position = head;
        FunctionNode current = head;

        while ((current = current.prev) != head) {
            if (point.getX() == current.point.getX()) {
                throw new InappropriateFunctionPointException();
            }

            if (point.getX() > current.point.getX()) {
                position = current;
                break;
            }
        }

        FunctionNode node = new FunctionNode();
        node.prev = current;
        node.next = current.next;
        current.next.prev = node;
        current.next = node;

        ++pointsCount;
        node.point = point;
    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (!correctIndex(index))
            throw new FunctionPointIndexOutOfBoundsException(index);

        if (getPointsCount() <= 2)
            throw new IllegalStateException();

        deleteNodeByIndex(index);
    }

    public boolean correctIndex(int index) {
        return index >= 0 && index < getPointsCount();
    }

    public boolean checkLies(FunctionNode node, double x) {
        double leftX = 0;
        double rightX = 0;

        if(node.prev == head) {
            leftX = getLeftDomainBorder();
        } else {
            leftX = node.prev.point.getX();
        }

        if(node.next == head) {
            rightX = getRightDomainBorder();
        } else {
            rightX = node.next.point.getX();
        }

        return x > leftX && x < rightX;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        FunctionNode node = head.next;
        builder.append("{").append(node.point.toString());
        for (int i = 0; i < this.pointsCount - 1; ++i) {
            node = node.next;
            builder.append(", ").append(node.point.toString());
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof TabulatedFunction)) return false;

        if (o instanceof LinkedListTabulatedFunction) {
            LinkedListTabulatedFunction ao = (LinkedListTabulatedFunction) o;
            if (ao.pointsCount != this.pointsCount) return false;
            FunctionNode thisNode = this.head.next;
            FunctionNode objNode = ao.head.next;
            for (int i = 0; i < this.pointsCount; ++i) {
                if (!thisNode.point.equals(objNode.point)) return false;
                thisNode = thisNode.next;
                objNode = objNode.next;
            }
            return true;
        } else {
            if (((TabulatedFunction) o).getPointsCount() != this.getPointsCount()) return false;
            FunctionNode thisNode = this.head.next;
            for (int i = 0; i < this.pointsCount; ++i) {
                if (!((TabulatedFunction) o).getPoint(i).equals(thisNode.point)) return false;
                thisNode = thisNode.next;
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        FunctionNode node = head;
        int result = Objects.hashCode(pointsCount);
        for (int i = 0; i < pointsCount; ++i) {
            node = node.next;
            result += Objects.hashCode(node.point);
        }

        return result;
    }

    @Override
    public Object clone()  {
        int length = this.getPointsCount();
        FunctionPoint[] p = new FunctionPoint[length];
        for (int i = 0; i < length; ++i) {
            p[i] = new FunctionPoint(this.getPoint(i));
        }
        try {
            return new LinkedListTabulatedFunction(p);
        } catch (InappropriateFunctionPointException e) {
            e.printStackTrace();
        }
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
