package com.siir.itq.events.Service;

import com.siir.itq.events.DTO.EventoBase;
import com.siir.itq.events.DTO.EventoFin;
import com.siir.itq.events.DTO.EventoResponse;
import com.siir.itq.events.DTO.ItemEquipo;
import com.siir.itq.events.DTO.EquipoEvento;
import com.siir.itq.events.Entity.Evento;
import com.siir.itq.events.Entity.ParticipanteEvento;
import com.siir.itq.events.Exception.ResourceNotFoundException;
import com.siir.itq.events.Repository.EventoRepository;
import com.siir.itq.events.Repository.ParticipanteEventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventoServiceImpl implements EventoService{
    private final EventoRepository eventoRepository;
    private final ParticipanteEventoRepository participanteEventoRepository;

    @Autowired
    public EventoServiceImpl(EventoRepository eventoRepository, ParticipanteEventoRepository participanteEventoRepository) {
        this.eventoRepository = eventoRepository;
        this.participanteEventoRepository = participanteEventoRepository;
    }

    @Override
    @Transactional
    public EventoResponse crearEvento(EventoBase eventoBaseDto) {
        Evento evento = new Evento();
        mapBaseDtoToEntity(eventoBaseDto, evento); // Maps basic fields

        // Clear existing participants if any (should not happen for new event) and add new ones
        evento.getParticipantes().clear(); 
        if (eventoBaseDto.getEquipos() != null) {
            for (ItemEquipo itemEquipoDto : eventoBaseDto.getEquipos()) {
                ParticipanteEvento participante = new ParticipanteEvento();
                mapEquipoDtoToParticipante(itemEquipoDto.getEquipoEvento(), participante);
                evento.addParticipante(participante);
            }
        }
        
        Evento eventoGuardado = eventoRepository.save(evento);
        return mapEntityToResponseDto(eventoGuardado);
    }

    @Override
    @Transactional
    public EventoResponse actualizarEvento(UUID idEvento, EventoBase eventoBaseDto) {
        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con ID: " + idEvento));

        mapBaseDtoToEntity(eventoBaseDto, evento);
        
        evento.getParticipantes().clear();
        
        List<ParticipanteEvento> nuevosParticipantes = new ArrayList<>();
        if (eventoBaseDto.getEquipos() != null) {
            for (ItemEquipo itemEquipoDto : eventoBaseDto.getEquipos()) {
                ParticipanteEvento participante = new ParticipanteEvento();
                mapEquipoDtoToParticipante(itemEquipoDto.getEquipoEvento(), participante);
                nuevosParticipantes.add(participante);
            }
        }

        // Clear and add all new participants
        evento.getParticipantes().clear(); // This will trigger orphan removal for old participants
        for(ParticipanteEvento p : nuevosParticipantes) {
            evento.addParticipante(p); // This sets the evento reference and adds to collection
        }
        Evento eventoActualizado = eventoRepository.save(evento);
        return mapEntityToResponseDto(eventoActualizado);
    }

    @Override
    @Transactional
    public void eliminarEvento(UUID idEvento) {
        if (!eventoRepository.existsById(idEvento)) {
            throw new ResourceNotFoundException("Evento no encontrado con ID: " + idEvento);
        }
        eventoRepository.deleteById(idEvento);
    }

    @Override
    @Transactional
    public EventoFin asignarPuntaje(UUID idEvento, UUID idEquipo, EventoFin eventoFinDto) {
        // idEquipo here refers to idEquipoLocal of a ParticipanteEvento
        ParticipanteEvento participante = participanteEventoRepository.findByEventoIdAndEquipoIdLocal(idEvento, idEquipo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Participante no encontrado para el evento ID: " + idEvento + " y equipo ID: " + idEquipo));

        participante.setPuntuacion(eventoFinDto.getPuntuacion());
        participanteEventoRepository.save(participante);

        // The spec asks to return EventoFin DTO
        EventoFin responseDto = new EventoFin();
        responseDto.setPuntuacion(participante.getPuntuacion());
        return responseDto;
    }

    // --- Helper Mapper Methods ---
    private void mapBaseDtoToEntity(EventoBase dto, Evento entity) {
        entity.setNombre(dto.getNombre());
        entity.setFechaInicio(dto.getFechaInicio());
        entity.setTipoEventoId(dto.getIdTipoEvento());
        entity.setDuracion(dto.getDuracion());
        entity.setLugar(dto.getLugar());
    }
    
    private void mapEquipoDtoToParticipante(EquipoEvento equipoDto, ParticipanteEvento participante) {
        if (equipoDto.getIdEquipoLocal() != null) {
            participante.setEquipoLocalId(equipoDto.getIdEquipoLocal());
            participante.setEquipoVisitanteNombre(null);
        } else if (equipoDto.getNombreEquipoForaneo() != null) {
            participante.setEquipoVisitanteNombre(equipoDto.getNombreEquipoForaneo());
            participante.setEquipoLocalId(null);
        }
        // Puntuacion is initially null or handled by asignarPuntaje
    }

    private EventoResponse mapEntityToResponseDto(Evento entity) {
        EventoResponse dto = new EventoResponse();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setFechaInicio(entity.getFechaInicio());
        dto.setIdTipoEvento(entity.getTipoEventoId());
        dto.setDuracion(entity.getDuracion());
        dto.setLugar(entity.getLugar());
        
        List<ItemEquipo> itemEquipos = entity.getParticipantes().stream()
            .map(participante -> {
                EquipoEvento equipoEvento = new EquipoEvento();
                if (participante.getEquipoLocalId() != null) {
                    equipoEvento.setIdEquipoLocal(participante.getEquipoLocalId());
                } else {
                    equipoEvento.setNombreEquipoForaneo(participante.getEquipoVisitanteNombre());
                }
                ItemEquipo itemEquipo = new ItemEquipo();
                itemEquipo.setEquipoEvento(equipoEvento);
                return itemEquipo;
            })
            .collect(Collectors.toList());
        dto.setEquipos(itemEquipos);
        return dto;
    }
}
