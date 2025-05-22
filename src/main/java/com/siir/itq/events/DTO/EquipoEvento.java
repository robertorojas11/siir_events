package com.siir.itq.events.DTO;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class EquipoEvento {
    private UUID idEquipoLocal;

    @Size(min = 1, max = 255)
    private String nombreEquipoForaneo;

    //Getters and Setters
    public UUID getIdEquipoLocal() {
        return idEquipoLocal;
    }
    public void setIdEquipoLocal(UUID idEquipoLocal) {
        this.idEquipoLocal = idEquipoLocal;
    }
    public String getNombreEquipoForaneo() {
        return nombreEquipoForaneo;
    }
    public void setNombreEquipoForaneo(String nombreEquipoForaneo) {
        this.nombreEquipoForaneo = nombreEquipoForaneo;
    }

    @AssertTrue(message = "Debe especificarse 'idEquipoLocal' o 'equipoForaneoNombre', pero no ambos ni ninguno.")
    public boolean isValid() {
        return (idEquipoLocal != null && nombreEquipoForaneo == null) || (idEquipoLocal == null && nombreEquipoForaneo != null);
    }
}
