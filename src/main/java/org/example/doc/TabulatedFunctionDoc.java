package org.example.doc;

import org.example.functions.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TabulatedFunctionDoc implements TabulatedFunction {
    private TabulatedFunction obj;
    private String fileName;
    private boolean changed;

    private FXMLMainFormController controller;

    public TabulatedFunctionDoc() {
        this.obj = new ArrayTabulatedFunction(0, 4, 5);
        this.fileName = null;
        this.changed = false;
        this.controller = null;
    }

    public TabulatedFunctionDoc(String filename) throws FileNotFoundException {
        this.fileName = filename;
        this.obj = TabulatedFunctions.readTabulatedFunction(new FileReader(this.fileName));
        this.changed = false;
        this.controller = null; //?
    }

    public TabulatedFunctionDoc(TabulatedFunction function) {
        this.obj = function;
        this.fileName = null;
        this.changed = false;
        this.controller = null; //?
    }

    public void newFunction(double leftX, double rightX, int pointsCount) {
        obj = new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        changed = true;
        CallRedraw();
    }

    public void tabulateFunction(Function function, double leftX, double rightX, int pointsCount){
        obj = TabulatedFunctions.tabulate(function, leftX, rightX, pointsCount);
        changed = true;
        CallRedraw();
    }

    public void saveFunctionAs(String filenName) {
        fileName = filenName;
        changed = false;

        try (FileWriter file = new FileWriter(filenName)) {
            JSONObject employeeObject = new JSONObject();
            int pointsCount = obj.getPointsCount();
            employeeObject.put("pointsCount", pointsCount);
            for (int i = 0; i < pointsCount; ++i) {
                employeeObject.put("x", obj.getPointX(i));
                employeeObject.put("y", obj.getPointY(i));
            }
            file.write(employeeObject.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Возможно неправильно работает
    public void loadFunction(String fileName) {
        this.fileName = fileName;
        FileReader reader = null;
        try {
            reader = new FileReader(fileName);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            int pointsCount = (int) jsonObject.get("pointsCount");
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; ++i) {
                points[i] = new FunctionPoint((int) jsonObject.get("x"), (int) jsonObject.get("y"));
            }
            obj = new ArrayTabulatedFunction(points);
            CallRedraw();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveFunction(){
        saveFunctionAs(fileName);
    }

    public boolean isModified() {
        return changed;
    }

    public boolean fileNameAssigned() {
        return fileName != null;
    }

    @Override
    public double getLeftDomainBorder() {
        return obj.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return obj.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return obj.getFunctionValue(x);
    }

    @Override
    public void print() {
        obj.print();
    }

    @Override
    public int getPointsCount() {
        return obj.getPointsCount();
    }

    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        return obj.getPoint(index);
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        obj.setPoint(index, point);
        changed = true;
        CallRedraw();
    }

    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        return obj.getPointX(index);
    }

    @Override
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        obj.setPointX(index, x);
        changed = true;
        CallRedraw();
    }

    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        return obj.getPointY(index);
    }

    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        obj.setPointY(index, y);
        changed = true;
        CallRedraw();
    }

    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        obj.deletePoint(index);
        changed = true;
        CallRedraw();
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        obj.addPoint(point);
        changed = true;
        CallRedraw();
    }

    @Override
    public boolean correctIndex(int index) {
        return correctIndex(index);
    }

    @Override
    public int hashCode() {
        return obj.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.equals(obj);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void CallRedraw(){
        if(controller != null) {
            controller.redraw();
        }
    }

    public void registerRedrawFunctionController(FXMLMainFormController controller) {
        this.controller = controller;
    }
}
