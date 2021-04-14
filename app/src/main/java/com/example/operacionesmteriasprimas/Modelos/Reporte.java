package com.example.operacionesmteriasprimas.Modelos;

import java.io.Serializable;
import java.util.HashMap;

public class Reporte implements Serializable {
    String fecha;
    String supervisor;
    String id;
    String turno;
    HashMap<String,Operador> operadores;

    public Reporte(String fecha, String supervisor, String id, String turno, HashMap<String, Operador> operadores) {
        this.fecha = fecha;
        this.supervisor = supervisor;
        this.id = id;
        this.turno = turno;
        this.operadores = operadores;
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
