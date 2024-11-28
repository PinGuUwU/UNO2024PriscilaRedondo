package ar.edu.unlu.poo.uno.controller;

import ar.edu.unlu.poo.uno.model.Jugador;
import ar.edu.unlu.poo.uno.model.Partida;
import ar.edu.unlu.poo.uno.model.Serializacion;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ControladorPartidasGuardadas implements Serializable {
    ControladorVista controlador;
    public ControladorPartidasGuardadas(ControladorVista controlador){
        this.controlador = controlador;
    }
    public ArrayList<Partida> cargarPartidasGuardadas(String id) throws IOException, ClassNotFoundException {
        return Serializacion.partidasGuardadas(id);
    }
    public void cargarPartida(Partida p) throws RemoteException {
        controlador.cargarNuevaPartida(p);
    }
    public ArrayList<Jugador> jugadoresPartidaActual() throws RemoteException {
        return controlador.jugadores();
    }
    public void guardarPartida() throws IOException, ClassNotFoundException {
        controlador.guardarPartidaActual();
    }
}
