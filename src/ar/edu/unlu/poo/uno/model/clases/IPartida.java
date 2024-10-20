package ar.edu.unlu.poo.uno.model.clases;

import ar.edu.unlu.poo.uno.controller.ControladorVista;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IPartida {
    void iniciarPartida() throws RemoteException;

    int cantJugadoresListos();

    void agregarJugadorListo() throws RemoteException;

    /*
        Debo leer y comprobar la condicion de la mano del jugador
        este metodo se utiliza en el sistema principal del juego, la clase partida
        Solo se encarga de crear los metodos necesarios para una partida del uno
         */
    void iniciarTurno() throws RemoteException;

    /*
        En este metodo compruebo si se puede tirar o no con Condicion
        y luego tira la carta que el jugador decida
        y luego la agrega al mazo de descarte
         */

    boolean tirarCarta(String id, String posCarta) throws RemoteException;

    void pedirColorJugador() throws RemoteException;

    void elegirColor(String color, String idj) throws RemoteException;
    void agregarObserver(ControladorVista controlador) throws RemoteException;

    boolean agregarJugador(String id);

    /*
        Mas que nada comprueba el valor de turno y lo actualiza
         */
    int siguienteTurno(int turnoActual) throws RemoteException;

    void finalizarPartida(Jugador ganador) throws RemoteException;

    boolean actualizarCartaDescarte() throws RemoteException;
    boolean actualizarCartasVista() throws RemoteException;
    boolean esTurno(String id);
    void levantarCarta() throws RemoteException;
    TipoCarta esEspecial(Carta carta);
    String[] getDescarte();
    ArrayList<String> getColores();
    ArrayList<String> getValores();
    ArrayList<Boolean> getValidas();
    void actualizarInicioPartida() throws RemoteException;
    int cantJugadores();
}
