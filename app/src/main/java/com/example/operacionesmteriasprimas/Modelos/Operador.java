package com.example.operacionesmteriasprimas.Modelos;

import com.example.operacionesmteriasprimas.ui.reporte.ListaOperadores;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Operador implements Serializable {
    List<Double> actividades;
    String nombre;
    boolean completo;
    List<String> nombreActividades;

    public Operador(List<Double> actividades, String nombre, boolean completo, List<String> nombreActividades) {
        this.actividades = actividades;
        this.nombre = nombre;
        this.completo = completo;
        this.nombreActividades = nombreActividades;
    }

    @Override
    public String toString() {
        return "Operador{" +
                "actividades=" + actividades +
                ", nombre='" + nombre + '\'' +
                ", completo=" + completo +
                ", nombreActividades=" + nombreActividades +
                '}';
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

    public List<Double> getActividades() {
        return actividades;
    }

    public void setActividades(List<Double> actividades) {
        this.actividades = actividades;
    }

    public int getActividadesPendientes(){
        int cont=0;
        for(double d:actividades){
            if(d>0.0){
                cont++;
            }
        }

        int restantes=nombreActividades.size()-cont;
        return restantes;
    }
}
