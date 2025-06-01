package com.siir.itq.events.Repository;

import com.siir.itq.events.Entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventoRepository extends JpaRepository<Evento, UUID> {

    List<Evento> findByFechaInicioAfter(LocalDateTime fechaInicio);

    List<Evento> findByFechaInicioBefore(LocalDateTime fechaActual);
  
    List<Evento> findByFechaInicioAfterAndParticipantesIdEquipo(LocalDateTime fechaActual, UUID idEquipo);

    List<Evento> findByFechaInicioBeforeAndParticipantesIdEquipo(LocalDateTime fechaActual, UUID idEquipo);
}
