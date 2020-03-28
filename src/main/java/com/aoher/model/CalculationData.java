package com.aoher.model;

public class CalculationData {

    private Double sum = 0D;

    public CalculationData() {
    }

    public Double getSum() {
        return sum;
    }

    public void add(Double value) {
        this.sum += value;
    }
}
