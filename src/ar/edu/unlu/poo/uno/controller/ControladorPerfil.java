package ar.edu.unlu.poo.uno.controller;

import ar.edu.unlu.poo.uno.model.Ranking;

import java.io.Serializable;

public class ControladorPerfil implements Serializable {
    //100% terminado
    Ranking ranking= new Ranking();
    public void actualizarNombreJugador(String idJ, String nuevoNombre){
        //Necesito crear una nueva instancia para poder actualizar el nombre en el historico
        ranking.actualizarNombreJugador(idJ, nuevoNombre);
    }
    public String[] datosJugadorID(String idJ){
        return ranking.buscarDatosJugadorID(idJ);
    }
}
