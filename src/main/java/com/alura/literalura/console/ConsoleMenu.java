// src/main/java/com/alura/literalura/console/ConsoleMenu.java
package com.alura.literalura.console;

import com.alura.literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleMenu {

    private final LibroService libroService;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public ConsoleMenu(LibroService libroService) {
        this.libroService = libroService;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("""
                \n=== Literalura ===
                1. Buscar libro por título
                2. Listar libros registrados
                3. Listar autores registrados
                4. Listar autores vivos en un año específico
                5. Listar libros por idioma
                6. Salir
                Elija una opción:
                """);

            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> libroService.buscarLibroPorTitulo();
                case 2 -> libroService.listarLibrosRegistrados();
                case 3 -> libroService.listarAutoresRegistrados();
                case 4 -> libroService.listarAutoresVivosEnAnio();
                case 5 -> libroService.listarLibrosPorIdioma();
                case 6 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 6);
    }
}