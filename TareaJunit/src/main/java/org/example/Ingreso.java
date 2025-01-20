package org.example;

public class Ingreso {
    private String concepto;
    private double cantidad;

    public Ingreso(String concepto, double cantidad) {
        this.concepto = concepto;
        this.cantidad = cantidad;
    }

    public String getConcepto() {
        return concepto;
    }

    public double getCantidad() {
        return cantidad;
    }
}