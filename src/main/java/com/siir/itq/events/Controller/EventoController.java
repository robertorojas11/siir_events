package com.siir.itq.events.Controller;

import com.siir.itq.events.DTO.*;
import com.siir.itq.events.Service.EventoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService eventoService;

    @Autowired
    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping
    public ResponseEntity<EventoResponse> crearEvento(@Valid @RequestBody EventoRequest eventoRequestDto) {
        EventoResponse eventoCreado = eventoService.crearEvento(eventoRequestDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(eventoCreado.getId()) // Assuming EventoResponse has getId()
                .toUri();
        return ResponseEntity.created(location).body(eventoCreado);
    }

    @GetMapping
    public ResponseEntity<ListaEventos> getEventos(
            @RequestParam(required = true) boolean futuros,
            @RequestParam(required = false) UUID idEquipo) {
        ListaEventos listaEventos = eventoService.getEventos(futuros, idEquipo);
        return ResponseEntity.ok(listaEventos);
    }

    @PutMapping("/{idEvento}")
    public ResponseEntity<Void> actualizarEvento(
            @PathVariable UUID idEvento,
            @Valid @RequestBody EventoRequest eventoRequestDto) {
        eventoService.actualizarEvento(idEvento, eventoRequestDto);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @DeleteMapping("/{idEvento}")
    public ResponseEntity<Void> eliminarEvento(@PathVariable UUID idEvento) {
        eventoService.eliminarEvento(idEvento);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
    
    @GetMapping("/{idEvento}/participantes")
    public ResponseEntity<List<EquipoEventoResponse>> getParticipantesByEvento(
            @PathVariable UUID idEvento,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime fechaInicio) {
        // The OAS specifies ParticipantesEventoResponse as the schema, which is an array of the items.
        // So, List<ParticipanteResponseItem> is appropriate.
        List<EquipoEventoResponse> participantes = eventoService.getParticipantesByEvento(idEvento, fechaInicio);
        return ResponseEntity.ok(participantes);
    }

    @PutMapping("/{idEvento}/equipos/{idEquipo}")
    public ResponseEntity<Void> asignarPuntajeEvento( // Renamed from asignarPuntaje to avoid conflict if EventoFin was used elsewhere
            @PathVariable UUID idEvento,
            @PathVariable UUID idEquipo, // This is id_equipo_local
            @Valid @RequestBody EventoFin eventoFinDto) {
        eventoService.asignarPuntaje(idEvento, idEquipo, eventoFinDto);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
