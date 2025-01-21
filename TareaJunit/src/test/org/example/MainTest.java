package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static javax.management.Query.times;
import static org.example.Main.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

public class MainTest {

   // Test de validación del main con dni y elección del menú cliente

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    public void UsuarioNoExiste() {

        System.setOut(new PrintStream(outContent)); // Redirige la salida estándar de la consola al ByteArrayOutputStream
        String input = "12345678A\n4\n"; // DNI no registrado y salir. Esto simula la entrada de usuario
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        GestorBaseDatos bbdd = mock(GestorBaseDatos.class); // Crea una simulación para GestorBaseDatos

        // Simula como se comportaría la base de datos al registrar a un nuevo usuario
        when(bbdd.usuarioExiste("12345678A")).thenReturn(false); // Usuario no existe
        when(bbdd.agregarUsuario("12345678A")).thenReturn(true); // Registro exitoso

        Main.dbManager = bbdd; // Reemplaza la instancia real a la bbdd con la simulación
        Main.main(null); // Ejecuta el programa principal
        String salidaGenerada = outContent.toString(); // Captura la salida de la consola
        PrintStream originalOut = System.out; // Guarda la referencia actual de System.out
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out))); // Restaura System.out a la consola
        System.out.println("Salida esperada: DNI no encontrado. Registrando usuario...");
        System.out.println("Salida generada: " + salidaGenerada);
        System.setOut(originalOut); // Vuelve a redirigir System.out al ByteArrayOutputStream

        // Verificación
        assertTrue(salidaGenerada.contains("DNI no encontrado. Registrando usuario..."),
                "El mensaje de registro no coincide. Salida generada: " + salidaGenerada);
    }

    @Test
    public void UsuarioExiste() {

        System.setOut(new PrintStream(outContent)); // Redirige la salida estándar
        String input = "12345678A\n3\n4\n"; // DNI registrado, mostrar saldo, salir. Orden actuación simulada en el test
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        GestorBaseDatos bbdd = mock(GestorBaseDatos.class); // Crea una simulación para GestorBaseDatos
        when(bbdd.usuarioExiste("12345678A")).thenReturn(true); // Usuario ya existe
        Main.dbManager = bbdd; // Reemplaza la instancia real de dbManager con el mock
        Main.saldo = 500.0; // Establece un saldo inicial para la verificación
        Main.main(null); // Ejecuta el programa principal
        String salidaGenerada = outContent.toString(); // Captura la salida de la consola
        PrintStream originalOut = System.out; // Guarda la referencia actual de System.out
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out))); // Restaura System.out a la consola
        System.out.println("Salida esperada: El saldo actual es: 500,00");
        System.out.println("Salida generada: " + salidaGenerada);
        System.setOut(originalOut); // Vuelve a redirigir System.out al ByteArrayOutputStream

        // Verificación
        assertTrue(salidaGenerada.contains("El saldo actual es: 500,00"),
                "El saldo mostrado no coincide. Salida generada: " + salidaGenerada);
    }

    @Test
    public void SalirSinAcciones() {

        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        String input = "12345678A\n4\n"; // DNI registrado, salir
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        GestorBaseDatos bbdd = mock(GestorBaseDatos.class);
        when(bbdd.usuarioExiste("12345678A")).thenReturn(true); // El usuario existe
        Main.dbManager = bbdd;
        Main.main(null);
        String salidaGenerada = outContent.toString();  // Recupera la salida generada
        String salidaEsperada = """
        Introduce tu DNI:
        
        ¿Qué desea realizar?
        1. Registrar Gasto
        2. Registrar Ingreso
        3. Mostrar Saldo
        4. Salir
        """;

        // Normalizar las cadenas para su comparación posterior en la verificación
        salidaEsperada = salidaEsperada.replace("\r\n", "\n").replace("\r", "\n").trim();
        salidaGenerada = salidaGenerada.replace("\r\n", "\n").replace("\r", "\n").trim();

        // Imprimir mensajes en la consola
        originalOut.println("\nSalida esperada:");
        originalOut.println("\n" + salidaEsperada);
        originalOut.println("\nSalida generada:");
        originalOut.println("\n" + salidaGenerada);
        System.setOut(originalOut);

        // Verificación
        assertEquals(salidaEsperada, salidaGenerada,
                "El flujo de salida no es correcto. Salida generada: " + salidaGenerada);
    }

    // Test de validación de Gasto

    @BeforeEach // Con esto el test de registrar gasto opcion invalida no falla cuando por como está planteado el código debería al menos en parte
    public void setUp() {
        gastos.clear();  // Limpiar la lista de gastos
        saldo = 1000;    // Asegúrate de que el saldo tenga un valor conocido
    }

    @Test
    public void RegistrarGastoValido() {

        // Simular entrada del usuario: opción 1 (Vacaciones) y cantidad 200, aunque en este caso es "Nómina" por error en el programa
        String input = "1\n200\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        // Para ejectuar el método y ver su funcionamiento
        Scanner scanner = new Scanner(System.in);
        registrarGasto(scanner);

        // Verificaciones
        assertEquals(1, gastos.size(), "El gasto no fue agregado a la lista.");
        // Está Nomina porque no está cambiado en el programa los conceptos de gasto
        assertEquals("Nómina", gastos.get(0).getConcepto(), "El concepto del gasto no es correcto.");
        assertEquals(200, gastos.get(0).getCantidad(), 0.01, "La cantidad del gasto no es correcta.");
        assertTrue(saldo > 0, "El saldo no se actualizó correctamente.");
        assertTrue(out.toString().contains("Se ha registrado un gasto"), "No se produjo el mensaje esperado.");
    }

    @Test
    public void OpcionInvalidaGasto() { // Las opciones no están completas ya que falta una tercera opción y que los conceptos sean correctos

        String input = "5\n"; // Simular entrada del usuario: opción 5 (inválida)
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Scanner scanner = new Scanner(System.in);
        registrarGasto(scanner);

        // Verificaciones
        assertEquals(0, gastos.size(), "No debería haberse registrado ningún gasto.");
        assertTrue(out.toString().contains("Opción no válida"), "No se produjo el mensaje esperado.");
    }

    @Test
    public void RegistrarGastoCantidadNegativa() { // Falla el test porque no hay una validación de la cantidad

        String input = "2\n-100\n"; // Simular entrada del usuario: opción 2 (Alquiler) y cantidad -100
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        double saldoInicial = saldo;
        Scanner scanner = new Scanner(System.in);
        registrarGasto(scanner);

        // Verificaciones
        assertEquals(0, gastos.size(), "No debería haberse registrado ningún gasto con cantidad negativa.");
        assertEquals(saldoInicial, saldo, 0.01, "El saldo no debería cambiar al intentar registrar un gasto negativo.");
        String textoCapturado = out.toString();
        assertFalse(textoCapturado.contains("Se ha registrado un gasto"),
                "No debería imprimirse un mensaje indicando que se registró un gasto.");
        assertTrue(textoCapturado.contains("Opción no válida") || textoCapturado.contains("Cantidad no válida"),
                "Debería haber un mensaje indicando que no se permiten cantidades negativas.");
    }


    // Test de validación de Ingreso

    @BeforeEach
    public void setUp2() { // Reseteo del saldo y gastos para evitar problemas en los test
        gastos.clear();
        ingresos.clear();
        saldo = 1000;
    }

    @Test
    public void OpcionValidaIngreso() {

        String input = "1\n500\n"; // Simular entrada del usuario: opción 1 (Nómina) y cantidad 500
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Scanner scanner = new Scanner(System.in);
        registrarIngreso(scanner);

        // Verificaciones
        assertEquals(1, ingresos.size(), "El ingreso no fue agregado a la lista.");
        assertEquals("Nómina", ingresos.get(0).getConcepto(), "El concepto del ingreso no es correcto.");
        assertEquals(500, ingresos.get(0).getCantidad(), 0.01, "La cantidad del ingreso no es correcta.");
        assertTrue(saldo > 0, "El saldo no se actualizó correctamente.");
        assertTrue(out.toString().contains("Se ha registrado un ingreso"), "No se produjo el mensaje esperado.");
    }

    @Test
    public void OpcionInvalidaIngreso() {

        String input = "3\n"; // Simular entrada del usuario: opción 3 (inválida)
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Scanner scanner = new Scanner(System.in);
        registrarIngreso(scanner);

        // Verificaciones
        assertEquals(0, ingresos.size(), "No debería haberse registrado ningún ingreso.");
        assertTrue(out.toString().contains("Opción no válida"), "No se produjo el mensaje esperado.");
    }

    @Test
    public void CantidadNegativaIngreso() { // Falla el test porque no hay una validación de la cantidad

        String input = "2\n-100\n"; // Simular entrada del usuario: opción 2 (Venta) y cantidad -100
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Scanner scanner = new Scanner(System.in);
        registrarIngreso(scanner);

        // Verificaciones
        assertEquals(0, ingresos.size(), "No debería haberse registrado ningún ingreso.");
        assertTrue(saldo == 0, "El saldo no debería haber cambiado.");
        assertTrue(out.toString().contains("Se ha registrado un ingreso"), "No se produjo el mensaje esperado.");
    }

    // Test de validación de mostrar saldo con formato

    @Test
    public void testMostrarSaldo() {

        double saldo = 100.50;
        Main.saldo = saldo;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Main.mostrarSaldo();
        String output = out.toString();

        // Verificación
        String expectedOutput = String.format("El saldo actual es: %.2f\n", saldo);
        assertEquals(expectedOutput, output, "La salida no es la esperada.");
    }

}