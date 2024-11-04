package ar.edu.unlu.poo.uno.model;

import ar.edu.unlu.poo.uno.controller.ControladorVista;
import ar.edu.unlu.poo.uno.model.cartas.Color;
import ar.edu.unlu.poo.uno.model.cartas.TipoCarta;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IPartida extends IObservableRemoto{

    void actualizarCartasVista() throws RemoteException;

    int cantJugadores() throws RemoteException;

    void agregarObserver(ControladorVista controlador) throws RemoteException;

    boolean yaEmpezo() throws RemoteException;

    int buscarNumeroCarta(int pos, String idJ) throws RemoteException;

    void actualizarCartaDescarte() throws RemoteException;

    boolean tirarCarta(String op, String j) throws RemoteException;

    void agregarJugadorListo() throws RemoteException;

    Color getColorDescarte() throws RemoteException;

    TipoCarta getTipoDescarte() throws RemoteException;

    int getNumeroDescarte() throws RemoteException;

    boolean esTurno(String idJ) throws RemoteException;

    void elegirColor(Color color, String idJ) throws RemoteException;

    void quitarJugador(String idJugador, ControladorVista cr) throws RemoteException;

    boolean agregarJugador(String idJ) throws RemoteException;


    ArrayList<Color> getColores(String id) throws RemoteException;

    ArrayList<TipoCarta> getValores(String id) throws RemoteException;

    ArrayList<Boolean> getValidas(String id) throws RemoteException;

    void levantarCarta() throws RemoteException;

}
