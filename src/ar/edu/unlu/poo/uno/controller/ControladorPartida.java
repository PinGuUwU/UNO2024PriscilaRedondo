package ar.edu.unlu.poo.uno.controller;

import ar.edu.unlu.poo.uno.model.clases.Jugador;
import ar.edu.unlu.poo.uno.model.clases.Partida;
import ar.edu.unlu.poo.uno.model.clases.Ranking;
import ar.edu.unlu.poo.uno.viewer.vista.VistaRanking;

public class ControladorPartida {
    //Por ahora lo necesito para decirle a la partida el id que le corresponde al nuevo jugador.
    Ranking ranking = new Ranking();
    public String ultimoID(){
        return ranking.ultimoID();
    }
    public void cargarJugador(Jugador j){ ranking.agregarJugador(j);}
    public String datosJugadorID(String id){
        return ranking.datosJugador(id);
    }
    public String datosJugadorName(String name){
        return ranking.buscarJugador(name);
    }
    public String existeJugador(String username){
        String id = ranking.buscarJugador(username);
        if(id != null){
            return ranking.datosJugador(id);
        } else {
            return null;
        }

    }
    public void actualizarPartidasGanadas(Jugador j){
        ranking.actualizarJugador(j);
    }
    public void actualizarPartidasPerdidas(Jugador j){
        ranking.actualizarJugador(j);
    }
}
