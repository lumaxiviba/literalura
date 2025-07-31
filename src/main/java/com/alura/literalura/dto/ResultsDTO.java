// src/main/java/com/alura/literalura/dto/ResultsDTO.java
package com.alura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResultsDTO(
        @JsonAlias("results") List<BookDTO> books
) {}