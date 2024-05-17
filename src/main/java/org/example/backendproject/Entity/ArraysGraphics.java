package org.example.backendproject.Entity;

public class ArraysGraphics {
    private double[] magnitudes;
    private long[] times;

    private double[] spectrum ;

    private double[] freqs;

    public ArraysGraphics(double[] magnitudes, long[] times, double[] spectrum, double[] freqs) {
        this.magnitudes = magnitudes;
        this.times = times;
        this.spectrum = spectrum;
        this.freqs = freqs;
    }

    public ArraysGraphics() {

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

    public double[] getSpectrum() {
        return spectrum;
    }

    public void setSpectrum(double[] spectrum) {
        this.spectrum = spectrum;
    }

    public double[] getFreqs() {
        return freqs;
    }

    public void setFreqs(double[] freqs) {
        this.freqs = freqs;
    }

    // Getters y setters
}

