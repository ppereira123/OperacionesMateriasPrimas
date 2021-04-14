package com.example.operacionesmteriasprimas.Modelos;

import java.util.HashMap;

public class Operador {
    HashMap<Integer,Double> actividades;

    public Operador(HashMap<Integer, Double> actividades) {
        this.actividades = actividades;
    }

    public Operador() {
    }

    public HashMap<Integer, Double> getActividades() {
        return actividades;
    }

    public void setActividades(HashMap<Integer, Double> actividades) {
        this.actividades = actividades;
    }
}
