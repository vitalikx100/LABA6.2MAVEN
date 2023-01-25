package org.example.functions;

public interface TabulatedFunction extends Function {
    public void print();
    public int getPointsCount();
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException;
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException;
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException;
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException;
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;
    public void  addPoint(FunctionPoint point) throws InappropriateFunctionPointException;
    public boolean correctIndex(int index);
    public Object clone();
}