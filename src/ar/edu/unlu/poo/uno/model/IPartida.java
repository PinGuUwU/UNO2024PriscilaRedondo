package ar.edu.unlu.poo.uno.model;

import ar.edu.unlu.poo.uno.model.cartas.Color;
import ar.edu.unlu.poo.uno.model.cartas.TipoCarta;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IPartida extends IObservableRemoto{

    boolean actualizarCartasVista() throws RemoteException;

    int cantJugadores() throws RemoteException;

    int buscarNumeroCarta(int pos, String idJ) throws RemoteException;

    boolean actualizarCartaDescarte() throws RemoteException;

    boolean tirarCarta(String op, String j) throws RemoteException;

    void agregarJugadorListo() throws RemoteException;

    Color getColorDescarte() throws RemoteException;

    TipoCarta getTipoDescarte() throws RemoteException;

    int getNumeroDescarte() throws RemoteException;

    boolean esTurno(String idJ) throws RemoteException;

    void elegirColor(Color color, String idJ) throws RemoteException;

    void quitarJugador(String idJugador) throws RemoteException;

    boolean agregarJugador(String idJ) throws RemoteException;


    ArrayList<Color> getColores() throws RemoteException;

    ArrayList<TipoCarta> getValores() throws RemoteException;

    ArrayList<Boolean> getValidas() throws RemoteException;

    void levantarCarta() throws RemoteException;

    boolean estadoPartida() throws RemoteException;
}
