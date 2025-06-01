package com.siir.itq.events.Client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;

@Service
public class EquipoServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(EquipoServiceClient.class);
    private final RestTemplate restTemplate;

    @Value("${equipos.service.url}") // Configure this in application.properties, e.g., http://localhost:8091
    private String equipoServiceBaseUrl;

    public EquipoServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getNombreEquipo(UUID idEquipo) {
        if (idEquipo == null) {
            return "Equipo Local Desconocido"; // Or throw an error
        }
        String url = equipoServiceBaseUrl + "/equipos/" + idEquipo.toString() + "/nombre";
        try {
            String nombre = restTemplate.getForObject(url, String.class);
            return nombre != null ? nombre : "Nombre no disponible";
        } catch (HttpClientErrorException.NotFound ex) {
            logger.warn("Equipo no encontrado en servicio externo con ID {}: {}", idEquipo, ex.getMessage());
            return "Equipo Local No Encontrado"; // Or a more specific default
        } catch (Exception e) {
            logger.error("Error al llamar al servicio de equipos para ID {}: {}", idEquipo, e.getMessage());
            return "Error al obtener nombre"; // Fallback name
        }
    }
}
