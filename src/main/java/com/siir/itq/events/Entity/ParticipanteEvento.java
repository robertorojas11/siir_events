package com.siir.itq.events.Entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
    @Table(name = "participantes_evento", uniqueConstraints = {
            @UniqueConstraint(columnNames = {"evento_id", "equipo_local_id"}),
            @UniqueConstraint(columnNames = {"evento_id", "equipo_visitante_nombre"})
    })
public class ParticipanteEvento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(name = "equipo_local_id", nullable = true)
    private UUID equipoLocalId;

    @Column(name = "equipo_externo", nullable = true, length = 255)
    private String equipoVisitanteNombre;

    @Column(nullable = true)
    private Double puntuacion;

    // Getters and Setters
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public Evento getEvento() {
        return evento;
    }
    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public UUID getEquipoLocalId() {
        return equipoLocalId;
    }
    public void setEquipoLocalId(UUID equipoLocalId) {
        this.equipoLocalId = equipoLocalId;
    }

    public String getEquipoVisitanteNombre() {
        return equipoVisitanteNombre;
    }
    public void setEquipoVisitanteNombre(String equipoVisitanteNombre) {
        this.equipoVisitanteNombre = equipoVisitanteNombre;
    }

    public Double getPuntuacion() {
        return puntuacion;
    }
    public void setPuntuacion(Double puntuacion) {
        this.puntuacion = puntuacion;
    }

    @PrePersist
    @PreUpdate
    public void checkEquipoConstraint() {
        if (!((equipoLocalId != null && equipoVisitanteNombre == null) || (equipoLocalId == null && equipoVisitanteNombre != null))) {
            throw new IllegalStateException("Un participante debe tener 'equipoIdLocal' o 'equipoForaneoNombre', pero no ambos ni ninguno.");
        }
    }
}
