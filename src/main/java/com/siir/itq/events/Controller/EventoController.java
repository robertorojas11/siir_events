package com.siir.itq.events.Controller;

import com.siir.itq.events.DTO.*;
import com.siir.itq.events.Service.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

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
    public ResponseEntity<?> getEventos(
            @RequestParam(required = true) boolean futuros,
            @RequestParam(required = false) String idEquipo) {
        
        return ResponseEntity.ok(eventoService.getEventos(futuros, idEquipo));
    }

    @PutMapping("/{idEvento}")
    public ResponseEntity<Void> actualizarEvento(
            @PathVariable String idEvento,
            @Valid @RequestBody EventoRequest eventoRequestDto) {
        eventoService.actualizarEvento(idEvento, eventoRequestDto);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @DeleteMapping("/{idEvento}")
    public ResponseEntity<Void> eliminarEvento(@PathVariable String idEvento) {
        eventoService.eliminarEvento(idEvento);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
    
    @GetMapping("/{idEvento}/participantes")
    public ResponseEntity<List<EquipoEventoResponse>> getParticipantesByEvento(
            @PathVariable String idEvento) {
        // The OAS specifies ParticipantesEventoResponse as the schema, which is an array of the items.
        // So, List<ParticipanteResponseItem> is appropriate.
        List<EquipoEventoResponse> participantes = eventoService.getParticipantesByEvento(idEvento);
        return ResponseEntity.ok(participantes);
    }

    @PutMapping("/{idEvento}/equipos/{idEquipo}")
    public ResponseEntity<Void> asignarPuntajeEvento( // Renamed from asignarPuntaje to avoid conflict if EventoFin was used elsewhere
            @PathVariable String idEvento,
            @PathVariable String idEquipo, // This is id_equipo_local
            @Valid @RequestBody EventoFin eventoFinDto) {
        eventoService.asignarPuntaje(idEvento, idEquipo, eventoFinDto);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
