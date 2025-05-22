package com.siir.itq.events.Repository;

import com.siir.itq.events.Entity.ParticipanteEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParticipanteEventoRepository extends JpaRepository<ParticipanteEvento, UUID> {
    // Find by event ID and local team ID (for assigning score)
    Optional<ParticipanteEvento> findByEventoIdAndEquipoIdLocal(UUID eventoId, UUID equipoIdLocal);
}
