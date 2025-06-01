package com.siir.itq.events.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventoBase {
    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 1, max = 255, message = "El nombre debe tener entre 1 y 255 caracteres")
    private String nombre;

    @NotNull(message = "La fecha no puede ser nula")
    private OffsetDateTime fechaInicio;

    @NotNull(message = "El ID del tipo de evento no puede ser nulo")
    private TipoEvento tipoEvento;

    @NotNull (message = "La duración no puede ser nula")
    @Min(value = 1, message = "La duración mínima es de 1 minuto")
    @Max(value = 480, message = "La duración máxima es de 480 minutos")
    private Integer duracion; // en minutos

    @NotNull(message = "El lugar de la sede no puede ser nulo")
    @Size(min = 1, max = 255, message = "El lugar de la sede debe tener entre 1 y 255 caracteres")
    private String lugar;
}