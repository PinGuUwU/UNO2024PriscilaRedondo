package ar.edu.unlu.poo.uno.controller;

import ar.edu.unlu.poo.uno.model.clases.Jugador;
import ar.edu.unlu.poo.uno.model.clases.Mano;
import ar.edu.unlu.poo.uno.model.clases.Partida;
import ar.edu.unlu.poo.uno.model.clases.Ranking;
import ar.edu.unlu.poo.uno.viewer.vista.VistaRanking;

public class ControladorPartida {
    //Por ahora lo necesito para decirle a la partida el id que le corresponde al nuevo jugador.
    Ranking ranking = new Ranking();
    Partida partida;
    ControladorConsola controlador;
    public ControladorPartida(Partida partida){
        this.partida = partida;
        partida.conectar(ControladorPartida.this);
    }
    public void conectar(ControladorConsola controlador){
        this.controlador = controlador;
    }
    public void actualizarCartaTirada(String color, int valor){
        controlador.actualizarDescarte(color, valor);
    }
    public Partida partida(){
        return partida;
    }
    public String ultimoID(){
        return ranking.ultimoID();
    }
    public void cargarJugador(Jugador j){ ranking.agregarJugador(j);}
    public String datosJugadorName(String name){
        return ranking.buscarJugador(name);
    }
    public String buscarIdName(String name){
        String[] datos = (ranking.buscarJugador(name)).split(",");
        return datos[0];
    }
    public String existeJugador(String username){
        String id = ranking.buscarJugador(username);
        if(id != null){
            return ranking.datosJugador(id);
        } else {
            return null;
        }

    }
    public String agregarJugador(String username){

        return partida.agregarJugador(username);
    }
    public void actualizarCartasEnMano(Mano cartas){

    }
    public void actualizarPartidasGanadas(Jugador j){
        ranking.actualizarJugador(j);
    }
    public void actualizarPartidasPerdidas(Jugador j){
        ranking.actualizarJugador(j);
    }
}
