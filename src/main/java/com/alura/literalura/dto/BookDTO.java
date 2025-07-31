// src/main/java/com/alura/literalura/dto/BookDTO.java
package com.alura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookDTO(
        Integer id,
        String title,
        @JsonAlias("authors") List<AuthorDTO> authorsDTO,
        @JsonAlias("languages") List<String> languages,
        @JsonAlias("download_count") Integer downloadCount
) {}