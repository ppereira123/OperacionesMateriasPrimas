package com.example.operacionesmteriasprimas.Modelos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Reporte implements Serializable {
    String fecha;
    String supervisor;
    String id;
    String turno;
    boolean completado;
    boolean subido;
    HashMap<String,Operador> operadores;

    public Reporte(String fecha, String supervisor, String id, String turno, boolean completado, boolean subido, HashMap<String, Operador> operadores) {
        this.fecha = fecha;
        this.supervisor = supervisor;
        this.id = id;
        this.turno = turno;
        this.completado = completado;
        this.subido = subido;
        this.operadores = operadores;
    }


    @Override
    public String toString() {
        return "Reporte{" +
                "fecha='" + fecha + '\'' +
                ", supervisor='" + supervisor + '\'' +
                ", id='" + id + '\'' +
                ", turno='" + turno + '\'' +
                ", completado=" + completado +
                ", subido=" + subido +
                ", operadores=" + operadores +
                '}';
    }

    public int getRestantes(){
        int contCompleto=0;
        for (Map.Entry<String,Operador> entry:operadores.entrySet()){
            if(entry.getValue().isCompleto()){
                contCompleto++;
            }
        }
        int restantes=operadores.size()-contCompleto;
        return restantes;
    }

    public boolean isSubido() {
        return subido;
    }

    public void setSubido(boolean subido) {
        this.subido = subido;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Reporte() {
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, Operador> getOperadores() {
        return operadores;
    }

    public void setOperadores(HashMap<String, Operador> operadores) {
        this.operadores = operadores;
    }
}
