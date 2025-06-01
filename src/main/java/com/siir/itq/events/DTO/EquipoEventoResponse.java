package com.siir.itq.events.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Ensure only one of the names is serialized
public class EquipoEventoResponse {

    private String equipoLocalNombre;
    private String equipoForaneoNombre;

    // Constructor for local team
    public EquipoEventoResponse(String equipoLocalNombre) {
        this.equipoLocalNombre = equipoLocalNombre;
        this.equipoForaneoNombre = null;
    }

    public static EquipoEventoResponse forForeignTeam(String equipoForaneoNombre) {
        EquipoEventoResponse item = new EquipoEventoResponse();
        item.setEquipoForaneoNombre(equipoForaneoNombre);
        return item;
    }
    
    public static EquipoEventoResponse forLocalTeam(String equipoLocalNombre) {
        EquipoEventoResponse item = new EquipoEventoResponse();
        item.setEquipoLocalNombre(equipoLocalNombre);
        return item;
    }
}
