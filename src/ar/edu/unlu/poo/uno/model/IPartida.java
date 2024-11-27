package ar.edu.unlu.poo.uno.model;

import ar.edu.unlu.poo.uno.model.cartas.Color;
import ar.edu.unlu.poo.uno.model.cartas.TipoCarta;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IPartida extends IObservableRemoto{

    void actualizarYaNoHayDesafio() throws RemoteException;

    boolean actualizarCartasVista() throws RemoteException;

    int cantJugadores() throws RemoteException;

    int buscarNumeroCarta(int pos, String idJ) throws RemoteException;

    boolean actualizarCartaDescarte() throws RemoteException;

    int cantJugadoresListos() throws RemoteException;

    ArrayList<Color> getColores(String id) throws RemoteException;

    ArrayList<TipoCarta> getValores(String id) throws RemoteException;

    ArrayList<Boolean> getValidas(String id) throws RemoteException;

    void agregarJugadorListo() throws IOException, ClassNotFoundException;

    Color getColorDescarte() throws RemoteException;

    TipoCarta getTipoDescarte() throws RemoteException;

    int getNumeroDescarte() throws RemoteException;

    boolean esTurno(String idJ) throws RemoteException;

    boolean tirarCarta(String id, String posCarta, boolean uno) throws IOException, ClassNotFoundException;

    void elegirColor(Color color, String idJ) throws RemoteException;

    void actualizarPorCambio() throws RemoteException;

    void quitarJugador(String idJ) throws IOException, ClassNotFoundException;

    boolean agregarJugador(String idJ) throws IOException, ClassNotFoundException;

    void noDesafia() throws RemoteException ;

    void levantarCarta() throws RemoteException;

    boolean estadoPartida() throws RemoteException;


    void jugadorPaso() throws IOException, ClassNotFoundException;


    void noDijoUNO() throws RemoteException;

    void desafio() throws RemoteException;

    Mano manoJugadorAnterior() throws RemoteException;
}
