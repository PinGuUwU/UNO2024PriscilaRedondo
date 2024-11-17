package ar.edu.unlu.poo.uno.listener;

import java.io.IOException;
import java.rmi.RemoteException;

public interface VentanaListener {
    void onVentanaCerrada(String ventana) throws IOException, ClassNotFoundException;
}
