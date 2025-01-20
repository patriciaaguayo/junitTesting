package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IngresoTest {

    @Test
    public void getConcepto() {
        Ingreso ingreso = new Ingreso("Nómina", 1500.0);
        System.out.println("El concepto es " + ingreso.getConcepto());
        assertEquals("Nómina", ingreso.getConcepto(), "El concepto no coincide");
    }

    @Test
    public void getCantidad() {
        Ingreso ingreso = new Ingreso("Nómina", 1500.0);
        System.out.println("La cantidad es " + ingreso.getCantidad() + " euros");
        assertEquals(1500.0, ingreso.getCantidad(), "La cantidad no coincide");
    }
}