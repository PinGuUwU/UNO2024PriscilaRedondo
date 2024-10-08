import ar.edu.unlu.poo.uno.model.clases.Partida;
import ar.edu.unlu.poo.uno.viewer.vista.VistaEleccion;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VistaEleccion consola = new VistaEleccion();
            }
        });


        Partida partida = new Partida();
        partida.agregarJugador();
        partida.agregarJugador();

    }
}