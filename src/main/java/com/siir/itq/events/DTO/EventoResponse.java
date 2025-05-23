package com.siir.itq.events.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class EventoResponse {
    private UUID id;
    private String nombre;
    private OffsetDateTime fechaInicio;
    private UUID idTipoEvento;
    private Integer duracion; // en minutos
    private String lugar;
    private List<ItemEquipo> equipos;
    
    // Constructor para crear un EventoResponse a partir de un EventoBase
    public EventoResponse(UUID id, String nombre, OffsetDateTime fechaInicio, UUID tipoEvento, Integer duracion, String lugar, List<ItemEquipo> equipos) {
        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.idTipoEvento = tipoEvento;
        this.duracion = duracion;
        this.lugar = lugar;
        this.equipos = equipos;
    }
}
