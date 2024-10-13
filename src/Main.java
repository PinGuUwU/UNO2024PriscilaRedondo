import ar.edu.unlu.poo.uno.controller.ControladorConsola;
import ar.edu.unlu.poo.uno.controller.ControladorPartida;
import ar.edu.unlu.poo.uno.model.clases.Jugador;
import ar.edu.unlu.poo.uno.model.clases.Partida;
import ar.edu.unlu.poo.uno.viewer.vista.VistaEleccion;
import ar.edu.unlu.poo.uno.viewer.vista.VistaInicio;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        Partida partida = new Partida();
        ControladorPartida controladorP = new ControladorPartida(partida);
        ControladorConsola controladorC = new ControladorConsola(controladorP);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VistaInicio inicio = new VistaInicio(controladorP);
            }
        });

        //En realidad debo empezar preguntando el usuario, buscando en el historico y de no existir aviso que no existe
        //Y pregunto si desea ingresar un nuevo usuario o crear un perfil con el usuario que había escrito
        //Si se encontro entonces lo abre y lo deriva a VistaEleccion creo
    }
}