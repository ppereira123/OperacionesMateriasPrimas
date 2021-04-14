package com.example.operacionesmteriasprimas.Modelos;

import java.util.HashMap;

public class Operador {
    HashMap<Integer,Double> actividades;
    String nombre;

    public Operador(HashMap<Integer, Double> actividades, String nombre) {
        this.actividades = actividades;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
