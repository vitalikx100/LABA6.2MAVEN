package org.example.functions.meta;

import org.example.functions.Function;

public class Composition implements Function {
    private Function fun1;
    private Function fun2;

    public Composition(Function fun1, Function fun2) {
        this.fun1 = fun1;
        this.fun2 = fun2;
    }

    @Override
    public double getLeftDomainBorder() {
        return fun1.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return fun1.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return fun1.getFunctionValue(fun2.getFunctionValue(x));
    }
}