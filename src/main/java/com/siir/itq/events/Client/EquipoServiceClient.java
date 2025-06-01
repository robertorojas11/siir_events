package com.siir.itq.events.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "teamsServer", url = "${siir.teams.url}/api")
public interface EquipoServiceClient {

    @GetMapping("equipos/{idEquipo}")
    public String getNombreEquipo(@PathVariable String idEquipo);

}
