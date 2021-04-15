package com.example.operacionesmteriasprimas.Modelos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Operador implements Serializable {
    HashMap<Integer,Double> actividades;
    String nombre;
    boolean completo;
    List<String> nombreActividades;

    public Operador(HashMap<Integer, Double> actividades, String nombre, boolean completo, List<String> nombreActividades) {
        this.actividades = actividades;
        this.nombre = nombre;
        this.completo = completo;
        this.nombreActividades = nombreActividades;
    }

    public List<String> getNombreActividades() {
        return nombreActividades;
    }

    public void setNombreActividades(List<String> nombreActividades) {
        this.nombreActividades = nombreActividades;
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
