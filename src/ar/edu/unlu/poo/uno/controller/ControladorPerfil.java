package ar.edu.unlu.poo.uno.controller;

import ar.edu.unlu.poo.uno.model.Jugador;
import ar.edu.unlu.poo.uno.model.Ranking;
import ar.edu.unlu.poo.uno.model.Serializacion;

import java.io.IOException;
import java.io.Serializable;

public class ControladorPerfil implements Serializable {
    //100% terminado
    public void actualizarNombreJugador(String idJ, String nuevoNombre) throws IOException, ClassNotFoundException {
        //Necesito crear una nueva instancia para poder actualizar el nombre en el historico
        Serializacion.actualizarNombreJugador(idJ, nuevoNombre);
    }
    public String[] datosJugadorID(String idJ) throws IOException, ClassNotFoundException {
        //recibe un jugador y lo transforma
        String[] datos = new String[4];
        Jugador j = Serializacion.buscarDatosDeJugadorPorID(idJ);
        datos[0] = j.jugadorID();
        datos[1] = j.name();
        datos[2] = String.valueOf(j.partidasGanadas());
        datos[3] = String.valueOf(j.partidasPerdidas());
        return datos;
    }
}
