package com.siir.itq.events.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.siir.itq.events.config.exceptions.CustomExceptions.BadRequestException;

public enum TipoEvento {
    TORNEO, 
    CAMPEONATO,
    ENTRENAMIENTO;

    @JsonCreator
    public static TipoEvento fromString(String value){
        for(TipoEvento tipoEvento : TipoEvento.values()){
            if(tipoEvento.name().equalsIgnoreCase(value)){
                return tipoEvento;
            }
        }
        throw new BadRequestException("Tipo de evento inválido: " + value);
    }
}
