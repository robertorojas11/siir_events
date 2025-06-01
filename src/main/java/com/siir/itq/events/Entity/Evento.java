package com.siir.itq.events.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.OffsetDateTime;

import com.siir.itq.events.DTO.TipoEvento;

@Entity
@Table(name = "eventos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String nombre;

    @Column(nullable = false)
    private OffsetDateTime fechaInicio;

    @Enumerated(EnumType.STRING) // Store enum as string
    @Column(nullable = false)
    private TipoEvento tipoEvento; // Changed from tipoEventoId (UUID)

    @Column(nullable = false)
    private Integer duracion; // in minutes

    @Column(nullable = false, length = 255)
    private String lugar;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ParticipanteEvento> participantes = new ArrayList<>();

    public void addParticipante(ParticipanteEvento participante) {
        if (this.participantes == null) {
            this.participantes = new ArrayList<>();
        }
        this.participantes.add(participante);
        participante.setEvento(this);
    }

    public void removeParticipante(ParticipanteEvento participante) {
        if (this.participantes != null) {
            this.participantes.remove(participante);
            participante.setEvento(null);
        }
    }
}
