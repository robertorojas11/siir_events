package com.siir.itq.events.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoRequest {

    @NotNull(message = "Los datos base del evento no pueden ser nulos")
    @Valid
    private EventoBase eventoBase;

    @NotEmpty(message = "La lista de participantes no puede estar vacía")
    @Valid
    private List<EquipoEventoRequest> participantes;
}