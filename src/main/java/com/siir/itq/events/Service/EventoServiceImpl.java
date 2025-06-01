package com.siir.itq.events.Service;

import com.siir.itq.events.DTO.*;
import com.siir.itq.events.Entity.Evento;
import com.siir.itq.events.Entity.ParticipanteEvento;
import com.siir.itq.events.Client.EquipoServiceClient;
import com.siir.itq.events.config.exceptions.CustomExceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

import com.siir.itq.events.Repository.EventoRepository;
import com.siir.itq.events.Repository.ParticipanteEventoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;
    private final ParticipanteEventoRepository participanteEventoRepository;
    private final EquipoServiceClient equipoServiceClient;
    
    @Transactional
    public EventoResponse crearEvento(EventoRequest eventoRequestDto) {
        Evento evento = new Evento();
        mapRequestDtoToEntity(eventoRequestDto, evento);
        
        Evento eventoGuardado = eventoRepository.save(evento);
        return mapEntityToResponseDto(eventoGuardado);
    }

    @Transactional
    public void actualizarEvento(String idEvento, EventoRequest eventoRequestDto) {
        Evento evento = eventoRepository.findById(UUID.fromString(idEvento))
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con ID: " + idEvento));
        
        // Clear old participants before mapping new ones
        if (evento.getParticipantes() != null) {
            evento.getParticipantes().clear();
        } else {
            evento.setParticipantes(new ArrayList<>());
        }
        // Important: Flush to ensure orphan removal takes effect before adding potentially conflicting new participants
        eventoRepository.flush();


        mapRequestDtoToEntity(eventoRequestDto, evento); // This will repopulate participants
        eventoRepository.save(evento);
        // Returns void for 204 No Content
    }

    @Override
    @Transactional
    public void eliminarEvento(String idEvento) {
        if (!eventoRepository.existsById(UUID.fromString(idEvento))) {
            throw new ResourceNotFoundException("Evento no encontrado con ID: " + idEvento);
        }
        eventoRepository.deleteById(UUID.fromString(idEvento));
    }

    @Transactional
    public void asignarPuntaje(String idEvento, String idEquipo, EventoFin eventoFinDto) {
        Optional<Evento> optionalEvento = eventoRepository.findById(UUID.fromString(idEvento));
        if(!optionalEvento.isPresent()){
            throw new ResourceNotFoundException("El evento con el id " + idEvento + " no pudo ser encontrado");
        }
        
        ParticipanteEvento participante = participanteEventoRepository.findByEventoAndIdEquipo(optionalEvento.get(), UUID.fromString(idEquipo))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Participante (equipo local) no encontrado para el evento ID: " + idEvento + " y equipo ID: " + idEquipo));

        participante.setPuntuacion(eventoFinDto.getPuntuacion());
        participanteEventoRepository.save(participante);
    }

    @Transactional(readOnly = true)
    public List<EventoResponse> getEventos(boolean futuros, String idEquipo) {
        LocalDateTime fechaActual = LocalDateTime.now();
        List<Evento> eventos;

        if (idEquipo != null) {
            if (futuros) {
                eventos = eventoRepository.findByFechaInicioAfterAndParticipantesIdEquipo(fechaActual, UUID.fromString(idEquipo));
            } else {
                eventos = eventoRepository.findByFechaInicioBeforeAndParticipantesIdEquipo(fechaActual, UUID.fromString(idEquipo));
            }
        } else {
            if (futuros) {
                eventos = eventoRepository.findByFechaInicioAfter(fechaActual);
            } else {
                eventos = eventoRepository.findByFechaInicioBefore(fechaActual);
            }
        }
        
        return eventos.stream()
            .map(this::mapEntityToResponseDto)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<EquipoEventoResponse> getParticipantesByEvento(String idEvento) {

        Optional<Evento> optionalEvento = eventoRepository.findById(UUID.fromString(idEvento));
        if(!optionalEvento.isPresent()){
            throw new ResourceNotFoundException("El evento con el id " + idEvento + " no pudo ser encontrado");
        }

        List<ParticipanteEvento> participantes = participanteEventoRepository.findByEvento(optionalEvento.get());
        return participantes.stream().map((ParticipanteEvento p) -> {
            if (p.getIdEquipo() != null) {
                String nombreEquipoLocal = equipoServiceClient.getNombreEquipo(p.getIdEquipo().toString());
                return EquipoEventoResponse.forLocalTeam(nombreEquipoLocal);
            } else {
                return EquipoEventoResponse.forForeignTeam(p.getEquipoVisitanteNombre());
            }
        })
        .collect(Collectors.toList());
    }


    @Transactional
    public EventoResponse crearEvento(EventoBase eventoBase) {
        EventoRequest eventoRequest = new EventoRequest();
        eventoRequest.setEventoBase(eventoBase);
        return crearEvento(eventoRequest);
    }

    @Transactional
    public void actualizarEvento(String idEvento, EventoBase eventoBase) {
        EventoRequest eventoRequest = new EventoRequest();
        eventoRequest.setEventoBase(eventoBase);
        actualizarEvento(idEvento, eventoRequest);
    }

    // --- Helper Mapper Methods ---
    private void mapRequestDtoToEntity(EventoRequest dto, Evento entity) {
        // Map from EventoBase inside EventoRequest
        EventoBase baseDto = dto.getEventoBase();
        entity.setNombre(baseDto.getNombre());
        entity.setFechaInicio(baseDto.getFechaInicio());
        entity.setTipoEvento(baseDto.getTipoEvento());
        entity.setDuracion(baseDto.getDuracion());
        entity.setLugar(baseDto.getLugar());

        if (entity.getParticipantes() == null) {
            entity.setParticipantes(new ArrayList<>());
        }

        if (dto.getParticipantes() != null) {
            for (EquipoEventoRequest participanteDto : dto.getParticipantes()) {
                ParticipanteEvento participanteEntity = new ParticipanteEvento();
                if (participanteDto.getIdEquipoLocal() != null) {
                    participanteEntity.setIdEquipo(participanteDto.getIdEquipoLocal());
                } else {
                    participanteEntity.setEquipoVisitanteNombre(participanteDto.getNombreEquipoForaneo());
                }
                // Puntuacion is handled separately
                entity.addParticipante(participanteEntity);
            }
        }
    }
    
    private EventoResponse mapEntityToResponseDto(Evento entity) {
        List<EquipoEventoResponse> EquipoEventoResponseItems = new ArrayList<>();
        if (entity.getParticipantes() != null) {
            for (ParticipanteEvento pEntity : entity.getParticipantes()) {
                if (pEntity.getIdEquipo() != null) {
                    String nombreEquipoLocal = equipoServiceClient.getNombreEquipo(pEntity.getIdEquipo().toString());
                    EquipoEventoResponseItems.add(EquipoEventoResponse.forLocalTeam(nombreEquipoLocal));
                } else {
                    EquipoEventoResponseItems.add(EquipoEventoResponse.forForeignTeam(pEntity.getEquipoVisitanteNombre()));
                }
            }
        }

        return new EventoResponse(
            entity.getId(),
            entity.getNombre(),
            entity.getFechaInicio(),
            entity.getTipoEvento(),
            entity.getDuracion(),
            entity.getLugar(),
            EquipoEventoResponseItems
        );
    }
}