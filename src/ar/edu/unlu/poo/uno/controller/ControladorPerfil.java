package ar.edu.unlu.poo.uno.controller;

import ar.edu.unlu.poo.uno.model.Ranking;

public class ControladorPerfil {
    //100% terminado
    Ranking ranking;
    public void actualizarNombreJugador(String idJ, String nuevoNombre){
        //Necesito crear una nueva instancia para poder actualizar el nombre en el historico
        ranking = new Ranking();
        ranking.actualizarNombreJugador(idJ, nuevoNombre);
    }
}
