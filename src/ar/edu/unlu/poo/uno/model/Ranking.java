package ar.edu.unlu.poo.uno.model;


import java.io.*;
import java.util.*;

public class Ranking implements Serializable{
    public ArrayList<Jugador> getTopGanadores() throws IOException, ClassNotFoundException {
        ArrayList<Jugador> jugadores = Serializacion.jugadores();
        /*Esto de jugadores.sort sirve para poder recorrer to-do el ArrayList
        y va comparando las partidas ganadas miembro a miembro
        lo ordena de mayor a menor
         */
        jugadores.sort(new Comparator<Jugador>() {
            @Override
            public int compare(Jugador j1, Jugador j2) {
                return Integer.compare(j2.partidasGanadas(), j1.partidasGanadas());
            }
        });
        //Esta operacion es muy rebuscada
        //.subList(0, 5)  devuelve una sublista de las primeras 5 posiciones (mejores jugadores)
        // y Math.min(5, .size) hace que si hay menos de 5 jugadores en la ArrayList, te devuelva esos que haya
        return new ArrayList<>(jugadores.subList(0, Math.min(5, jugadores.size())));
    }
    public ArrayList<Jugador> getTopPerdedores() throws IOException, ClassNotFoundException {
        ArrayList<Jugador> jugadoresAll = Serializacion.jugadores();
        /*Esto de jugadoresAll.sort sirve para poder recorrer to-do el ArrayList
        y va comparando las partidas ganadas miembro a miembro
        lo ordena de mayor a menor
         */
        jugadoresAll.sort(new Comparator<Jugador>() {
            @Override
            public int compare(Jugador j1, Jugador j2) {
                return Integer.compare(j2.partidasPerdidas(), j1.partidasPerdidas());
            }
        });
        //Esta operacion es muy rebuscada
        //.subList(0, 5)  devuelve una sublista de las primeras 5 posiciones (mejores jugadores)
        // y Math.min(5, .size) hace que si hay menos de 5 jugadores en la ArrayList, te devuelva esos que haya
        return new ArrayList<>(jugadoresAll.subList(0, Math.min(5, jugadoresAll.size())));
    }

}
