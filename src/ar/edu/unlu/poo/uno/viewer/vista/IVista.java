package ar.edu.unlu.poo.uno.viewer.vista;


import ar.edu.unlu.poo.uno.model.cartas.Color;
import ar.edu.unlu.poo.uno.model.cartas.TipoCarta;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IVista {
    void iniciar();
    void levantarCarta();
    void avisoInicio();
    void setDescarte(Color color, TipoCarta valor) throws RemoteException;

    void pedirCambioColor() throws RemoteException;
    void mostrarCartasJugador(ArrayList<Color> colores, ArrayList<TipoCarta> valores, ArrayList<Boolean> validos) throws RemoteException;
    void marcarNoListo();
    void marcarListo();
}
