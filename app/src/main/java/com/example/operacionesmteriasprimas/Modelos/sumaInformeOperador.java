package com.example.operacionesmteriasprimas.Modelos;

import java.util.List;

public class sumaInformeOperador {
    String nombreOperador;
    List<sumas> listaactividades;

    public sumaInformeOperador(String nombreOperador, List<sumas> listaactividades) {
        this.nombreOperador = nombreOperador;
        this.listaactividades = listaactividades;
    }

    public String getNombreOperador() {
        return nombreOperador;
    }

    public void setNombreOperador(String nombreOperador) {
        this.nombreOperador = nombreOperador;
    }

    public List<sumas> getListaactividades() {
        return listaactividades;
    }

    public void setListaactividades(List<sumas> listaactividades) {
        this.listaactividades = listaactividades;
    }
}
