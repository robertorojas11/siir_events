package com.siir.itq.events.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.siir.itq.events.config.exceptions.CustomExceptions.*;

public enum TipoUsuario {
    ADMIN,
    COACH,
    ALUMNO;

    @JsonCreator
    public static TipoUsuario fromString(String value){
        for(TipoUsuario tipoUsuario : TipoUsuario.values()){
            if(tipoUsuario.name().equalsIgnoreCase(value)){
                return tipoUsuario;
            }   
        }
        throw new BadRequestException("Tipo de usuario no valido: " + value);
    }
}
