package com.siir.itq.events.Repository;

import com.siir.itq.events.Entity.ParticipanteEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface ParticipanteEventoRepository extends JpaRepository<ParticipanteEvento, UUID> {
    Optional<ParticipanteEvento> findByEventoAndIdEquipo(UUID evento, UUID idEquipo);
    List<ParticipanteEvento> findByEventoId(UUID eventoId);
}
