package com.siir.itq.events.DTO;

import jakarta.validation.constraints.NotNull;

public class EventoFin {
    @NotNull(message = "La puntuación no puede ser nula")
    private double puntuacion;

    // Getters and Setters
    public double getPuntuacion() {
        return puntuacion;
    }
    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }
}
