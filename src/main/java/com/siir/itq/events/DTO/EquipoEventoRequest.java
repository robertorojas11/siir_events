package com.siir.itq.events.DTO;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipoEventoRequest {

    private UUID idEquipoLocal;

    @Size(min = 1, max = 255)
    private String nombreEquipoForaneo;

    @AssertTrue(message = "Debe especificarse 'idEquipoLocal' o 'nombreEquipoForaneo', pero no ambos ni ninguno.")
    public boolean isOneOfConstraintValid() {
        return idEquipoLocal!=null ^ nombreEquipoForaneo!=null;
    }
}
