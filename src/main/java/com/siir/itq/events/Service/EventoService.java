package com.siir.itq.events.Service;

import java.util.List;

import com.siir.itq.events.DTO.EventoFin;
import com.siir.itq.events.DTO.EventoResponse;
import com.siir.itq.events.DTO.EventoRequest;
import com.siir.itq.events.DTO.EquipoEventoResponse;

public interface EventoService {
    EventoResponse crearEvento(EventoRequest eventoRequestDto);
    void actualizarEvento(String idEvento, EventoRequest eventoRequestDto); // Returns void for 204
    void eliminarEvento(String idEvento);
    void asignarPuntaje(String idEvento, String idEquipo, EventoFin eventoFinDto); // Returns void for 204
    List<EventoResponse> getEventos(boolean futuros, String idEquipo); // idEquipo is optional
    List<EquipoEventoResponse> getParticipantesByEvento(String idEvento); // Corresponds to GET /eventos/{idEvento}/participantes
}
