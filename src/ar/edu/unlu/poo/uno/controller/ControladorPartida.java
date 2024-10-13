package ar.edu.unlu.poo.uno.controller;

import ar.edu.unlu.poo.uno.model.clases.Jugador;
import ar.edu.unlu.poo.uno.model.clases.Partida;
import ar.edu.unlu.poo.uno.model.clases.Ranking;

import java.util.ArrayList;

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
    public void pedirColor(){
        controlador.pedirElColor();
    }
    public Partida partida(){
        return partida;
    }
    public String ultimoID(){
        return ranking.ultimoID();
    }
    public void cargarJugador(Jugador j){ ranking.agregarJugador(j);}
    public String[] datosDataJugadorName(String name){
        return ranking.buscarDatosJugadorName(name);
    }
    public String buscarIdName(String id){
        String[] datos = (ranking.buscarDatosJugadorID(id)).split(",");
        return datos[1];
    }
    public String existeJugador(String username){
        return ranking.buscarIDJugadorName(username);
    }
    public String agregarJugador(String username){
        return partida.agregarJugador(username);
    }
    public void actualizarCartasEnMano(ArrayList<String> colores, ArrayList<String> valores, ArrayList<Boolean> posibles){
        controlador.actualizarCartasJugador(colores, valores, posibles);
    }
    public void actualizarPartidasGanadas(Jugador j){
        ranking.actualizarJugador(j);
    }
    public void actualizarPartidasPerdidas(Jugador j){
        ranking.actualizarJugador(j);
    }
    public void avisarInicioPartida(){
        controlador.mostrarInicioPartida();
    }
    public void debeLevantarCarta(){
        controlador.levantarCartaObligatorio();
    }
}
