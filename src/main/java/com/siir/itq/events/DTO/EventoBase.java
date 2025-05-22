package com.siir.itq.events.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class EventoBase {
    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 1, max = 255, message = "El nombre debe tener entre 1 y 255 caracteres")
    private String nombre;

    @NotNull(message = "La fecha no puede ser nula")
    private OffsetDateTime fechaInicio;

    @NotNull(message = "El ID del tipo de evento no puede ser nulo")
    private UUID idTipoEvento;

    @NotNull (message = "La duración no puede ser nula")
    @Min(value = 1, message = "La duración mínima es de 1 minuto")
    @Max(value = 480, message = "La duración máxima es de 480 minutos")
    private Integer duracion; // en minutos

    @NotNull(message = "El lugar de la sede no puede ser nulo")
    @Size(min = 1, max = 255, message = "El lugar de la sede debe tener entre 1 y 255 caracteres")
    private String lugar;

    @NotNull(message = "La lista de equipos no puede ser nula")
    @Valid
    private List<ItemEquipo> equipos;

    // Getters and Setters
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
