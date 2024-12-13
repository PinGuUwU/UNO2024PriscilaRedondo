package ar.edu.unlu.poo.uno.controller;

import ar.edu.unlu.poo.uno.model.Jugador;
import ar.edu.unlu.poo.uno.model.Partida;
import ar.edu.unlu.poo.uno.model.Serializacion;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ControladorPartidaGuardada implements Serializable {

    ControladorVista controlador;

    public ControladorPartidaGuardada(ControladorVista controlador){
        //Constructor
        this.controlador = controlador;
    }

    public void guardarPartida() throws IOException, ClassNotFoundException {
        //Guardo la instancia de la partida que se está jugando actualmente
        controlador.guardarPartidaActual();
    }

    public void cargarPartida(long id) throws IOException {
        //Cargo una partida mediante su ID
        controlador.cargarNuevaPartida(id);
    }
    public boolean buscarJugador(Jugador j) throws RemoteException {
        return controlador.buscarJugadorPorID(j.jugadorID());
    }

    public ArrayList<Partida> cargarPartidasGuardadas(String id) throws IOException, ClassNotFoundException {
        //Devuelvo las partidas en las cuales está presente el jugador con el ID pasado por parámetro
        return Serializacion.partidasGuardadasPorJugador(id);
    }

    public ArrayList<Jugador> jugadoresPartidaActual() throws RemoteException {
        //Devuelvo los jugadores de la partida actual, utilizado para comparación
        return controlador.jugadores();
    }

}
