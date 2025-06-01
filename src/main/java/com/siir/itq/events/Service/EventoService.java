package com.siir.itq.events.Service;

import java.util.UUID;
import java.time.OffsetDateTime;
import java.util.List;

import com.siir.itq.events.DTO.EventoBase;
import com.siir.itq.events.DTO.EventoFin;
import com.siir.itq.events.DTO.EventoResponse;
import com.siir.itq.events.DTO.EventoRequest;
import com.siir.itq.events.DTO.ListaEventos;
import com.siir.itq.events.DTO.EquipoEventoResponse;

public interface EventoService {
    EventoResponse crearEvento(EventoRequest eventoRequestDto);
    void actualizarEvento(UUID idEvento, EventoRequest eventoRequestDto); // Returns void for 204
    void eliminarEvento(UUID idEvento);
    void asignarPuntaje(UUID idEvento, UUID idEquipo, EventoFin eventoFinDto); // Returns void for 204
    ListaEventos getEventos(boolean futuros, UUID idEquipo); // idEquipo is optional
    List<EquipoEventoResponse> getParticipantesByEvento(UUID idEvento, OffsetDateTime fechaInicio); // Corresponds to GET /eventos/{idEvento}/participantes
}
