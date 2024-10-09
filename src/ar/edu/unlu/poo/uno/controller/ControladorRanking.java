package ar.edu.unlu.poo.uno.controller;

import ar.edu.unlu.poo.uno.model.clases.Jugador;
import ar.edu.unlu.poo.uno.model.clases.Ranking;
import ar.edu.unlu.poo.uno.viewer.vista.VistaRanking;

import java.util.ArrayList;
import java.util.List;

public class ControladorRanking {
    Ranking ranking;
    VistaRanking vista;

    public ControladorRanking(){
        ranking = new Ranking();
        vista = new VistaRanking();
    }
    //Tambien debe poder hacer ABM, pero le deja to-do el trabajo al ranking
    //Controlador se encarga de mandarle mensajes a la vista y de "traducir" ciertos mensajes
    //Por eso no conoce la estructura de ninguno de los dos, pero si que cosas puede mandarles

    public void top5Ganadores(){
        ArrayList<Jugador> jugadores = ranking.getTopGanadores();
        List<String> nombres = null;
        List<Integer> cantidad = null;
        for(int i=0; i<jugadores.size();i++){
            nombres.add(jugadores.get(i).name());
            cantidad.add(jugadores.get(i).partidasGanadas());
        }
        vista.actualizarRanking(nombres, cantidad);
    }
    public void top5Perdedores(){
        ArrayList<Jugador> jugadores = ranking.getTopPerdedores();
        List<String> nombres = null;
        List<Integer> cantidad = null;
        for(int i=0; i<jugadores.size();i++){
            nombres.add(jugadores.get(i).name());
            cantidad.add(jugadores.get(i).partidasPerdidas());
        }
        vista.actualizarRanking(nombres, cantidad);
    }
}
