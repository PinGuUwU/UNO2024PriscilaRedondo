package ar.edu.unlu.poo.uno.model.cartas;

import ar.edu.unlu.poo.uno.model.Partida;

import java.io.Serializable;
import java.rmi.RemoteException;

public abstract class Carta implements Serializable {
    public final Color color;
    private final TipoCarta valor;

    public Carta(Color color, TipoCarta valor){
        this.color = color;
        this.valor = valor;
    }
    public TipoCarta valor(){
        return valor;
    }
    public Color color(){
        return color;
    }

    public boolean jugar(Partida partida) throws RemoteException {
        return false;
    }
}