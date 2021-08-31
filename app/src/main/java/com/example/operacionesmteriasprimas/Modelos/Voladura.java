package com.example.operacionesmteriasprimas.Modelos;

import java.io.Serializable;
import java.util.ArrayList;

public class Voladura implements Serializable {

    String Autor;
    String CodigoVoladura;
    String Fecha;
    String Equipo;
    Double Diametro;
    Double Espaciamiento;
    Double AlturaBanco;
    Double SobrePerforacion;
    Double LongitudPerforacion;
    Integer NumeroPerforaciones;
    Integer NumeroFilas;
    Integer PerforacionesxFila;
    ArrayList<Explosivo> explosivos;
    Boolean subido;


    public Voladura() {
    }

    public Voladura(String autor, String codigoVoladura, String fecha, String equipo, Double diametro,
                    Double espaciamiento, Double alturaBanco, Double sobrePerforacion,
                    Double longitudPerforacion, Integer numeroPerforaciones, Integer numeroFilas,
                    Integer perforacionesxFila, ArrayList<Explosivo> explosivos, Boolean subido) {
        Autor = autor;
        CodigoVoladura = codigoVoladura;
        Fecha = fecha;
        Equipo = equipo;
        Diametro = diametro;
        Espaciamiento = espaciamiento;
        AlturaBanco = alturaBanco;
        SobrePerforacion = sobrePerforacion;
        LongitudPerforacion = longitudPerforacion;
        NumeroPerforaciones = numeroPerforaciones;
        NumeroFilas = numeroFilas;
        PerforacionesxFila = perforacionesxFila;
        this.explosivos = explosivos;
        this.subido = subido;
    }

    public Voladura(String codigoVoladura, String fecha, String equipo, Double diametro,
                    Double espaciamiento, Double alturaBanco, Double sobrePerforacion,
                    Double longitudPerforacion, Integer numeroPerforaciones, Integer numeroFilas,
                    Integer perforacionesxFila, ArrayList<Explosivo> explosivos) {
        CodigoVoladura = codigoVoladura;
        Fecha = fecha;
        Equipo = equipo;
        Diametro = diametro;
        Espaciamiento = espaciamiento;
        AlturaBanco = alturaBanco;
        SobrePerforacion = sobrePerforacion;
        LongitudPerforacion = longitudPerforacion;
        NumeroPerforaciones = numeroPerforaciones;
        NumeroFilas = numeroFilas;
        PerforacionesxFila = perforacionesxFila;
        this.explosivos = explosivos;
    }

    public String getAutor() {
        return Autor;
    }

    public void setAutor(String autor) {
        Autor = autor;
    }

    public Boolean getSubido() {
        return subido;
    }

    public void setSubido(Boolean subido) {
        this.subido = subido;
    }

    public String getCodigoVoladura() {
        return CodigoVoladura;
    }

    public void setCodigoVoladura(String codigoVoladura) {
        CodigoVoladura = codigoVoladura;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getEquipo() {
        return Equipo;
    }

    public void setEquipo(String equipo) {
        Equipo = equipo;
    }

    public Double getDiametro() {
        return Diametro;
    }

    public void setDiametro(Double diametro) {
        Diametro = diametro;
    }

    public Double getEspaciamiento() {
        return Espaciamiento;
    }

    public void setEspaciamiento(Double espaciamiento) {
        Espaciamiento = espaciamiento;
    }

    public Double getAlturaBanco() {
        return AlturaBanco;
    }

    public void setAlturaBanco(Double alturaBanco) {
        AlturaBanco = alturaBanco;
    }

    public Double getSobrePerforacion() {
        return SobrePerforacion;
    }

    public void setSobrePerforacion(Double sobrePerforacion) {
        SobrePerforacion = sobrePerforacion;
    }

    public Double getLongitudPerforacion() {
        return LongitudPerforacion;
    }

    public void setLongitudPerforacion(Double longitudPerforacion) {
        LongitudPerforacion = longitudPerforacion;
    }

    public Integer getNumeroPerforaciones() {
        return NumeroPerforaciones;
    }

    public void setNumeroPerforaciones(Integer numeroPerforaciones) {
        NumeroPerforaciones = numeroPerforaciones;
    }

    public Integer getNumeroFilas() {
        return NumeroFilas;
    }

    public void setNumeroFilas(Integer numeroFilas) {
        NumeroFilas = numeroFilas;
    }

    public Integer getPerforacionesxFila() {
        return PerforacionesxFila;
    }

    public void setPerforacionesxFila(Integer perforacionesxFila) {
        PerforacionesxFila = perforacionesxFila;
    }

    public ArrayList<Explosivo> getExplosivos() {
        return explosivos;
    }

    public void setExplosivos(ArrayList<Explosivo> explosivos) {
        this.explosivos = explosivos;
    }
}
