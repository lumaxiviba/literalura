# 📚 Literalura – Tu biblioteca de libros del Proyecto Gutenberg

> Una aplicación de consola desarrollada con **Spring Boot** que permite buscar, almacenar y consultar libros del **Proyecto Gutenberg** a través de la API [Gutendex](https://gutendex.com/).

![Java](https://img.shields.io/badge/Java-17+-f89822?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.4-6db33f?logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?logo=postgresql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-4.0-blueviolet?logo=apache-maven&logoColor=white)

---

## 🌟 ¿Qué es Literalura?

**Literalura** es una aplicación de línea de comandos que conecta con la API de [Gutendex](https://gutendex.com/) (metadatos de libros del Proyecto Gutenberg) para permitirte:

- Buscar libros por título.
- Guardar libros en una base de datos local (PostgreSQL).
- Consultar libros y autores registrados.
- Filtrar autores vivos en un año específico.
- Listar libros por idioma.

Ideal para aprender **Spring Boot**, **JPA**, **consumo de APIs REST** y **patrones de diseño en Java**.

---

## 🛠 Funcionalidades

| Opción | Descripción |
|-------|-------------|
| 1 | 🔍 Buscar libro por título (desde Gutendex) |
| 2 | 📚 Listar todos los libros registrados |
| 3 | 👤 Listar todos los autores registrados |
| 4 | 🕰 Listar autores vivos en un año específico |
| 5 | 🌍 Listar libros por idioma (ES, EN, FR, PT) |
| 6 | ❌ Salir |

---

## 📦 Tecnologías utilizadas

- **Java 17+** – Lenguaje principal
- **Spring Boot 3.5.4** – Framework principal
- **Spring Data JPA** – Persistencia y acceso a datos
- **PostgreSQL** – Base de datos relacional
- **Maven** – Gestión de dependencias
- **RestTemplate** – Consumo de API REST (Gutendex)
- **Gutendex API** – [https://gutendex.com/](https://gutendex.com/)

---

## 🚀 Ejecución del proyecto

### 1. Prerrequisitos

- Java 17 o superior
- Maven
- PostgreSQL instalado y en ejecución

### 2. Configuración de la base de datos

Crea una base de datos en PostgreSQL:

```sql
CREATE DATABASE literalura_db;
