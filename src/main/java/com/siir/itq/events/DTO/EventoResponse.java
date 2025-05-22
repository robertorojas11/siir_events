package com.siir.itq.events.DTO;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.List;

public class EventoResponse {
    private UUID id;
    private String nombre;
    private OffsetDateTime fechaInicio;
    private UUID idTipoEvento;
    private Integer duracion; // en minutos
    private String lugar;
    private List<ItemEquipo> equipos;

    public EventoResponse() {}

    public EventoResponse(UUID id, String nombre, OffsetDateTime fechaInicio, UUID idTipoEvento, Integer duracion, String lugar, List<ItemEquipo> equipos) {
        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.idTipoEvento = idTipoEvento;
        this.duracion = duracion;
        this.lugar = lugar;
        this.equipos = equipos;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public OffsetDateTime getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(OffsetDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public UUID getIdTipoEvento() {
        return idTipoEvento;
    }
    public void setIdTipoEvento(UUID idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    public Integer getDuracion() {
        return duracion;
    }
    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getLugar() {
        return lugar;
    }
    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public List<ItemEquipo> getEquipos() {
        return equipos;
    }
    public void setEquipos(List<ItemEquipo> equipos) {
        this.equipos = equipos;
    }

    
}
