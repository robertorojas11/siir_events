package com.siir.itq.events.Entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "eventos")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String nombre;

    @Column(nullable = false)
    private OffsetDateTime fechaInicio;

    @Column(nullable = false)
    private UUID tipoEventoId; // Assuming this links to another table or is just an ID

    @Column(nullable = false)
    private Integer duracion; // in minutes

    @Column(nullable = false, length = 255)
    private String lugar;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ParticipanteEvento> participantes = new ArrayList<>();

    // Getters and Setters
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

    public UUID getTipoEventoId() {
        return tipoEventoId;
    }

    public void setTipoEventoId(UUID tipoEventoId) {
        this.tipoEventoId = tipoEventoId;
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

    public List<ParticipanteEvento> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<ParticipanteEvento> participantes) {
        this.participantes = participantes;
    }

    public void addParticipante(ParticipanteEvento participante) {
        participantes.add(participante);
        participante.setEvento(this);
    }

    public void removeParticipante(ParticipanteEvento participante) {
        participantes.remove(participante);
        participante.setEvento(null);
    }
}
