package com.example.operacionesmteriasprimas.Modelos;

public class sumas {
    String actividad;
    Double horas;

    public sumas(String actividad, Double horas) {
        this.actividad = actividad;
        this.horas = horas;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public Double getHoras() {
        return horas;
    }

    public void setHoras(Double horas) {
        this.horas = horas;
    }
}
