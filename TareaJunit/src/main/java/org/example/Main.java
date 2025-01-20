package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static double saldo = 0.0;
    public static GestorBaseDatos dbManager = new GestorBaseDatos(); // No puede ser final para la comprobación en los test
    public static final List<Gasto> gastos = new ArrayList<>();
    public static final List<Ingreso> ingresos = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduce tu DNI:");
        String dni = scanner.nextLine();

        if (!dbManager.usuarioExiste(dni)) {
            System.out.println("DNI no encontrado. Registrando usuario...");
            if (!dbManager.agregarUsuario(dni)) {
                System.out.println("Error al registrar el usuario. Saliendo...");
                return;
            }
        }

        boolean continuar = true;
        while (continuar) {
            System.out.println("\n¿Qué desea realizar?\n1. Registrar Gasto\n2. Registrar Ingreso\n3. Mostrar Saldo\n4. Salir");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir salto de línea

            switch (opcion) {
                case 1 -> registrarGasto(scanner);
                case 2 -> registrarIngreso(scanner);
                case 3 -> mostrarSaldo();
                case 4 -> continuar = false;
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    public static void registrarGasto(Scanner scanner) {
        System.out.println("Seleccione un concepto:\n1. Vacaciones\n2. Alquiler\n3. IRPF (15%)\n4. Vicios");
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consumir salto de línea

        String concepto;

        if (opcion == 1) {
            concepto = "Nómina";
        } else if (opcion == 2) {
            concepto = "Venta en páginas de segunda mano";
        } else {
            System.out.println("Opción no válida. Regresando al menú.");
            return;  // Regresar al menú sin hacer nada más
        }

        System.out.println("Ingrese la cantidad del gasto:");
        double cantidad = scanner.nextDouble();
        scanner.nextLine();

        gastos.add(new Gasto(concepto, cantidad));
        saldo -= cantidad;
        System.out.printf("Se ha registrado un gasto de %.2f en %s. Saldo actual: %.2f\n", cantidad, concepto, saldo);
    }

    public static void registrarIngreso(Scanner scanner) {
        System.out.println("Seleccione un concepto:\n1. Nómina\n2. Venta en páginas de segunda mano");
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consumir salto de línea

        String concepto;

        if (opcion == 1) {
            concepto = "Nómina";
        } else if (opcion == 2) {
            concepto = "Venta en páginas de segunda mano";
        } else {
            System.out.println("Opción no válida. Regresando al menú.");
            return;  // Regresar al menú sin hacer nada más
        }

        System.out.println("Ingrese la cantidad del ingreso:");
        double cantidad = scanner.nextDouble();
        scanner.nextLine();

        ingresos.add(new Ingreso(concepto, cantidad));
        saldo += cantidad;
        System.out.printf("Se ha registrado un ingreso de %.2f en %s. Saldo actual: %.2f\n", cantidad, concepto, saldo);
    }

    public static void mostrarSaldo() {
        System.out.printf("El saldo actual es: %.2f\n", saldo);
    }
}
