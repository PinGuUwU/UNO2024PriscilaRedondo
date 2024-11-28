package ar.edu.unlu.poo.uno.model;

import java.io.Serializable;
import java.rmi.RemoteException;

public class TurnoPartida implements Serializable {
    private static final long serialVersionUID = 1L;
    /*
    Lleva el turno de la partida
    (0-3)
    verifica si el jugador ya levanto, si ya levanto entonces no puede volver a levantar, debe pasar o tirar una carta
     */
    int turno;
    int cantJugadores;
    boolean sentido;
    public TurnoPartida(int cantJugadores){
        this.turno=0;
        this.cantJugadores = cantJugadores;
    }
    public int turnoActual(){
        return this.turno;
    }
    public void seguirTurno(){
        this.turno = siguienteTurno();
    }
    public int siguienteTurno(){
        int turnoActual = this.turno;
        //Turno del 1-4 (0-3)
        if(sentido){
            if(turnoActual == (cantJugadores-1)){
                turnoActual = 0;
            } else {
                turnoActual++;
            }
        } else { //if(!sentido)
            if(turnoActual == 0){
                turnoActual =  (cantJugadores-1);
            } else {
                turnoActual--;
            }
        }
        return turnoActual;
    }
    public int turnoAnterior(){
        int turnoActual = this.turno;
        if(sentido){//Principio a fin
            if(turnoActual == 0){ //Primera persona de la ronda
                turnoActual = cantJugadores-1; //Ultima persona de la ronda
            } else {
                turnoActual --;
            }
        } else {//if(!sentido)//Fin a principio
            if(turnoActual == cantJugadores-1){//Ultima persona de la ronda
                turnoActual = 0;
            } else {
                turnoActual++;
            }
        }
        return turnoActual;
    }
}
