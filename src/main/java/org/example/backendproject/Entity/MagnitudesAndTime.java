package org.example.backendproject.Entity;

public class MagnitudesAndTime {
    private double[] magnitudes;
    private long[] times;

    public MagnitudesAndTime(double[] magnitudes, long[] times) {
        this.magnitudes = magnitudes;
        this.times = times;
    }

    public MagnitudesAndTime() {

    }

    public double[] getMagnitudes() {
        return magnitudes;
    }

    public void setMagnitudes(double[] magnitudes) {
        this.magnitudes = magnitudes;
    }

    public long[] getTimes() {
        return times;
    }

    public void setTimes(long[] times) {
        this.times = times;
    }

    // Getters y setters
}

