# Proyecto Literatura Challenge Oracle

Este es un proyecto de gestión de literatura desarrollado con Spring Boot y JPA para el desafío de Oracle.

## Contenido del Proyecto

- **Entidad `Autor`**: Representa a los autores en la base de datos.
- **Repositorio `AutorRepository`**: Interfaz que permite operaciones CRUD y consultas personalizadas sobre la entidad `Autor`.
- **Menús Interactivos**: Menús de selección de libros, autores e idiomas con diseños visuales atractivos usando Java.

## Configuración del Proyecto

### Prerrequisitos

- JDK 11 o superior.
- Maven 3.6 o superior.
- Spring Boot 2.5 o superior.
- Base de datos compatible con JPA (por ejemplo, MySQL, PostgreSQL).

### Configuración del entorno

1. Clona este repositorio en tu máquina local:

         ```bash
         git clone https://github.com/tu_usuario/proyecto-literatura-challenge-oracle.git
         
   
2. Navega al directorio del proyecto:

           cd proyecto-literatura-challenge-oracle

4. Configura los detalles de tu base de datos en el archivo application.properties:

        spring.datasource.url=jdbc:mysql://localhost:3306/tu_base_de_datos
        spring.datasource.username=tu_usuario
        spring.datasource.password=tu_contraseña
        spring.jpa.hibernate.ddl-auto=update
      


5. Compila y ejecuta el proyecto con Maven:

        mvn clean install
        mvn spring-boot:run

  
                                                                    Menú Principal

   
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
                         |                          |   |
                         |  0. ❌ Salir              |   |
                         |__________________________|   |
  

