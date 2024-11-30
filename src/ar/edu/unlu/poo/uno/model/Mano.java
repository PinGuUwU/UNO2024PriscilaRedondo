package ar.edu.unlu.poo.uno.model;

import ar.edu.unlu.poo.uno.model.cartas.Carta;

import java.io.Serializable;
import java.util.ArrayList;

public class Mano implements Serializable {
    private final ArrayList<Carta> manoActual;
    public Mano(){
        manoActual = new ArrayList<>();
    }

    public void entregarCarta(int pos){
        manoActual.remove(pos);
    }

    public void recibeCarta(Carta carta){
        manoActual.add(carta);
    }

    public Carta leerCartaMano(int pos){
        return manoActual.get(pos);
    }

    public int cantCartas(){
        return manoActual.size();
    }

}
