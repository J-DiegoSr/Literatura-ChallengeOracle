package com.alura.challenge.Literatura_ChallengeOracle.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(@JsonAlias("title") String title,
                         @JsonAlias("authors") List<DatosAutor> authors,
                         @JsonAlias("languages") List<String> languages,
                         @JsonAlias("download_count") Integer download_count)
{


}
