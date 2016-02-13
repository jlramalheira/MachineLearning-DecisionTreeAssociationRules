/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author joao
 */
public class Limits {

    private double[] minValues;
    private double[] maxValues;
    private double[][] steps;

    public Limits(int size) {
        minValues = new double[size];
        maxValues = new double[size];
        
        steps = new double[size][size];

        for (int i = 0; i < size; i++) {
            minValues[i] = Double.MAX_VALUE;
            maxValues[i] = Double.MIN_VALUE;
        }
    }

    public double[] getMinValues() {
        return minValues;
    }

    public double[] getMaxValues() {
        return maxValues;
    }

    public void insertMinValue(double value, int position) {
        if (value < minValues[position]) {
            minValues[position] = value;
        }
    }

    public void insertMaxValue(double value, int position) {
        if (value > maxValues[position]) {
            maxValues[position] = value;
        }
    }

    public double[][] getSteps() {
        return steps;
    }
    
    public void calcSteps(int numBoxs) {
        for (int i = 0; i < this.maxValues.length; i++) {
            double step = (this.maxValues[i] - this.minValues[i]) / numBoxs;
            for (int j = 0; j < this.maxValues.length; j++) {
                this.steps[i][j] = j*step;
            }
        }
    }

}
