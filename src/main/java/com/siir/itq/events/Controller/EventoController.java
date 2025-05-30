package com.siir.itq.events.Controller;

import com.siir.itq.events.DTO.EventoBase;
import com.siir.itq.events.DTO.EventoFin;
import com.siir.itq.events.DTO.EventoResponse;
import com.siir.itq.events.Service.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/eventos")
@RequiredArgsConstructor
public class EventoController {
    
    private final EventoService eventoService;

    @PostMapping
    public ResponseEntity<EventoResponse> crearEvento(@Valid @RequestBody EventoBase eventoBaseDto) {
        EventoResponse eventoCreado = eventoService.crearEvento(eventoBaseDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(eventoCreado.getId())
                .toUri();
        return ResponseEntity.created(location).body(eventoCreado);
    }

    @PutMapping("/{idEvento}")
    public ResponseEntity<EventoResponse> actualizarEvento(
            @PathVariable UUID idEvento,
            @Valid @RequestBody EventoBase eventoBaseDto) {
        EventoResponse eventoActualizado = eventoService.actualizarEvento(idEvento, eventoBaseDto);
        return ResponseEntity.ok(eventoActualizado);
    }

    @DeleteMapping("/{idEvento}")
    public ResponseEntity<Void> eliminarEvento(@PathVariable UUID idEvento) {
        eventoService.eliminarEvento(idEvento);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PutMapping("/{idEvento}/equipos/{idEquipo}")
    public ResponseEntity<EventoFin> asignarPuntaje(
            @PathVariable UUID idEvento,
            @PathVariable UUID idEquipo, // This is id_equipo_local
            @Valid @RequestBody EventoFin eventoFinDto) {
        EventoFin resultado = eventoService.asignarPuntaje(idEvento, idEquipo, eventoFinDto);
        return ResponseEntity.ok(resultado);
    }
}
