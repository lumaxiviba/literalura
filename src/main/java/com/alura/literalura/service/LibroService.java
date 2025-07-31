// src/main/java/com/alura/literalura/service/LibroService.java

package com.alura.literalura.service;

import com.alura.literalura.dto.AuthorDTO;
import com.alura.literalura.dto.BookDTO;
import com.alura.literalura.dto.ResultsDTO;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LibroService {

    private static final String GUTENDEX_URL = "https://gutendex.com/books";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Busca libros por título en Gutendex y los guarda en la base de datos.
     */
    public void buscarLibroPorTitulo() {
        System.out.println("Ingrese el título del libro:");
        String titulo = scanner.nextLine().trim();

        if (titulo.isEmpty()) {
            System.out.println("El título no puede estar vacío.");
            return;
        }

        String url = GUTENDEX_URL + "?search=" + titulo.replace(" ", "%20");

        try {
            ResultsDTO response = restTemplate.getForObject(url, ResultsDTO.class);

            if (response == null || response.books() == null || response.books().isEmpty()) {
                System.out.println("No se encontraron libros con ese título.");
                return;
            }

            System.out.println("\n=== Libros encontrados en Gutendex ===");
            for (BookDTO bookDTO : response.books()) {
                procesarYGuardarLibro(bookDTO);
            }
        } catch (Exception e) {
            System.out.println("Error al conectar con Gutendex: " + e.getMessage());
        }
    }

    /**
     * Procesa un BookDTO y lo guarda como entidad Libro si no está duplicado.
     */
    private void procesarYGuardarLibro(BookDTO bookDTO) {
        // Validar que tenga al menos un autor
        if (bookDTO.authorsDTO() == null || bookDTO.authorsDTO().isEmpty()) {
            System.out.println("Libro sin autor, omitido: " + bookDTO.title());
            return;
        }

        // Tomamos el primer autor (los más relevantes suelen estar primero)
        AuthorDTO authorDTO = bookDTO.authorsDTO().get(0);

        Autor autor = obtenerOCrearAutor(authorDTO);

        // Mapear idioma (usamos el primero disponible)
        String idiomaStr = !bookDTO.languages().isEmpty() ? bookDTO.languages().get(0) : "es";
        Libro.Idioma idioma = mapearIdioma(idiomaStr);

        // Verificar si el libro ya existe por título (ignorando mayúsculas)
        Optional<Libro> libroExistente = libroRepository.findByTituloIgnoreCase(bookDTO.title());

        if (libroExistente.isPresent()) {
            System.out.printf("📚 Ya registrado: '%s' por %s%n", bookDTO.title(), autor.getNombre());
        } else {
            Libro libro = new Libro(bookDTO.title(), idioma, bookDTO.downloadCount(), autor);
            libroRepository.save(libro);
            System.out.printf("✅ Guardado: '%s' — Idioma: %s — Descargas: %d%n",
                    libro.getTitulo(), libro.getIdioma(), libro.getNumeroDescargas());
        }
    }

    /**
     * Obtiene un autor existente o crea uno nuevo.
     */
    private Autor obtenerOCrearAutor(AuthorDTO authorDTO) {
        return autorRepository.findByNombre(authorDTO.name())
                .orElseGet(() -> {
                    Autor nuevoAutor = new Autor(
                            authorDTO.name(),
                            authorDTO.birthYear(),
                            authorDTO.deathYear()
                    );
                    return autorRepository.save(nuevoAutor);
                });
    }

    /**
     * Mapea un código de idioma de Gutendex a nuestro enum Idioma.
     */
    private Libro.Idioma mapearIdioma(String lang) {
        return switch (lang.toLowerCase()) {
            case "es" -> Libro.Idioma.ES;
            case "en" -> Libro.Idioma.EN;
            case "fr" -> Libro.Idioma.FR;
            case "pt" -> Libro.Idioma.PT;
            default -> Libro.Idioma.ES; // Idioma por defecto
        };
    }

    /**
     * Lista todos los libros registrados en la base de datos.
     */
    public void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.println("\n=== Libros registrados ===");
            libros.forEach(libro -> System.out.printf(
                    "📘 '%s' — %s — %d descargas — Autor: %s%n",
                    libro.getTitulo(),
                    libro.getIdioma(),
                    libro.getNumeroDescargas(),
                    libro.getAutor().getNombre()
            ));
        }
    }

    /**
     * Lista todos los autores registrados.
     */
    public void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            System.out.println("\n=== Autores registrados ===");
            autores.forEach(autor -> System.out.printf(
                    "👤 %s (%d-%s)%n",
                    autor.getNombre(),
                    autor.getFechaNacimiento() != null ? autor.getFechaNacimiento() : "?",
                    autor.getFechaFallecimiento() != null ? autor.getFechaFallecimiento().toString() : "actual"
            ));
        }
    }

    /**
     * Lista autores vivos en un año específico.
     */
    public void listarAutoresVivosEnAnio() {
        System.out.println("Ingrese el año:");
        try {
            int anio = Integer.parseInt(scanner.nextLine().trim());
            List<Autor> autores = autorRepository.findAutoresVivosEnAnio(anio);
            if (autores.isEmpty()) {
                System.out.printf("No se encontraron autores vivos en el año %d.%n", anio);
            } else {
                System.out.printf("\n=== Autores vivos en %d ===%n", anio);
                autores.forEach(autor -> System.out.printf(
                        "👤 %s (%d–%s)%n",
                        autor.getNombre(),
                        autor.getFechaNacimiento(),
                        autor.getFechaFallecimiento() == null ? "actual" : autor.getFechaFallecimiento().toString()
                ));
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un número válido.");
        }
    }

    /**
     * Lista libros por idioma.
     */
    public void listarLibrosPorIdioma() {
        System.out.println("Idiomas disponibles: ES, EN, FR, PT");
        System.out.println("Ingrese el código del idioma:");
        String codigo = scanner.nextLine().trim().toUpperCase();

        try {
            Libro.Idioma idioma = Libro.Idioma.valueOf(codigo);
            List<Libro> libros = libroRepository.findByIdioma(idioma);
            if (libros.isEmpty()) {
                System.out.printf("No hay libros en idioma %s.%n", idioma);
            } else {
                System.out.printf("\n=== Libros en %s ===%n", idioma);
                libros.forEach(libro -> System.out.printf(
                        "📘 '%s' — %d descargas — Autor: %s%n",
                        libro.getTitulo(),
                        libro.getNumeroDescargas(),
                        libro.getAutor().getNombre()
                ));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Idioma no válido. Use: ES, EN, FR, PT.");
        }
    }
}