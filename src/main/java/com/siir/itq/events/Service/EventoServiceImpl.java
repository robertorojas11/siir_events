package com.siir.itq.events.Service;

import com.siir.itq.events.DTO.*;
import com.siir.itq.events.Entity.Evento;
import com.siir.itq.events.Entity.ParticipanteEvento;
import com.siir.itq.events.Client.EquipoServiceClient;
import com.siir.itq.events.config.exceptions.CustomExceptions.ResourceNotFoundException;
import com.siir.itq.events.Repository.EventoRepository;
import com.siir.itq.events.Repository.ParticipanteEventoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;
    private final ParticipanteEventoRepository participanteEventoRepository;
    private final EquipoServiceClient equipoServiceClient;

    @Autowired
    public EventoServiceImpl(EventoRepository eventoRepository,
                             ParticipanteEventoRepository participanteEventoRepository,
                             EquipoServiceClient equipoServiceClient) {
        this.eventoRepository = eventoRepository;
        this.participanteEventoRepository = participanteEventoRepository;
        this.equipoServiceClient = equipoServiceClient;
    }
    
    @Transactional
    public EventoResponse crearEvento(EventoRequest eventoRequestDto) {
        Evento evento = new Evento();
        mapRequestDtoToEntity(eventoRequestDto, evento);
        
        Evento eventoGuardado = eventoRepository.save(evento);
        return mapEntityToResponseDto(eventoGuardado);
    }

    @Transactional
    public void actualizarEvento(UUID idEvento, EventoRequest eventoRequestDto) {
        Evento evento = eventoRepository.findById(idEvento)
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
    public void eliminarEvento(UUID idEvento) {
        if (!eventoRepository.existsById(idEvento)) {
            throw new ResourceNotFoundException("Evento no encontrado con ID: " + idEvento);
        }
        eventoRepository.deleteById(idEvento);
    }

    @Transactional
    public void asignarPuntaje(UUID idEvento, UUID idEquipo, EventoFin eventoFinDto) {
        ParticipanteEvento participante = participanteEventoRepository.findByEventoAndIdEquipo(idEvento, idEquipo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Participante (equipo local) no encontrado para el evento ID: " + idEvento + " y equipo ID: " + idEquipo));

        participante.setPuntuacion(eventoFinDto.getPuntuacion());
        participanteEventoRepository.save(participante);
    }

    @Transactional(readOnly = true)
    public ListaEventos getEventos(boolean futuros, UUID idEquipo) {
        OffsetDateTime fechaActual = OffsetDateTime.now(ZoneOffset.UTC);
        List<Evento> eventos;

        if (idEquipo != null) {
            if (futuros) {
                eventos = eventoRepository.findEventosFuturosByEquipoLocal(fechaActual, idEquipo);
            } else {
                eventos = eventoRepository.findEventosPasadosByEquipoLocal(fechaActual, idEquipo);
            }
        } else {
            if (futuros) {
                eventos = eventoRepository.findEventosFuturos(fechaActual);
            } else {
                eventos = eventoRepository.findEventosPasados(fechaActual);
            }
        }
        
        List<EventoResponse> eventoResponses = eventos.stream()
                .map(this::mapEntityToResponseDto)
                .collect(Collectors.toList());
        return new ListaEventos(eventoResponses);
    }
    
    @Transactional(readOnly = true)
    public List<EquipoEventoResponse> getParticipantesByEvento(UUID idEvento, OffsetDateTime fechaInicio) {

        if (!eventoRepository.existsById(idEvento)) {
            throw new ResourceNotFoundException("Evento no encontrado con ID: " + idEvento);
        }

        List<ParticipanteEvento> participantes = participanteEventoRepository.findByEventoId(idEvento);
        return participantes.stream().map((ParticipanteEvento p) -> {
            if (p.getIdEquipo() != null) {
                String nombreEquipoLocal = equipoServiceClient.getNombreEquipo(p.getIdEquipo());
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
        // You may need to set participantes if required
        return crearEvento(eventoRequest);
    }

    @Transactional
    public void actualizarEvento(UUID idEvento, EventoBase eventoBase) {
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
                    String nombreEquipoLocal = equipoServiceClient.getNombreEquipo(pEntity.getIdEquipo());
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