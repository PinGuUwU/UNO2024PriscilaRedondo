package ar.edu.unlu.poo.uno.servidor;

import ar.edu.unlu.poo.uno.model.Partida;
import ar.edu.unlu.poo.uno.model.Serializacion;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.servidor.Servidor;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class AppServidor {
    public static void main(String[] args) throws IOException {
        ArrayList<String> ips = Util.getIpDisponibles();
        /*
        String ip = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que escuchará peticiones el servidor", "IP del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),
                null
        );
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchará peticiones el servidor", "Puerto del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                8888
        );*/
        Partida modelo = new Partida(Serializacion.ultimoIDPartida());
        Servidor servidor = new Servidor("127.0.0.1", 8888);
        try {
            servidor.iniciar(modelo);
            System.out.println("Servidor RMI está en ejecución...");

            // Mantener el servidor en ejecución
            synchronized (AppServidor.class) {
                AppServidor.class.wait(); // Espera indefinidamente
            }
        } catch(RemoteException | RMIMVCException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
