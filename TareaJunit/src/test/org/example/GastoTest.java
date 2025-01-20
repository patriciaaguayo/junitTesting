package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GastoTest {

    @Test
    public void getConcepto() {

        Gasto gasto = new Gasto("Alquiler", 800.0);
        System.out.println("El concepto del gasto es " + gasto.getConcepto());
        assertEquals("Alquiler", gasto.getConcepto(), "El concepto no coincide");
    }

    @Test
    public void getCantidad() {
        Gasto gasto = new Gasto("Alquiler", 800.0);
        System.out.println("La cantidad del gasto es " + gasto.getCantidad() + " euros");
        assertEquals(800.0, gasto.getCantidad(), "La cantidad no coincide");
    }
}