package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GestorBaseDatosTest {

    @Test
    public void agregarUsuario() {
        GestorBaseDatos bbdd = new GestorBaseDatos();
        assertTrue(bbdd.agregarUsuario("12345678A"),"El usuario debería haberse agregado correctamente");
        assertFalse(bbdd.agregarUsuario("12345678A"), "No se debería poder agregar un usuario duplicado");
    }

    @Test
    public void usuarioExiste() {
        GestorBaseDatos bbdd = new GestorBaseDatos();
        bbdd.agregarUsuario("87654321B");
        assertTrue(bbdd.usuarioExiste("87654321B"), "El usuario debería existir en la base de datos");
        assertFalse(bbdd.usuarioExiste("12345678C"), "El usuario no debería existir en la base de datos");
    }
}