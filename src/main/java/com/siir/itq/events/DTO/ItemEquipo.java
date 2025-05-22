package com.siir.itq.events.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class ItemEquipo {
    @NotNull
    @Valid
    private EquipoEvento equipoEvento;

    //getters and setters
    public EquipoEvento getEquipoEvento() {
        return equipoEvento;
    }

    public void setEquipoEvento(EquipoEvento equipoEvento) {
        this.equipoEvento = equipoEvento;
    }
}
