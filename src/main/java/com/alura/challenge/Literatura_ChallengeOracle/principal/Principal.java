package com.alura.challenge.Literatura_ChallengeOracle.principal;


import com.alura.challenge.Literatura_ChallengeOracle.model.Autor;
import com.alura.challenge.Literatura_ChallengeOracle.model.DatosAutor;
import com.alura.challenge.Literatura_ChallengeOracle.model.DatosLibro;
import com.alura.challenge.Literatura_ChallengeOracle.model.Libro;
import com.alura.challenge.Literatura_ChallengeOracle.repository.AutorRepository;
import com.alura.challenge.Literatura_ChallengeOracle.repository.LibroRepository;
import com.alura.challenge.Literatura_ChallengeOracle.service.ConsumoAPI;
import com.alura.challenge.Literatura_ChallengeOracle.service.ConvierteDatos;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final String url = "https://gutendex.com/books/";
    private List<DatosLibro> datosLibros = new ArrayList<>();
    private List<DatosAutor> datosAutores = new ArrayList<>();
    private List<Libro> libros;
    private List<Autor> autores;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {

            // Menú en forma de libro con emojis
            var menu = """
                   BIENVENIDO(A) AL SISTEMA DE CONSULTA DE LIBROS JDSR \n
                   ___________________________
                  /                          /|
                 /        MENÚ              / |
                /__________________________/  |
               |                          |  /|
               |  1. 📚 Buscar Libro       | / |
               |     por nombre           |/  |
               |__________________________|   |
               |                          |  /|
               |  2. 📖 Mostrar Libros     | / |
               |     registrados          |/  |
               |__________________________|   |
               |                          |  /|
               |  3. 👨‍🏫 Mostrar Autores   | / |
               |     registrados          |/  |
               |__________________________|   |
               |                          |  /|
               |  4. 📅 Mostrar Autores    | / |
               |     vivos por años       |/  |
               |__________________________|   |
               |                          |  /|
               |  5. 🌐 Mostrar por        | / |
               |     Idiomas              |/  |
               |__________________________|   |
               |                          |  /|
               |  6. 📊 Generar estadísticas | / |
               |     de Libros            |/  |
               |__________________________|   |
               |                          |  /|
               |  7. 🏆 Mostrar Top 10     | / |
               |     Libros más           |/  |
               |     descargados          |   |
               |__________________________|   |
               |                          |  /|
               |  8. 🔎 Buscar Autor       | / |
               |     por nombre           |/  |
               |__________________________|   |
               |                          |  /|
               |  9. 🗂️ Listar Autores por | / |
               |     año de nacimiento    |/  |
               |     o fallecimiento      |   |
               |__________________________|   |
               |                          |  /|
               |  0. ❌ Salir              | / |
               |                          |/  |
               |__________________________|   |
               |                          |  /|
               |__________________________| / |
               |_o_o_o_o_o_o_o_o_o_o_o_o_o_|/  |
        """;
            System.out.println(menu);
            System.out.print("> ");
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1 -> buscarLibroPorNombre();
                case 2 -> mostrarLibrosRegistrados();
                case 3 -> mostrarAutoresRegistrados();
                case 4 -> mostrarAutoresPorFecha();
                case 5 -> mostrarPorIdiomas();
                case 6 -> mostrarEstadisticasLibros();
                case 7 -> mostrarTopLibrosMasDescargados();
                case 8 -> buscarAutorPorNombre();
                case 9 -> listarAutoresPorAtributo();
                case 0 -> System.out.println("Gracias por su visita.Cerrando Sistema...");
                default -> System.out.println("Opción incorrecta. Intente nuevamente.");
            }
        }
    }

    private void buscarLibroPorNombre() {
        System.out.println("Escribe el nombre del libro que desea buscar: ");
        var nombreLibro = teclado.nextLine();

        String urlFinal = url + "?search=" + URLEncoder.encode(nombreLibro, StandardCharsets.UTF_8);
        System.out.println("URL construida: " + urlFinal);

        var json = consumoAPI.obtenerDatos(urlFinal);
        System.out.println("Respuesta JSON: " + json);

        try {

            var rootNode = conversor.obtenerDatos(json, Map.class);
            var results = (List<Map<String, Object>>) rootNode.get("results");

            if (results != null && !results.isEmpty()) {
                for (var result : results) {

                    var datosLibroJson = conversor.obtenerDatos(new ObjectMapper().writeValueAsString(result), DatosLibro.class);
                    Libro libro = new Libro(datosLibroJson);

                    // GUARDAR AUTORES ASOCIADOS AL LIBRO
                    for (DatosAutor datosAutor : datosLibroJson.authors()) {
                        Autor autor = new Autor(datosAutor);
                        try {
                            // CONSULTA SI EL AUTOR EXISTE, VALIDACION
                            if (!autorRepository.existsByNombre(autor.getNombre())) {
                                autorRepository.save(autor);
                                System.out.println("Autor guardado correctamente: " + autor.getNombre());
                            } else {
                                System.out.println("El autor ya se encuentra registrado en nuestra base de datos: " + autor.getNombre());
                            }
                        } catch (Exception e) {
                            System.out.println("Error al guardar el autor: " + e.getMessage());
                        }
                    }

                    //  GUARDAR LIBRO EN LA BASE
                    try {
                        libroRepository.save(libro);
                        System.out.println("Libro guardado correctamente: " + libro.getTitle());
                    } catch (Exception e) {
                        System.out.println("Error al guardar el libro: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("No se encontraron libros para la búsqueda solicitada.");
            }
        } catch (Exception e) {
            System.out.println("Error al procesar los datos del JSON: " + e.getMessage());
        }
    }

    private void mostrarLibrosRegistrados() {
        try {
            libros = libroRepository.findAll();
            if (libros.isEmpty()) {
                System.out.println("No existen libros registrados...");
                System.out.println("Presione Enter para continuar...");
                teclado.nextLine();
            } else {
                libros.forEach(l -> {
                    System.out.printf("""
                            Libro: %s
                            Autor: %s
                            Idioma: %s
                            Descargas: %s
                            %n""", l.getTitle(), l.getAuthor(), l.getLanguage(), l.getDownload_count().toString());
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void mostrarAutoresRegistrados() {
        try {
            autores = autorRepository.findAll();
            if (autores.isEmpty()) {
                System.out.println("No existen libros registrados...");
                System.out.println("Presione Enter para continuar...");
                teclado.nextLine();
            } else {
                autores.forEach(a -> {
                    System.out.printf("""
                            Autor: %s
                            Nacimiento: %s
                            Fallecimiento: %s
                            %n""",
                            a.getNombre(), a.getDiaCumpleanos() != null ? a.getDiaCumpleanos().toString() : "No se encuentra fecha de nacimiento",
                            a.getDiaFallecio() != null ? a.getDiaFallecio().toString() : "En la actualidad");
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void mostrarAutoresPorFecha() {
        System.out.println("Ingrese el año a buscar para los autores que estaban vivos en ese periodo:");
        System.out.print("> ");
        int anio = teclado.nextInt();
        teclado.nextLine();

        try {

            //CONSULTA AL RESPOSITORIO SOBRE LOS AUTORES VIVOS EN EL AÑO SOLICITADO
            List<Autor> autoresVivos = autorRepository.findAuthorsAliveInYear(anio);


            if (autoresVivos.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + anio);
            } else {
                System.out.println("Autores vivos en el año " + anio + ":");
                autoresVivos.forEach(a -> {
                    System.out.printf("""
                        Autor: %s
                        Nacimiento: %s
                        Fallecimiento: %s
                        %n""",
                            a.getNombre(),
                            a.getDiaCumpleanos() != null ? a.getDiaCumpleanos() : "Desconocido",
                            a.getDiaFallecio() != null ? a.getDiaFallecio() : "Aún sigue vivo");
                    teclado.nextLine();
                });
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error al consultar los autores: " + e.getMessage());
        }
    }

    private void mostrarPorIdiomas() {
        System.out.println( """
          🌍  Selecciona un idioma para mostrar los libros registrados:
        ─────────────────────────────────────────
        1: 🇺🇸 Inglés (en)
        2: 🇪🇸 Español (es)
        3: 🇫🇷 Francés (fr)
        4: 🇩🇪 Alemán (de)
        5: 🇮🇹 Italiano (it)
        6: 🌐 Otro idioma
        ─────────────────────────────────────────
        """);
        System.out.print("> ");
        int opcion = teclado.nextInt();
        teclado.nextLine();

        String idioma = null;

        // Mapear la opción seleccionada a un código de idioma
        switch (opcion) {
            case 1 -> idioma = "en";
            case 2 -> idioma = "es";
            case 3 -> idioma = "fr";
            case 4 -> idioma = "de";
            case 5 -> idioma = "it";
            case 6 -> {
                System.out.println("Ingrese el código del idioma, por ejemplo: en, es, fr):");
                System.out.print("> ");
                idioma = teclado.nextLine();
            }
            default -> {
                System.out.println("Opción inválida. Volviendo al menú principal...");
                return;
            }
        }

        try {
            // CONSULTAR LIBROS X IDIMOAS
            List<Libro> librosPorIdioma = libroRepository.findByLanguage(idioma);

            if (librosPorIdioma.isEmpty()) {
                System.out.println("No existen libros registrados en el idioma seleccionado: (" + idioma + ").");
            } else {
                System.out.println("Libros disponibles en el idioma seleccionado: (" + idioma + "):");
                librosPorIdioma.forEach(libro -> System.out.printf("""
                    Título: %s
                    Autor: %s
                    Idioma: %s
                    Descargas: %d
                    %n""",
                        libro.getTitle(),
                        libro.getAuthor(),
                        libro.getLanguage(),
                        libro.getDownload_count()));
                teclado.nextLine();
            }
        } catch (Exception e) {
            System.out.println("Error al consultar los libros: " + e.getMessage());
        }
    }

    private void mostrarEstadisticasLibros() {
        try {

            List<Libro> libros = libroRepository.findAll();

            if (libros.isEmpty()) {
                System.out.println("No existen libros registrados para generar estadísticas.");
                return;
            }

            //OBTENER ESTADISTICAS
            DoubleSummaryStatistics stats = libros.stream()
                    .mapToDouble(Libro::getDownload_count)
                    .summaryStatistics();

            // ESTADISTICAS
            System.out.println("Estadísticas de descargas de libros:");
            System.out.printf("Número total de descargas: %.0f%n", stats.getSum());
            System.out.printf("Promedio de descargas: %.2f%n", stats.getAverage());
            System.out.printf("Máximo número de descargas: %.0f%n", stats.getMax());
            System.out.printf("Mínimo número de descargas: %.0f%n", stats.getMin());
            teclado.nextLine();
        } catch (Exception e) {
            System.out.println("Error al generar las estadísticas: " + e.getMessage());
        }
    }

    private void mostrarTopLibrosMasDescargados() {
        try {
            //  LOS LIBROS MAS DESCARGADOS
            List<Libro> topLibros = libroRepository.findTop10ByOrderByDownloadCountDesc(PageRequest.of(0,10));

                    //(PageRequest.of(0, 10));
            if (topLibros.isEmpty()) {
                System.out.println("No existen libros registrados.");
                return;
            }

            // MOSTRAR TOP 10
            System.out.println("Top 10 libros más descargados:");
            topLibros.forEach(libro -> System.out.printf("""
                Título: %s
                Autor: %s
                Descargas: %d
                %n""",
                    libro.getTitle(),
                    libro.getAuthor(),
                    libro.getDownload_count()));
        } catch (Exception e) {
            System.out.println("Error al obtener el top 10 de libros: " + e.getMessage());
        }
    }

    private void buscarAutorPorNombre() {
        System.out.println("Ingrese el nombre del autor a buscar:");
        System.out.print("> ");
        String nombreAutor = teclado.nextLine();

        try {
            // CONSULTA EN LA BASE LOS NOMBRES DE AUTORES
            List<Autor> autores = autorRepository.findByName(nombreAutor);

            if (autores.isEmpty()) {
                System.out.println("El autor no existe en nuestra base de datos. Buscando en la API...");


                String urlFinal = url + "?search=" + URLEncoder.encode(nombreAutor, StandardCharsets.UTF_8);
                var json = consumoAPI.obtenerDatos(urlFinal);


                var rootNode = conversor.obtenerDatos(json, Map.class);
                var results = (List<Map<String, Object>>) rootNode.get("results");

                if (results == null || results.isEmpty()) {
                    System.out.println("No se encontraron autores en la API con el nombre: " + nombreAutor);
                    return;
                }

                  //JALA LOS DATOS DE CADA AUTOR Y LOS ALMACENA EN LA BASE
                for (var result : results) {
                    var datosAutorJson = conversor.obtenerDatos(new ObjectMapper().writeValueAsString(result.get("authors")), DatosAutor[].class);

                    for (DatosAutor datosAutor : datosAutorJson) {
                        Autor autor = new Autor(datosAutor);

                        if (!autorRepository.existsByNombre(autor.getNombre())) {
                            autorRepository.save(autor);
                            System.out.println("Autor guardado correctamente: " + autor.getNombre());
                        } else {
                            System.out.println("El autor ya existe en la base de datos: " + autor.getNombre());
                        }
                    }
                }


                autores = autorRepository.findByName(nombreAutor);
            }

            // AUTOTES ENCONTRADOS
            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores con el nombre: " + nombreAutor);
            } else {
                System.out.println("Autores encontrados:");
                autores.forEach(a -> System.out.printf("""
                    Nombre: %s
                    Año de nacimiento: %s
                    Año de fallecimiento: %s
                    %n""",
                        a.getNombre(),
                        a.getDiaCumpleanos() != null ? a.getDiaCumpleanos() : "Desconocido",
                        a.getDiaFallecio() != null ? a.getDiaFallecio() : "Aún vivo"));
            }
        } catch (Exception e) {
            System.out.println("Error al buscar el autor: " + e.getMessage());
        }
    }

    private void listarAutoresPorAtributo() {
        // Menú de selección de atributos con emojis
        System.out.println("""
        
        🤔  ¿Qué atributo deseas consultar?
        ─────────────────────────────────────
        1: 📅 Año de nacimiento
        2: ⚰️  Año de fallecimiento
        ─────────────────────────────────────
        """);
        System.out.print("> ");
        int opcion = teclado.nextInt();
        teclado.nextLine();

        System.out.println("Ingrese el año a consultar:");
        System.out.print("> ");
        int anio = teclado.nextInt();
        teclado.nextLine();

        try {
            List<Autor> autores = switch (opcion) {
                case 1 -> autorRepository.findByBirthYear(anio);
                case 2 -> autorRepository.findByDeathYear(anio);
                default -> {
                    System.out.println("Opción incorrecta. Volviendo al menú principal...");
                    yield new ArrayList<>();
                }
            };

            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores en el año especificado.");
            } else {
                System.out.println("Autores encontrados:");
                autores.forEach(a -> System.out.printf("""
                    Nombre: %s
                    Año de nacimiento: %s
                    Año de fallecimiento: %s
                    %n""",
                        a.getNombre(),
                        a.getDiaCumpleanos() != null ? a.getDiaCumpleanos() : "Desconocido",
                        a.getDiaFallecio() != null ? a.getDiaFallecio() : "Aún vivo"));
            }
        } catch (Exception e) {
            System.out.println("Error al consultar los autores: " + e.getMessage());
        }
    }

}
