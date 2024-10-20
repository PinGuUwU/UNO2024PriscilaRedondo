package ar.edu.unlu.poo.uno.viewer.vista;


import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IVista {
    void iniciar();
    void levantarCarta();
    void avisoInicio();
    void setDescarte(String color, String valor);
    void pedirCambioColor() throws RemoteException;
    void mostrarCartasJugador(ArrayList<String> colores, ArrayList<String> valores, ArrayList<Boolean> validos);
    void marcarNoListo();
    void setInTop();
}
