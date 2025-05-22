package com.siir.itq.events.Service;

import java.util.UUID;

import com.siir.itq.events.DTO.EventoBase;
import com.siir.itq.events.DTO.EventoFin;
import com.siir.itq.events.DTO.EventoResponse;

public interface EventoService {
    EventoResponse crearEvento (EventoBase eventoBaseDto);
    EventoResponse actualizarEvento (UUID idEventom , EventoBase eventoBaseDto);
    void eliminarEvento (UUID idEvento);
    EventoFin asignarPuntaje(UUID idEvento, UUID idEquipo, EventoFin eventoFinDto);
}
