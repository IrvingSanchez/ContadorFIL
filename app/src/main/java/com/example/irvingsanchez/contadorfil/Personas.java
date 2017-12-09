package com.example.irvingsanchez.contadorfil;


import java.io.Serializable;

/**
 * Created by irvingsanchez on 12/09/17.
 */

public class Personas implements Serializable {

    private String area, ninio, ninia, jovenHombre,jovenMujer, hombre, mujer, discapacidad,fecha;

    public Personas(String area, String ninio, String ninia, String jovenHombre, String jovenMujer, String hombre, String mujer, String discapacidad, String fecha) {
        this.area = area;
        this.ninio = ninio;
        this.ninia = ninia;
        this.jovenHombre = jovenHombre;
        this.jovenMujer = jovenMujer;
        this.hombre = hombre;
        this.mujer = mujer;
        this.discapacidad = discapacidad;
        this.fecha = fecha;
    }

    public String getConteo()
    {
        String conteo;
        conteo = "\nNiños: " + ninio;
        conteo += "\nNiñas: " + ninia;
        conteo += "\nJóven Hombre: " + jovenHombre;
        conteo += "\nJóven Mujer: " + jovenMujer;
        conteo += "\nHombres: " + hombre;
        conteo += "\nMujeres: " + mujer;
        conteo += "\nDiscapacidad: " + discapacidad+ "\n";


        return conteo;
    }

    public String getTotal()
    {
        int total = toInt(ninio) + toInt(ninia) + toInt(hombre) +
                toInt(mujer) + toInt(discapacidad) + toInt(jovenHombre) + toInt(jovenMujer);
        return String.valueOf(total);
    }
    private int toInt(String string)
    {
        return Integer.valueOf(string);
    }

    public String getTitulo()
    {
        return area + " " + fecha;
    }

    public int getNinio() {
        return toInt(ninio);
    }

    public int getNinia() {
        return toInt(ninia);
    }

    public int getJovenHombre() {
        return toInt(jovenHombre);
    }

    public int getJovenMujer() {
        return toInt(jovenMujer);
    }

    public int getHombre() {
        return toInt(hombre);
    }

    public int getMujer() {
        return toInt(mujer);
    }

    public int getDiscapacidad() {
        return toInt(discapacidad);
    }
}
