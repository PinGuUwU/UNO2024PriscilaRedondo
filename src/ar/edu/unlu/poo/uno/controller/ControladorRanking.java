package ar.edu.unlu.poo.uno.controller;

import ar.edu.unlu.poo.uno.model.Jugador;
import ar.edu.unlu.poo.uno.model.Ranking;
import ar.edu.unlu.poo.uno.viewer.vista.VistaRanking;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ControladorRanking implements Serializable {
    Ranking ranking;
    VistaRanking vista;

    public ControladorRanking(VistaRanking vista){
        //Constructor
        this.vista = vista;
        ranking = new Ranking();
    }

    public void top5Ganadores() throws IOException, ClassNotFoundException {
        //Devuelve una lista con los 5 jugadores con mayor cantidad de partidas ganadas
        ArrayList<Jugador> jugadores = ranking.getTopGanadores();
        List<String> nombres = new ArrayList<>();
        List<Integer> cantidad = new ArrayList<>();
        for (Jugador j : jugadores) {
            nombres.add(j.name());
            cantidad.add(j.partidasGanadas());
        }
        vista.actualizarRanking(nombres, cantidad);
    }

    public void top5Perdedores() throws IOException, ClassNotFoundException {
        //Devuelve una lista con los 5 jugadores con mayor cantidad de partidas perdidas
        ArrayList<Jugador> jugadores = ranking.getTopPerdedores();
        List<String> nombres = new ArrayList<>();
        List<Integer> cantidad = new ArrayList<>();
        for (Jugador j : jugadores) {
            nombres.add(j.name());
            cantidad.add(j.partidasPerdidas());
        }
        vista.actualizarRanking(nombres, cantidad);
    }
}
