package com.example.operacionesmteriasprimas.Modelos;

import java.io.Serializable;
import java.util.HashMap;

public class Operador implements Serializable {
    HashMap<Integer,Double> actividades;
    String nombre;
    boolean completo;

    public Operador(HashMap<Integer, Double> actividades, String nombre) {
        this.actividades = actividades;
        this.nombre = nombre;
    }

    public Operador(HashMap<Integer, Double> actividades, String nombre, boolean completo) {
        this.actividades = actividades;
        this.nombre = nombre;
        this.completo = completo;
    }

    public boolean isCompleto() {
        return completo;
    }

    public void setCompleto(boolean completo) {
        this.completo = completo;
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
