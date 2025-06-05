package com.siir.itq.events.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoResponse {
    private String idEvento;
    private String nombre;
    private LocalDateTime fechaInicio;
    private TipoEvento tipoEvento;
    private Integer duracion;
    private String lugar;
    private List<EquipoEventoResponse> participantes;
}
