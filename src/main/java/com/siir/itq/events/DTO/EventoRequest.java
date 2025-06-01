package com.siir.itq.events.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoRequest {

    @JsonUnwrapped
    private EventoBase eventoBase;

    @NotEmpty(message = "La lista de participantes no puede estar vacía")
    private List<EquipoEventoRequest> participantes;
}