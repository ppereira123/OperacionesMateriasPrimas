package com.example.operacionesmteriasprimas.Modelos;

import java.io.Serializable;
import java.util.ArrayList;

public class Explosivo implements Serializable {
    String key;
    String tipo;
    Integer Egreso;
    Integer Ingreso;
    Double preciounidad;
    Integer Saldo;


    public Explosivo() {
    }

    public Explosivo(String tipo, Integer egreso, Integer ingreso, Double preciounidad) {
        this.tipo = tipo;
        Egreso = egreso;
        Ingreso = ingreso;
        this.preciounidad = preciounidad;
    }

    public Explosivo(String tipo, Integer egreso, Integer ingreso) {
        this.tipo = tipo;
        Egreso = egreso;
        Ingreso = ingreso;
    }

    public Explosivo(String tipo, Integer egreso, Integer ingreso, Double preciounidad, Integer saldo) {
        this.tipo = tipo;
        Egreso = egreso;
        Ingreso = ingreso;
        this.preciounidad = preciounidad;
        Saldo = saldo;
    }

    public Explosivo(String key, String tipo, Double preciounidad, Integer saldo) {
        this.key = key;
        this.tipo = tipo;
        this.preciounidad = preciounidad;
        Saldo = saldo;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getEgreso() {
        return Egreso;
    }

    public void setEgreso(Integer egreso) {
        Egreso = egreso;
    }

    public Integer getIngreso() {
        return Ingreso;
    }

    public void setIngreso(Integer ingreso) {
        Ingreso = ingreso;
    }

    public Double getPreciounidad() {
        return preciounidad;
    }

    public void setPreciounidad(Double preciounidad) {
        this.preciounidad = preciounidad;
    }

    public Integer getSaldo() {
        return Saldo;
    }

    public void setSaldo(Integer saldo) {
        Saldo = saldo;
    }
}
