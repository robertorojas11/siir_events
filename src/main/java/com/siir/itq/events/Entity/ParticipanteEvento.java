package com.siir.itq.events.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
    @Table(name = "participacion_evento", uniqueConstraints = {
            @UniqueConstraint(columnNames = {"evento_id", "equipo_local_id"}),
            @UniqueConstraint(columnNames = {"evento_id", "equipo_visitante_nombre"})
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipanteEvento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_participacion;

    @ManyToOne
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @Column(name = "id_equipo", nullable = true)
    private UUID idEquipo;

    @Column(name = "equipo_externo", nullable = true, length = 255)
    private String equipoVisitanteNombre;

    @Column(nullable = true)
    private Double puntuacion;

    @PrePersist
    @PreUpdate
    public void checkEquipoConstraint() {
        if (!((idEquipo != null && equipoVisitanteNombre == null) || (idEquipo == null && equipoVisitanteNombre != null))) {
            throw new IllegalStateException("Un participante debe tener 'equipoIdLocal' o 'equipoForaneoNombre', pero no ambos ni ninguno.");
        }
    }
}
