package com.siir.itq.events.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventoFin {
    @NotNull(message = "La puntuación no puede ser nula")
    private double puntuacion;
}
