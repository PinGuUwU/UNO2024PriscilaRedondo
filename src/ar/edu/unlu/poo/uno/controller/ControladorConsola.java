package ar.edu.unlu.poo.uno.controller;

import ar.edu.unlu.poo.uno.model.clases.Partida;
import ar.edu.unlu.poo.uno.model.clases.Ranking;
import ar.edu.unlu.poo.uno.viewer.vista.VistaConsola;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ControladorConsola {
    Ranking ranking;
    Partida partida;
    ControladorPartida controladorP;
    VistaConsola consola;
    public ControladorConsola(ControladorPartida controlador){
        ranking = new Ranking();
        this.partida = controlador.partida;
        controladorP = controlador;
        controladorP.conectar(ControladorConsola.this);
    }
    public void conectar(VistaConsola consola){
        this.consola = consola;
    }
    public ControladorConsola controlador(){
        return ControladorConsola.this;
    }
    public boolean opcion(String op, String jugador) throws RemoteException {
        return partida.tirarCarta(jugador, op);
    }
    public void pedirElColor(){
        consola.pedirCambioColor();
    }
    public void actualizarCartasJugador(ArrayList<String> colores, ArrayList<String> valores, ArrayList<Boolean> posibles){
        consola.mostrarCartasJugador(colores, valores, posibles);
    }
    public void iniciar(String id) throws RemoteException {
        partida.agregarJugadorListo(id);
    }
    //Aca se hace la ejecución de "comandos"/"Acciones" en el juego
    public String datosJugadorID(String id){
        return ranking.datosJugador(id);
    }
    public void actualizarDescarte(String color, int valor){
        consola.setDescarte(color, String.valueOf(valor));
    }
    public void mostrarInicioPartida(){
        consola.avisoInicio();
    }
    public void levantarCartaObligatorio(){
        consola.levantarCarta();
    }
    public boolean esSuTurno(String idJ){
        return partida.esTurno(idJ);
    }
    public void cambiarColor(String color, String idj) throws RemoteException {
        partida.elegirColor(color, idj);
    }
}
