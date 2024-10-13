package ar.edu.unlu.poo.uno.controller;

import ar.edu.unlu.poo.uno.model.clases.Partida;
import ar.edu.unlu.poo.uno.model.clases.Ranking;
import ar.edu.unlu.poo.uno.viewer.vista.VistaConsola;

public class ControladorConsola {
    Ranking ranking;
    Partida partida;
    ControladorPartida controladorP;
    VistaConsola consola;
    public ControladorConsola(ControladorPartida controlador, VistaConsola consola){
        ranking = new Ranking();
        this.partida = controlador.partida;
        controladorP = controlador;
        controladorP.conectar(ControladorConsola.this);
    }
    public ControladorConsola controlador(){
        return ControladorConsola.this;
    }
    public void opcion(String op){
        
    }
    public void iniciar(){
        partida.agregarJugadorListo();
    }
    //Aca se hace la ejecución de "comandos"/"Acciones" en el juego
    public String datosJugadorID(String id){
        return ranking.datosJugador(id);
    }
    public void actualizarDescarte(String color, int valor){
        consola.setDescarte(color, String.valueOf(valor));
    }
}
