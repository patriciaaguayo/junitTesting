package org.example;

public class Gasto {
    private String concepto;
    private double cantidad;

    public Gasto(String concepto, double cantidad) {
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
