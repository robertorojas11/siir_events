package com.siir.itq.events.Repository;

import com.siir.itq.events.Entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventoRepository extends JpaRepository<Evento, UUID> {

    // Find future events
    @Query("SELECT e FROM Evento e WHERE e.fechaInicio > :fechaActual")
    List<Evento> findEventosFuturos(@Param("fechaActual") OffsetDateTime fechaActual);

    // Find past events
    @Query("SELECT e FROM Evento e WHERE e.fechaInicio <= :fechaActual")
    List<Evento> findEventosPasados(@Param("fechaActual") OffsetDateTime fechaActual);

    // Find future events for a specific team (local)
    @Query("SELECT e FROM Evento e JOIN e.participantes p WHERE e.fechaInicio > :fechaActual AND p.equipoIdLocal = :idEquipo")
    List<Evento> findEventosFuturosByEquipoLocal(@Param("fechaActual") OffsetDateTime fechaActual, @Param("idEquipo") UUID idEquipo);

    // Find past events for a specific team (local)
    @Query("SELECT e FROM Evento e JOIN e.participantes p WHERE e.fechaInicio <= :fechaActual AND p.equipoIdLocal = :idEquipo")
    List<Evento> findEventosPasadosByEquipoLocal(@Param("fechaActual") OffsetDateTime fechaActual, @Param("idEquipo") UUID idEquipo);
    
    // Find future events for a specific team (foreign)
    @Query("SELECT e FROM Evento e JOIN e.participantes p WHERE e.fechaInicio > :fechaActual AND p.equipoForaneoNombre = :nombreEquipo")
    List<Evento> findEventosFuturosByEquipoForaneo(@Param("fechaActual") OffsetDateTime fechaActual, @Param("nombreEquipo") String nombreEquipo);

    // Find past events for a specific team (foreign)
    @Query("SELECT e FROM Evento e JOIN e.participantes p WHERE e.fechaInicio <= :fechaActual AND p.equipoForaneoNombre = :nombreEquipo")
    List<Evento> findEventosPasadosByEquipoForaneo(@Param("fechaActual") OffsetDateTime fechaActual, @Param("nombreEquipo") String nombreEquipo);
}
