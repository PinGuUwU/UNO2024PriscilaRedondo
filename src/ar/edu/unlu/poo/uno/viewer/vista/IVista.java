package ar.edu.unlu.poo.uno.viewer.vista;


import ar.edu.unlu.poo.uno.model.cartas.Color;
import ar.edu.unlu.poo.uno.model.cartas.TipoCarta;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IVista {
    void iniciar();

    void esperandoInicio() throws RemoteException;

    void otroJugadorListo() throws RemoteException;

    void avisoInicio();

    void setDescarte(Color color, TipoCarta valor) throws RemoteException;

    void pedirCambioColor() throws RemoteException;

    void mostrarCartasJugador(ArrayList<Color> colores, ArrayList<TipoCarta> valores, ArrayList<Boolean> validos) throws RemoteException;

    void yaSeApelo();

    void marcarNoListo();
    void marcarListo();

    void seCambioElColor();

    int cantJugadoresListos() throws RemoteException;

    int cantJugadoresTotal() throws RemoteException;

    void yaLevanto() throws RemoteException;

    void noDesafiar();

    void decirUNO();

    void desafiarJugadorAnterior() throws RemoteException;

    void avisarQueNoDijoUNO() throws RemoteException;

    void puedeDesafiar();

    void pasarTurno() throws IOException, ClassNotFoundException;

    void mostrarOpcionPasarTurno();
}
