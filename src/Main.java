import ar.edu.unlu.poo.uno.model.clases.Partida;
import ar.edu.unlu.poo.uno.viewer.vista.VistaConsola;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VistaConsola consola = new VistaConsola();
            }
        });


        Partida partida = new Partida();
        partida.agregarJugador();
        partida.agregarJugador();

    }
}