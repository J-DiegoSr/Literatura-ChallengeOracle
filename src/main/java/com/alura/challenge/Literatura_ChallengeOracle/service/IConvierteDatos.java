package com.alura.challenge.Literatura_ChallengeOracle.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);

}
