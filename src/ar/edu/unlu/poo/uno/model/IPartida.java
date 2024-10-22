package ar.edu.unlu.poo.uno.model;

import ar.edu.unlu.poo.uno.controller.ControladorVista;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IPartida {
    void agregarObserver(ControladorVista c) throws RemoteException;

    boolean actualizarCartasVista() throws RemoteException;

    int cantJugadores();

    boolean actualizarCartaDescarte() throws RemoteException;

    boolean tirarCarta(String op, String j) throws RemoteException;

    void agregarJugadorListo() throws RemoteException;

    boolean esTurno(String idJ);

    void elegirColor(String color, String idJ) throws RemoteException;

    boolean agregarJugador(String idJ);

    String[] getDescarte();

    ArrayList<String> getColores();

    ArrayList<String> getValores();

    ArrayList<Boolean> getValidas();

    void levantarCarta() throws RemoteException;

}
